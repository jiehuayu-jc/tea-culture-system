package com.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.utils.DeepSeekClient;

/**
 * 茶道AI · Agent 服务：
 * ReAct 工具调用循环（最多 4 轮）→ 最终回答 SSE 流式输出。
 * 工具：查商品推荐 / 检索知识库 / 泡茶指南 / 查我的订单 / 推荐讲座。
 * DeepSeek 不可用时自动降级为"离线演示模式"。
 */
@Service
public class TeaAgentService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private TeaRagService ragService;

    @Value("${ai.deepseek.api-key:}")
    private String apiKey;

    @Value("${ai.deepseek.base-url:https://api.deepseek.com}")
    private String baseUrl;

    @Value("${ai.deepseek.model:deepseek-chat}")
    private String model;

    private static final String SYSTEM_PROMPT =
            "你是「茶道AI」，茶文化管理系统中的智能茶顾问。你可以调用工具查询站内商品、知识库、订单和讲座。" +
            "回答规则：1) 涉及推荐商品时必须调用 recommend_teas 工具；2) 涉及茶知识时优先调用 search_knowledge 检索站内内容并注明来源；" +
            "3) 用户问自己的订单时调用 get_my_orders；4) 回答使用简洁中文，适当分点，不要编造站内不存在的商品。";

    /** Agent 工具定义（OpenAI function calling 格式） */
    private JSONArray toolSchemas() {
        JSONArray tools = new JSONArray();
        tools.add(tool("recommend_teas", "按预算/分类/关键词推荐站内在售茶叶商品",
                "{\"type\":\"object\",\"properties\":{\"budget_max\":{\"type\":\"number\",\"description\":\"预算上限(元)\"},\"category\":{\"type\":\"string\",\"description\":\"分类:绿茶/红茶/乌龙茶/白茶/黄茶/黑茶/茶具/茶点\"},\"keyword\":{\"type\":\"string\",\"description\":\"名称关键词\"}},\"required\":[]}"));
        tools.add(tool("search_knowledge", "检索茶文化知识库，返回站内文章片段",
                "{\"type\":\"object\",\"properties\":{\"query\":{\"type\":\"string\",\"description\":\"知识问题\"}},\"required\":[\"query\"]}"));
        tools.add(tool("get_brewing_guide", "获取某类茶的冲泡参数建议",
                "{\"type\":\"object\",\"properties\":{\"tea_name\":{\"type\":\"string\",\"description\":\"茶名或茶类\"}},\"required\":[\"tea_name\"]}"));
        tools.add(tool("get_my_orders", "查询当前登录用户的最近订单",
                "{\"type\":\"object\",\"properties\":{}}"));
        tools.add(tool("recommend_lectures", "推荐站内线上茶艺讲座",
                "{\"type\":\"object\",\"properties\":{\"topic\":{\"type\":\"string\",\"description\":\"感兴趣的主题(可空)\"}},\"required\":[]}"));
        return tools;
    }

    private Map<String, Object> tool(String name, String desc, String params) {
        Map<String, Object> t = new HashMap<>();
        t.put("type", "function");
        Map<String, Object> fn = new HashMap<>();
        fn.put("name", name);
        fn.put("description", desc);
        fn.put("parameters", JSON.parseObject(params));
        t.put("function", fn);
        return t;
    }

    /** SSE 主流程 */
    public void streamChat(String query, Long userId, SseEmitter emitter) {
        DeepSeekClient client = new DeepSeekClient(baseUrl, apiKey, model);
        try {
            JSONArray messages = new JSONArray();
            messages.add(msg("system", SYSTEM_PROMPT));
            messages.add(msg("user", query));

            List<Map<String, Object>> sources = new ArrayList<>();
            List<Map<String, Object>> cards = new ArrayList<>();
            boolean offline = false;

            // ---- Agent ReAct 循环（非流式，最多 4 轮）----
            JSONArray toolCalls = null;
            int round = 0;
            try {
                while (round < 4) {
                    JSONObject message = client.chatOnce(messages, toolSchemas());
                    messages.add(message); // assistant 消息需完整回传（含 tool_calls）
                    JSONArray calls = message.getJSONArray("tool_calls");
                    if (calls == null || calls.isEmpty()) break;
                    for (int i = 0; i < calls.size(); i++) {
                        JSONObject call = calls.getJSONObject(i);
                        String name = call.getJSONObject("function").getString("name");
                        String argsStr = call.getJSONObject("function").getString("arguments");
                        JSONObject args;
                        try { args = JSON.parseObject(argsStr == null ? "{}" : argsStr); }
                        catch (Exception e) { args = new JSONObject(); }
                        send(emitter, "tool_call", name + " " + argsStr);
                        JSONObject result = executeTool(name, args, userId, sources, cards);
                        send(emitter, "tool_result", result.toJSONString());
                        Map<String, Object> toolMsg = new HashMap<>();
                        toolMsg.put("role", "tool");
                        toolMsg.put("tool_call_id", call.getString("id"));
                        toolMsg.put("content", result.toJSONString());
                        messages.add(msg("tool", JSON.toJSONString(toolMsg)));
                    }
                    round++;
                }
            } catch (Exception e) {
                offline = !client.isConfigured() || isNetworkError(e);
                if (client.isConfigured() && !isNetworkError(e)) throw e;
            }

            // ---- 降级：离线演示模式 ----
            if (offline) {
                send(emitter, "mode", "offline");
                offlineAnswer(query, emitter, sources, cards);
                return;
            }

            // ---- 最终回答：SSE 流式 ----
            final List<Map<String, Object>> fSources = sources, fCards = cards;
            client.chatStream(messages, (delta, finish, tc) -> {
                if (delta != null) send(emitter, "delta", delta);
                if ("stop".equals(finish)) {
                    send(emitter, "sources", JSON.toJSONString(fSources));
                    send(emitter, "cards", JSON.toJSONString(fCards));
                    send(emitter, "done", "ok");
                }
            });
        } catch (Exception e) {
            try {
                send(emitter, "error", String.valueOf(e.getMessage()));
                send(emitter, "done", "error");
            } catch (Exception ignore) { }
        }
    }

    private boolean isNetworkError(Throwable e) {
        String m = String.valueOf(e.getMessage()).toLowerCase();
        return m.contains("connect") || m.contains("timeout") || m.contains("http") || m.contains("network");
    }

    /** 工具执行器 */
    private JSONObject executeTool(String name, JSONObject args, Long userId,
                                   List<Map<String, Object>> sources, List<Map<String, Object>> cards) {
        JSONObject out = new JSONObject();
        switch (name) {
            case "recommend_teas": {
                StringBuilder where = new StringBuilder(" WHERE onshelves = 1 ");
                List<Object> params = new ArrayList<>();
                if (args.getString("category") != null && !args.getString("category").isEmpty()) {
                    where.append(" AND shangpinfenlei = ? "); params.add(args.getString("category"));
                }
                if (args.getString("keyword") != null && !args.getString("keyword").isEmpty()) {
                    where.append(" AND (shangpinmingcheng LIKE ? OR shangpinjieshao LIKE ?) ");
                    params.add("%" + args.getString("keyword") + "%"); params.add("%" + args.getString("keyword") + "%");
                }
                if (args.get("budget_max") != null) {
                    where.append(" AND price <= ? "); params.add(args.getDouble("budget_max"));
                }
                List<Map<String, Object>> rows = jdbcTemplate.queryForList(
                        "SELECT id, shangpinmingcheng, shangpinfenlei, guige, price, clicknum FROM shangpinxinxi "
                                + where + " ORDER BY clicknum DESC LIMIT 4", params.toArray());
                JSONArray arr = new JSONArray();
                for (Map<String, Object> r : rows) {
                    JSONObject card = new JSONObject();
                    card.put("id", r.get("id"));
                    card.put("name", r.get("shangpinmingcheng"));
                    card.put("category", r.get("shangpinfenlei"));
                    card.put("price", r.get("price"));
                    card.put("guige", r.get("guige"));
                    arr.add(card);
                    JSONObject cardCopy = new JSONObject();
                    cardCopy.putAll(card); // 独立副本，避免 fastjson 循环引用 $ref
                    cards.add(cardCopy);
                }
                out.put("recommendations", arr);
                out.put("count", arr.size());
                break;
            }
            case "search_knowledge": {
                List<Map<String, Object>> hits = ragService.search(args.getString("query"), 3);
                out.put("hits", JSON.parseArray(JSON.toJSONString(hits)));
                for (Map<String, Object> h : hits) {
                    Map<String, Object> s = new HashMap<>();
                    s.put("title", h.get("title"));
                    s.put("source_type", h.get("source_type"));
                    s.put("source_id", h.get("source_id"));
                    sources.add(s);
                }
                break;
            }
            case "get_brewing_guide": {
                String tea = args.getString("tea_name") == null ? "" : args.getString("tea_name");
                List<Map<String, Object>> hits = ragService.search(tea + " 冲泡 水温", 2);
                if (!hits.isEmpty()) {
                    out.put("guide_source", hits.get(0).get("title"));
                    out.put("guide", hits.get(0).get("snippet"));
                    Map<String, Object> s = new HashMap<>();
                    s.put("title", hits.get(0).get("title"));
                    s.put("source_id", hits.get(0).get("source_id"));
                    sources.add(s);
                } else {
                    out.put("guide", "通用原则：绿茶80-85℃快出汤，乌龙茶沸水工夫泡，红茶90℃温润，白茶老茶可煮饮。");
                }
                break;
            }
            case "get_my_orders": {
                if (userId == null) { out.put("error", "未登录，无法查询订单"); break; }
                List<Map<String, Object>> rows = jdbcTemplate.queryForList(
                        "SELECT orderid, goodname, total, status, addtime FROM orders WHERE userid = ? ORDER BY addtime DESC LIMIT 5", userId);
                out.put("orders", JSON.parseArray(JSON.toJSONString(rows)));
                break;
            }
            case "recommend_lectures": {
                String topic = args.getString("topic");
                List<Map<String, Object>> rows = topic == null || topic.isEmpty()
                        ? jdbcTemplate.queryForList("SELECT id, zixunmingcheng, zixunfeiyong, kaifangshijian FROM xinlizixun ORDER BY clicknum DESC LIMIT 4")
                        : jdbcTemplate.queryForList("SELECT id, zixunmingcheng, zixunfeiyong, kaifangshijian FROM xinlizixun WHERE zixunmingcheng LIKE ? OR zixunfenlei LIKE ? LIMIT 4",
                                "%" + topic + "%", "%" + topic + "%");
                JSONArray arr = new JSONArray();
                for (Map<String, Object> r : rows) {
                    JSONObject card = new JSONObject();
                    card.put("id", r.get("id"));
                    card.put("name", r.get("zixunmingcheng"));
                    card.put("fee", r.get("zixunfeiyong"));
                    card.put("time", r.get("kaifangshijian"));
                    arr.add(card);
                    JSONObject cardCopy = new JSONObject();
                    cardCopy.putAll(card); // 独立副本，避免 fastjson 循环引用 $ref
                    cards.add(cardCopy);
                }
                out.put("lectures", arr);
                break;
            }
            default:
                out.put("error", "unknown tool: " + name);
        }
        return out;
    }

    /** 离线演示模式：BM25 + 固定推荐，保证答辩现场可用 */
    private void offlineAnswer(String query, SseEmitter emitter,
                               List<Map<String, Object>> sources, List<Map<String, Object>> cards) {
        StringBuilder sb = new StringBuilder();
        sb.append("【离线演示模式】当前未配置大模型网络，以下为本地知识库检索结果：\n\n");
        List<Map<String, Object>> hits = ragService.search(query, 3);
        if (hits.isEmpty()) {
            sb.append("知识库中暂未检索到相关内容。你可以问我：如何泡龙井、送长辈什么茶、有哪些讲座。\n");
        } else {
            for (Map<String, Object> h : hits) {
                sb.append("· 《").append(h.get("title")).append("》：").append(h.get("snippet")).append("\n");
                Map<String, Object> s = new HashMap<>();
                s.put("title", h.get("title"));
                s.put("source_type", h.get("source_type"));
                s.put("source_id", h.get("source_id"));
                sources.add(s);
            }
        }
        if (query.contains("推荐") || query.contains("送") || query.contains("买")) {
            sb.append("\n按点击量为你挑选了站内人气茶品，见下方商品卡片，可直接加购：\n");
            executeTool("recommend_teas", new JSONObject(), null, sources, cards);
        }
        String text = sb.toString();
        for (int i = 0; i < text.length(); i += 3) {
            send(emitter, "delta", text.substring(i, Math.min(i + 3, text.length())));
            try { Thread.sleep(18); } catch (InterruptedException ignore) { }
        }
        send(emitter, "sources", JSON.toJSONString(sources));
        send(emitter, "cards", JSON.toJSONString(cards));
        send(emitter, "done", "ok");
    }

    private void send(SseEmitter emitter, String type, Object data) {
        try {
            JSONObject evt = new JSONObject();
            evt.put("type", type);
            evt.put("data", data);
            emitter.send(SseEmitter.event().data(evt.toJSONString()));
        } catch (Exception ignore) { }
    }

    private Map<String, Object> msg(String role, String content) {
        Map<String, Object> m = new HashMap<>();
        m.put("role", role);
        m.put("content", content);
        return m;
    }
}
