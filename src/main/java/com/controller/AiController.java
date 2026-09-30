package com.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.annotation.IgnoreAuth;
import com.service.TeaAgentService;
import com.service.TeaRagService;
import com.utils.DeepSeekClient;
import com.utils.R;

/**
 * 茶道AI 接口：RAG 问答 / Agent 流式对话 / 知识库重建 / 管理端 NL 查询与 AI 写手
 */
@RestController
@RequestMapping("/ai")
public class AiController {

    @Autowired
    private TeaAgentService agentService;

    @Autowired
    private com.utils.AiRateLimiter rateLimiter;

    @Autowired
    private TeaRagService ragService;

    /** 稠密向量通道（可选）：没有任何实现 Bean 时为 null，检索自动降级为单路 BM25 */
    @Autowired(required = false)
    private com.utils.EmbeddingProvider embeddingProvider;

    @Value("${ai.deepseek.api-key:}")
    private String apiKey;

    @Value("${ai.deepseek.base-url:https://api.deepseek.com}")
    private String baseUrl;

    @Value("${ai.deepseek.model:deepseek-chat}")
    private String model;

    /** Agent 流式对话（SSE）。必须登录：拦截器校验 Token 并写入 session，get_my_orders 依赖 userId */
    @RequestMapping("/chat/stream")
    public SseEmitter chatStream(@RequestBody JSONObject body, HttpServletRequest request,
                                 HttpServletResponse response) throws java.io.IOException {
        String ip = request.getRemoteAddr();
        if (!rateLimiter.tryAcquire(ip)) {
            com.utils.AiMetrics.rateLimited.incrementAndGet();
            response.setStatus(429);
            response.setContentType("application/json; charset=utf-8");
            response.getWriter().print(JSON.toJSONString(
                    R.error(429, "请求过于频繁，请稍后再试（每分钟10次/每天200次）")));
            return null; // 限流命中：不创建 SSE，不发起任何大模型调用
        }
        com.utils.AiMetrics.totalRequests.incrementAndGet();
        String query = body.getString("query");
        if (query == null || query.trim().isEmpty()) {
            response.setStatus(400);
            response.setContentType("application/json; charset=utf-8");
            response.getWriter().print(JSON.toJSONString(R.error(400, "问题不能为空")));
            return null;
        }
        if (query.length() > 500) {
            response.setStatus(400);
            response.setContentType("application/json; charset=utf-8");
            response.getWriter().print(JSON.toJSONString(R.error(400, "问题过长（上限500字）")));
            return null;
        }
        Object uid = request.getSession().getAttribute("userId");
        Long userId = uid instanceof Long ? (Long) uid : uid instanceof Integer ? ((Integer) uid).longValue() : null;
        SseEmitter emitter = new SseEmitter(180000L);
        agentService.streamChat(query, userId, emitter);
        return emitter;
    }

    /** 知识库重建（仅管理员） */
    @RequestMapping("/knowledge/rebuild")
    public R rebuild(HttpServletRequest request) {
        if (!"管理员".equals(request.getSession().getAttribute("role"))) {
            return R.error(403, "仅管理员可重建知识库");
        }
        return R.ok().put("data", ragService.rebuild());
    }

    /** AI 状态自检 */
    @IgnoreAuth
    @RequestMapping("/status")
    public R status() {
        Map<String, Object> r = new HashMap<>();
        r.put("llm_configured", apiKey != null && !apiKey.trim().isEmpty());
        r.put("model", model);
        r.put("knowledge_count", ragService.count());
        // 检索通道状态：dense 为 true 表示「BM25 + 向量」双路 RRF 融合已生效
        Map<String, Object> emb = new HashMap<>();
        boolean denseOn = embeddingProvider != null && embeddingProvider.available();
        emb.put("dense_enabled", denseOn);
        emb.put("provider", embeddingProvider == null ? "none" : embeddingProvider.name());
        emb.put("recall", denseOn ? "hybrid(bm25 + dense, RRF)" : "sparse(bm25)");
        emb.put("rerank", "llm");
        r.put("retrieval", emb);
        r.put("metrics", rateLimiter.snapshot());
        return R.ok().put("data", r);
    }

    /** 管理端：自然语言查数据（NL2Query → 统计数据 → 前端出图） */
    @RequestMapping("/admin/nlq")
    public R nlq(@RequestBody JSONObject body, HttpServletRequest request) {
        if (!"管理员".equals(request.getSession().getAttribute("role"))) {
            return R.error(403, "仅管理员可用");
        }
        String question = body.getString("question");
        // 1) LLM 解析意图（失败走关键词兜底）
        JSONObject intent = null;
        DeepSeekClient client = new DeepSeekClient(baseUrl, apiKey, model);
        if (client.isConfigured()) {
            try {
                JSONArray messages = new JSONArray();
                messages.add(msg("system", "你是数据分析意图解析器。只输出 JSON：{\"metric\":\"sales_by_category\"|\"sales_by_product\"|\"click_by_product\"|\"order_status\","
                        + "\"limit\":数字}。可用的指标仅限这四种。问题：" + question));
                JSONObject msg = client.chatOnce(messages, null);
                intent = JSON.parseObject(extractJson(msg.getString("content")));
            } catch (Exception ignore) { }
        }
        if (intent == null) intent = fallbackIntent(question);

        // 2) 执行统计 SQL
        int limit = intent.getIntValue("limit") > 0 ? Math.min(intent.getIntValue("limit"), 10) : 5;
        String metric = intent.getString("metric");
        List<Map<String, Object>> rows;
        String title;
        switch (metric == null ? "" : metric) {
            case "sales_by_product":
                rows = jdbcTemplate().queryForList("SELECT shangpinmingcheng AS k, SUM(buynumber) AS v FROM orders WHERE status IN ('已支付','已发货','已完成') GROUP BY shangpinmingcheng ORDER BY v DESC LIMIT ?", limit);
                title = "商品销量排行";
                break;
            case "click_by_product":
                rows = jdbcTemplate().queryForList("SELECT shangpinmingcheng AS k, clicknum AS v FROM shangpinxinxi ORDER BY clicknum DESC LIMIT ?", limit);
                title = "商品浏览量排行";
                break;
            case "order_status":
                rows = jdbcTemplate().queryForList("SELECT status AS k, COUNT(*) AS v FROM orders GROUP BY status");
                title = "订单状态分布";
                break;
            case "sales_by_category":
            default:
                rows = jdbcTemplate().queryForList("SELECT s.shangpinfenlei AS k, SUM(o.buynumber) AS v FROM orders o JOIN shangpinxinxi s ON o.goodid = s.id WHERE o.status IN ('已支付','已发货','已完成') GROUP BY s.shangpinfenlei ORDER BY v DESC LIMIT ?", limit);
                title = "分类销量排行";
                break;
        }
        List<String> categories = new ArrayList<>();
        List<Object> values = new ArrayList<>();
        for (Map<String, Object> r : rows) {
            categories.add(String.valueOf(r.get("k")));
            values.add(r.get("v"));
        }
        Map<String, Object> chart = new HashMap<>();
        chart.put("title", title);
        chart.put("categories", categories);
        chart.put("values", values);
        chart.put("intent", metric);
        return R.ok().put("data", chart);
    }

    /** 管理端：AI 写手（商品介绍 / 茶文化文章草稿） */
    @RequestMapping("/admin/writer")
    public R writer(@RequestBody JSONObject body, HttpServletRequest request) {
        if (!"管理员".equals(request.getSession().getAttribute("role"))) {
            return R.error(403, "仅管理员可用");
        }
        String kind = body.getString("kind");
        String name = body.getString("name");
        String keywords = body.getString("keywords");
        DeepSeekClient client = new DeepSeekClient(baseUrl, apiKey, model);
        if (!client.isConfigured()) {
            return R.error(500, "未配置 DeepSeek API Key，AI 写手不可用（请设置环境变量 DEEPSEEK_API_KEY，或复制 config/application.yml.example 为 config/application.yml 并填入 Key）");
        }
        String prompt = "tea".equals(kind)
                ? "你是茶行业文案专家。为商品「" + name + "」写一段电商介绍，要点：" + keywords
                        + "。要求：120字以内，语气高级克制，突出产地、工艺、口感三方面，直接输出纯文本。"
                : "你是茶文化专栏作者。以「" + name + "」为题写一篇茶文化短文，要点：" + keywords
                        + "。要求：300字左右，用<p>段落包裹的HTML，语言雅致有典故，直接输出HTML。";
        try {
            JSONArray messages = new JSONArray();
            messages.add(msg("user", prompt));
            JSONObject msg = client.chatOnce(messages, null);
            return R.ok().put("data", msg.getString("content"));
        } catch (Exception e) {
            return R.error(500, "AI 生成失败：" + e.getMessage());
        }
    }

    private String extractJson(String s) {
        if (s == null) return null;
        int a = s.indexOf('{'), b = s.lastIndexOf('}');
        return (a >= 0 && b > a) ? s.substring(a, b + 1) : null;
    }

    private JSONObject fallbackIntent(String q) {
        JSONObject intent = new JSONObject();
        if (q.contains("浏览") || q.contains("点击")) intent.put("metric", "click_by_product");
        else if (q.contains("状态") || q.contains("退款") || q.contains("未支付")) intent.put("metric", "order_status");
        else if (q.contains("哪个商品") || q.contains("单品") || q.contains("销量排行")) intent.put("metric", "sales_by_product");
        else intent.put("metric", "sales_by_category");
        intent.put("limit", 5);
        return intent;
    }

    @Autowired
    private org.springframework.jdbc.core.JdbcTemplate jdbcTemplate;

    private org.springframework.jdbc.core.JdbcTemplate jdbcTemplate() {
        return jdbcTemplate;
    }

    private Map<String, Object> msg(String role, String content) {
        Map<String, Object> m = new HashMap<>();
        m.put("role", role);
        m.put("content", content);
        return m;
    }
}
