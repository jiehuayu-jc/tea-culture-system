package com.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.utils.DeepSeekClient;
import com.utils.EmbeddingProvider;

/**
 * 茶道AI · RAG 知识服务。
 *
 * <p>检索采用两阶段架构：
 * <ol>
 *   <li><b>召回层</b>：BM25 稀疏检索（纯 Java，无外部依赖）。若容器中存在
 *       {@link EmbeddingProvider} 实现，则同时做稠密向量召回，两路结果用
 *       RRF（Reciprocal Rank Fusion）融合；没有实现时自动降级为单路 BM25。</li>
 *   <li><b>重排层</b>：把召回候选交给大模型按语义相关性重排，只保留真正相关的片段。
 *       大模型未配置或调用失败时，直接返回召回层顺序，链路不中断。</li>
 * </ol>
 */
@Service
public class TeaRagService {

    /** RRF 融合常数，取信息检索领域经验值 */
    private static final int RRF_K = 60;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /** 稠密向量通道。容器中没有任何实现 Bean 时为 null，检索自动走单路 BM25。 */
    @Autowired(required = false)
    private EmbeddingProvider embeddingProvider;

    /**
     * 文档向量缓存：key 为 ai_knowledge.id。
     * 稠密检索若每次查询都现算全库向量，会拖慢每个请求；这里改为懒加载 + 启动预热。
     * null 表示尚未构建；知识库重建时置空以失效。
     */
    private volatile Map<Long, float[]> docVecCache = null;

    // ==================== 知识入库 ====================

    /** 启动时知识库为空则自动建库，保证开箱可演示 */
    @org.springframework.context.event.EventListener(org.springframework.boot.context.event.ApplicationReadyEvent.class)
    public void autoSeed() {
        org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(TeaRagService.class);
        try {
            if (count() == 0) {
                Map<String, Object> r = rebuild();
                log.info("[茶道AI] 知识库自动建库完成，共 {} 条", r.get("knowledge_count"));
            } else {
                log.info("[茶道AI] 知识库已有 {} 条内容，跳过自动建库", count());
            }
            warmupDenseCache();
        } catch (Exception e) {
            log.error("[茶道AI] 知识库初始化失败：ai_knowledge 表不存在或不可写。"
                    + "请先执行 db/springbootj8kskvkr.sql（文件末尾包含 ai_knowledge 建表语句）后重启服务。", e);
        }
    }

    /** 后台预热稠密向量缓存：避免首次检索时才逐条编码导致卡顿 */
    private void warmupDenseCache() {
        if (embeddingProvider == null || !embeddingProvider.available()) return;
        Thread t = new Thread(() -> {
            try {
                docVecCache = buildDocVectorCache();
            } catch (Exception e) {
                org.slf4j.LoggerFactory.getLogger(TeaRagService.class)
                        .warn("[茶道AI] 稠密向量预热失败：{}", e.getMessage());
            }
        }, "tea-rag-embedding-warmup");
        t.setDaemon(true);
        t.start();
    }

    /** 将站内内容重建进知识库 */
    public Map<String, Object> rebuild() {
        jdbcTemplate.update("DELETE FROM ai_knowledge");
        docVecCache = null;   // 知识库内容已变，文档向量缓存失效
        int n = 0;
        // 茶文化（jiaoxueshipin）
        n += ingest("SELECT id, biaoti, jibenjieshao FROM jiaoxueshipin", "article", "biaoti", "jibenjieshao");
        // 购物资讯（news）
        n += ingest("SELECT id, title, content FROM news", "news", "title", "content");
        // 商品介绍（shangpinxinxi）
        n += ingest("SELECT id, shangpinmingcheng, shangpinjieshao FROM shangpinxinxi", "tea", "shangpinmingcheng", "shangpinjieshao");
        // 线上讲座（xinlizixun）
        n += ingest("SELECT id, zixunmingcheng, zixunxiangqing FROM xinlizixun", "lecture", "zixunmingcheng", "zixunxiangqing");
        Map<String, Object> r = new HashMap<>();
        r.put("knowledge_count", n);
        return r;
    }

    private int ingest(String query, String type, String titleCol, String contentCol) {
        int n = 0;
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(query);
        for (Map<String, Object> row : rows) {
            Object id = row.get("id");
            String title = String.valueOf(row.get(titleCol) == null ? "" : row.get(titleCol));
            String content = stripHtml(String.valueOf(row.get(contentCol) == null ? "" : row.get(contentCol)));
            if (title.isEmpty() || content.isEmpty()) continue;
            jdbcTemplate.update("INSERT INTO ai_knowledge (source_type, source_id, title, content) VALUES (?,?,?,?)",
                    type, id, title, content.length() > 4000 ? content.substring(0, 4000) : content);
            n++;
        }
        return n;
    }

    private String stripHtml(String s) {
        return s.replaceAll("<[^>]+>", "").replaceAll("&nbsp;", " ").replaceAll("\\s+", " ").trim();
    }

    public int count() {
        List<Integer> list = jdbcTemplate.queryForList("SELECT COUNT(*) FROM ai_knowledge", Integer.class);
        return list.isEmpty() ? 0 : list.get(0);
    }

    // ==================== 检索 ====================

    /** 兼容既有调用：不启用大模型重排，直接返回召回结果 */
    public List<Map<String, Object>> search(String query, int top) {
        return search(query, top, null);
    }

    /**
     * 两阶段检索入口。
     *
     * @param top 最终返回条数
     * @param llm 大模型客户端；为 null 或未配置时跳过重排，直接返回召回结果
     */
    public List<Map<String, Object>> search(String query, int top, DeepSeekClient llm) {
        // 召回放大：多取候选，为重排留出空间（top*4，上限 24）
        int recallN = Math.min(Math.max(top * 4, 10), 24);
        List<Map<String, Object>> cands = recall(query, recallN);
        // 只有 0~1 条候选时无从重排，直接返回
        if (cands.size() <= 1) return cands;
        if (llm == null || !llm.isConfigured()) {
            return new ArrayList<>(cands.subList(0, Math.min(top, cands.size())));
        }
        try {
            List<Map<String, Object>> reranked = llmRerank(query, cands, llm);
            if (reranked != null && !reranked.isEmpty()) {
                List<Map<String, Object>> out = new ArrayList<>(reranked.subList(0, Math.min(top, reranked.size())));
                for (Map<String, Object> h : out) {
                    h.put("reranked", true);   // 标记：该条经过大模型语义重排
                }
                return out;
            }
        } catch (Exception ignore) {
            // 重排属于增益步骤，失败不得影响主链路
        }
        return new ArrayList<>(cands.subList(0, Math.min(top, cands.size())));
    }

    /** 召回层：稀疏 BM25 +（可选）稠密向量，RRF 融合 */
    private List<Map<String, Object>> recall(String query, int n) {
        List<Map<String, Object>> sparse = bm25(query, n);
        if (embeddingProvider == null || !embeddingProvider.available()) {
            return sparse;
        }
        float[] qv = embeddingProvider.embed(query);
        if (qv == null) return sparse;
        List<Map<String, Object>> dense = denseRecall(qv, n);
        if (dense.isEmpty()) return sparse;
        return rrf(sparse, dense, n);
    }

    /**
     * 稠密路召回：对全部知识片段做余弦相似度排序。
     * 文档向量走缓存（首次构建后复用），避免每次查询都调用 embedding 接口。
     */
    private List<Map<String, Object>> denseRecall(float[] qv, int n) {
        Map<Long, float[]> cache = docVecCache;
        if (cache == null) {
            cache = buildDocVectorCache();
            docVecCache = cache;
        }
        if (cache.isEmpty()) return new ArrayList<>();

        List<Map<String, Object>> docs = jdbcTemplate.queryForList(
                "SELECT id, source_type, source_id, title, content FROM ai_knowledge");
        List<Map<String, Object>> scored = new ArrayList<>();
        for (Map<String, Object> doc : docs) {
            Object idObj = doc.get("id");
            if (idObj == null) continue;
            float[] dv = cache.get(Long.valueOf(String.valueOf(idObj)));
            if (dv == null) continue;
            double sim = cosine(qv, dv);
            if (sim <= 0) continue;
            scored.add(toHit(doc, sim));
        }
        scored.sort((a, b) -> Double.compare((Double) b.get("score"), (Double) a.get("score")));
        return scored.subList(0, Math.min(n, scored.size()));
    }

    /** 构建文档向量缓存：一次性把全部知识片段编码入库 */
    private Map<Long, float[]> buildDocVectorCache() {
        Map<Long, float[]> cache = new HashMap<>();
        if (embeddingProvider == null || !embeddingProvider.available()) return cache;
        List<Map<String, Object>> docs = jdbcTemplate.queryForList(
                "SELECT id, title, content FROM ai_knowledge");
        long t0 = System.currentTimeMillis();
        int failed = 0;
        for (Map<String, Object> doc : docs) {
            Object idObj = doc.get("id");
            if (idObj == null) continue;
            float[] v = embeddingProvider.embed(doc.get("title") + " " + doc.get("content"));
            if (v == null) {
                failed++;
                if (!embeddingProvider.available()) break;   // 熔断触发，放弃本轮
                continue;
            }
            cache.put(Long.valueOf(String.valueOf(idObj)), v);
        }
        org.slf4j.LoggerFactory.getLogger(TeaRagService.class).info(
                "[茶道AI] 稠密向量缓存构建完成：成功 {} 条，失败 {} 条，耗时 {} ms",
                cache.size(), failed, System.currentTimeMillis() - t0);
        return cache;
    }

    /** 倒数排名融合：score(d) = Σ 1/(RRF_K + rank_i(d)) */
    private List<Map<String, Object>> rrf(List<Map<String, Object>> a, List<Map<String, Object>> b, int n) {
        Map<String, Double> fused = new LinkedHashMap<>();
        Map<String, Map<String, Object>> byKey = new LinkedHashMap<>();
        accumulate(fused, byKey, a);
        accumulate(fused, byKey, b);
        List<Map<String, Object>> out = new ArrayList<>();
        for (Map.Entry<String, Double> e : fused.entrySet()) {
            Map<String, Object> hit = byKey.get(e.getKey());
            hit.put("rrf", Math.round(e.getValue() * 10000) / 10000.0);
            out.add(hit);
        }
        out.sort((x, y) -> Double.compare((Double) y.get("rrf"), (Double) x.get("rrf")));
        return out.subList(0, Math.min(n, out.size()));
    }

    private void accumulate(Map<String, Double> fused, Map<String, Map<String, Object>> byKey,
                            List<Map<String, Object>> list) {
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> hit = list.get(i);
            String key = hit.get("source_type") + "#" + hit.get("source_id") + "#" + hit.get("title");
            fused.merge(key, 1.0 / (RRF_K + i + 1), Double::sum);
            byKey.putIfAbsent(key, hit);
        }
    }

    private double cosine(float[] a, float[] b) {
        int len = Math.min(a.length, b.length);
        double dot = 0, na = 0, nb = 0;
        for (int i = 0; i < len; i++) {
            dot += a[i] * b[i];
            na += a[i] * a[i];
            nb += b[i] * b[i];
        }
        if (na == 0 || nb == 0) return 0;
        return dot / (Math.sqrt(na) * Math.sqrt(nb));
    }

    /** BM25 打分（稀疏路） */
    private List<Map<String, Object>> bm25(String query, int top) {
        List<Map<String, Object>> docs = jdbcTemplate.queryForList(
                "SELECT id, source_type, source_id, title, content FROM ai_knowledge");
        List<String> terms = tokenize(query);
        List<Map<String, Object>> result = new ArrayList<>();
        if (docs.isEmpty() || terms.isEmpty()) return result;

        int N = docs.size();
        double avgLen = 0;
        List<Map<String, Integer>> docTf = new ArrayList<>();
        List<Integer> docLen = new ArrayList<>();
        for (Map<String, Object> doc : docs) {
            List<String> tokens = tokenize(doc.get("title") + " " + doc.get("content"));
            Map<String, Integer> tf = new HashMap<>();
            for (String t : tokens) tf.merge(t, 1, Integer::sum);
            docTf.add(tf);
            docLen.add(tokens.size());
            avgLen += tokens.size();
        }
        avgLen = avgLen / (double) N;

        double k1 = 1.5, b = 0.75;
        for (int i = 0; i < N; i++) {
            double score = 0;
            boolean titled = false;
            String title = String.valueOf(docs.get(i).get("title"));
            for (String term : terms) {
                int f = docTf.get(i).getOrDefault(term, 0);
                if (f == 0) {
                    // 二元组未命中时尝试"包含"匹配（标题包含给高分）
                    if (title.contains(term)) { score += 2.0; titled = true; }
                    continue;
                }
                double idf = Math.log(1 + (N - countDocsContaining(docTf, term) + 0.5) / (countDocsContaining(docTf, term) + 0.5));
                score += idf * f * (k1 + 1) / (f + k1 * (1 - b + b * docLen.get(i) / avgLen));
            }
            if (score <= 0) continue;
            Map<String, Object> hit = toHit(docs.get(i), Math.round(score * 100) / 100.0);
            if (titled) hit.put("title_hit", true);
            result.add(hit);
        }
        result.sort((a, b2) -> Double.compare((Double) b2.get("score"), (Double) a.get("score")));
        return result.subList(0, Math.min(top, result.size()));
    }

    /** 统一构造检索结果条目 */
    private Map<String, Object> toHit(Map<String, Object> doc, double score) {
        String content = String.valueOf(doc.get("content"));
        Map<String, Object> hit = new HashMap<>();
        hit.put("title", doc.get("title"));
        hit.put("source_type", doc.get("source_type"));
        hit.put("source_id", doc.get("source_id"));
        hit.put("score", score);
        hit.put("snippet", content.length() > 180 ? content.substring(0, 180) + "…" : content);
        return hit;
    }

    /**
     * 重排层：让大模型按语义相关性给候选排序，并剔除不相关项。
     * 只认模型返回的序号顺序；任一步失败即抛异常，由调用方降级。
     */
    private List<Map<String, Object>> llmRerank(String query, List<Map<String, Object>> cands, DeepSeekClient llm)
            throws Exception {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < cands.size(); i++) {
            Map<String, Object> c = cands.get(i);
            sb.append(i + 1).append(". 【").append(c.get("title")).append("】")
              .append(c.get("snippet")).append("\n");
        }
        JSONArray messages = new JSONArray();
        messages.add(DeepSeekClient.message("system",
                "你是检索重排器。根据用户问题，从候选文档中挑出真正相关的，按相关性从高到低排序。"
                        + "只输出 JSON，格式：{\"order\":[序号,序号]}。不相关的不要列入；若全都不相关则输出 {\"order\":[]}。"));
        messages.add(DeepSeekClient.message("user", "问题：" + query + "\n\n候选文档：\n" + sb));
        JSONObject resp = llm.chatOnce(messages, null);
        String content = resp == null ? null : resp.getString("content");
        if (content == null) return null;
        int a = content.indexOf('{'), z = content.lastIndexOf('}');
        if (a < 0 || z <= a) return null;
        JSONObject obj = JSON.parseObject(content.substring(a, z + 1));
        JSONArray order = obj == null ? null : obj.getJSONArray("order");
        if (order == null) return null;
        List<Map<String, Object>> out = new ArrayList<>();
        for (int i = 0; i < order.size(); i++) {
            int idx = order.getIntValue(i) - 1;
            if (idx >= 0 && idx < cands.size()) {
                out.add(cands.get(idx));
            }
        }
        return out;
    }

    private int countDocsContaining(List<Map<String, Integer>> docTf, String term) {
        int c = 0;
        for (Map<String, Integer> tf : docTf) if (tf.containsKey(term)) c++;
        return c;
    }

    /** 中文二字分词 + 英数词元 */
    static List<String> tokenize(String text) {
        List<String> tokens = new ArrayList<>();
        if (text == null) return tokens;
        String clean = text.toLowerCase().replaceAll("<[^>]+>", " ").replaceAll("[^\\u4e00-\\u9fa5a-z0-9]+", " ");
        for (String word : clean.split(" ")) {
            if (word.isEmpty()) continue;
            if (word.matches("[a-z0-9]+")) { tokens.add(word); continue; }
            for (int i = 0; i < word.length() - 1; i++) tokens.add(word.substring(i, i + 2));
        }
        return tokens;
    }
}
