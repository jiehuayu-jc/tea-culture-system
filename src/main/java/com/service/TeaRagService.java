package com.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

/**
 * 茶道AI · RAG 知识服务：
 * 知识入库（站内 4 类内容）+ BM25 中文二字分词检索（纯 Java，无外部依赖）。
 */
@Service
public class TeaRagService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

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
        } catch (Exception e) {
            log.error("[茶道AI] 知识库初始化失败：ai_knowledge 表不存在或不可写。"
                    + "请先执行 db/springbootj8kskvkr.sql（文件末尾包含 ai_knowledge 建表语句）后重启服务。", e);
        }
    }

    /** 将站内内容重建进知识库 */
    public Map<String, Object> rebuild() {
        jdbcTemplate.update("DELETE FROM ai_knowledge");
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

    /** BM25 检索，返回 [{title, source_type, source_id, snippet, score}] */
    public List<Map<String, Object>> search(String query, int top) {
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
            Map<String, Object> doc = docs.get(i);
            String content = String.valueOf(doc.get("content"));
            Map<String, Object> hit = new HashMap<>();
            hit.put("title", title);
            hit.put("source_type", doc.get("source_type"));
            hit.put("source_id", doc.get("source_id"));
            hit.put("score", Math.round(score * 100) / 100.0);
            hit.put("snippet", content.length() > 180 ? content.substring(0, 180) + "…" : content);
            if (titled) hit.put("title_hit", true);
            result.add(hit);
        }
        result.sort((a, b2) -> Double.compare((Double) b2.get("score"), (Double) a.get("score")));
        return result.subList(0, Math.min(top, result.size()));
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
