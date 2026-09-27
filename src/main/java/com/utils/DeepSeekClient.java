package com.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 轻量 DeepSeek（OpenAI 兼容协议）客户端：多轮对话、Function Calling、SSE 流式。
 * 仅依赖 JDK + fastjson，兼容 Java 8 / Spring Boot 2.2。
 */
public class DeepSeekClient {

    public interface StreamCallback {
        /** delta: 增量文本；finish: 结束原因(stop/tool_calls) */
        void onEvent(String deltaText, String finishReason, JSONArray toolCalls);
    }

    private final String baseUrl;
    private final String apiKey;
    private final String model;

    public DeepSeekClient(String baseUrl, String apiKey, String model) {
        this.baseUrl = baseUrl == null ? "https://api.deepseek.com" : baseUrl.replaceAll("/$", "");
        this.apiKey = apiKey;
        this.model = model == null ? "deepseek-chat" : model;
    }

    public boolean isConfigured() {
        return apiKey != null && !apiKey.trim().isEmpty();
    }

    /** 非流式对话（Agent 规划轮使用）。返回 choices[0] 的 message 对象（含 content / tool_calls）。 */
    public JSONObject chatOnce(JSONArray messages, JSONArray tools) throws Exception {
        JSONObject body = new JSONObject();
        body.put("model", model);
        body.put("messages", messages);
        if (tools != null && !tools.isEmpty()) {
            body.put("tools", tools);
            body.put("tool_choice", "auto");
        }
        JSONObject resp = post("/chat/completions", body.toJSONString());
        return resp.getJSONArray("choices").getJSONObject(0).getJSONObject("message");
    }

    /** 流式对话（最终回答轮使用），逐段回调增量文本。 */
    public void chatStream(JSONArray messages, StreamCallback cb) throws Exception {
        JSONObject body = new JSONObject();
        body.put("model", model);
        body.put("messages", messages);
        body.put("stream", true);
        HttpURLConnection conn = open("/chat/completions", body.toJSONString());
        try (InputStream is = conn.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            String line;
            StringBuilder toolCallBuf = new StringBuilder();
            String finish = null;
            while ((line = reader.readLine()) != null) {
                if (!line.startsWith("data:")) continue;
                String data = line.substring(5).trim();
                if ("[DONE]".equals(data)) break;
                if (data.isEmpty()) continue;
                try {
                    JSONObject chunk = JSON.parseObject(data);
                    JSONArray choices = chunk.getJSONArray("choices");
                    if (choices == null || choices.isEmpty()) continue;
                    JSONObject delta = choices.getJSONObject(0).getJSONObject("delta");
                    finish = choices.getJSONObject(0).getString("finish_reason");
                    if (delta != null) {
                        String text = delta.getString("content");
                        if (text != null && !text.isEmpty()) cb.onEvent(text, null, null);
                        JSONArray tc = delta.getJSONArray("tool_calls");
                        if (tc != null) toolCallBuf.append(tc.toJSONString());
                    }
                } catch (Exception ignore) {
                    // 单个 chunk 解析失败不影响整体流
                }
            }
            if ("tool_calls".equals(finish) && toolCallBuf.length() > 0) {
                cb.onEvent(null, finish, JSON.parseArray(toolCallBuf.toString()));
            } else {
                cb.onEvent(null, finish == null ? "stop" : finish, null);
            }
        }
    }

    private JSONObject post(String path, String json) throws Exception {
        HttpURLConnection conn = open(path, json);
        int code = conn.getResponseCode();
        InputStream is = code >= 400 ? conn.getErrorStream() : conn.getInputStream();
        StringBuilder sb = new StringBuilder();
        try (BufferedReader r = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            String line;
            while ((line = r.readLine()) != null) sb.append(line);
        }
        if (code >= 400) {
            throw new RuntimeException("DeepSeek HTTP " + code + ": " + sb.substring(0, Math.min(300, sb.length())));
        }
        return JSON.parseObject(sb.toString());
    }

    private HttpURLConnection open(String path, String json) throws Exception {
        HttpURLConnection conn = (HttpURLConnection) new URL(baseUrl + path).openConnection();
        conn.setRequestMethod("POST");
        conn.setConnectTimeout(10000);
        conn.setReadTimeout(180000);
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
        conn.setRequestProperty("Authorization", "Bearer " + apiKey);
        byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
        conn.setFixedLengthStreamingMode(bytes.length);
        try (OutputStream os = conn.getOutputStream()) {
            os.write(bytes);
        }
        return conn;
    }

    /** 便捷构造 user message */
    public static Map<String, Object> userMsg(String content) {
        return message("user", content);
    }

    public static Map<String, Object> message(String role, String content) {
        Map<String, Object> m = new java.util.HashMap<>();
        m.put("role", role);
        m.put("content", content);
        return m;
    }
}
