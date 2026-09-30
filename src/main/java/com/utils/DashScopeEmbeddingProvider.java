package com.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 稠密向量通道 · 阿里云百炼（DashScope）OpenAI 兼容接口实现。
 *
 * <p>配置项（不写死在代码里，走环境变量或 config/application.yml）：
 * <pre>
 * ai:
 *   embedding:
 *     api-key: "${DASHSCOPE_API_KEY:}"          # 未配置时 available() 返回 false，检索自动降级为单路 BM25
 *     base-url: "https://dashscope.aliyuncs.com/compatible-mode/v1"
 *     model: "text-embedding-v3"
 * </pre>
 *
 * <p>只要这一段配置填好，TeaRagService 的 recall() 就会自动叠加稠密召回并与 BM25
 * 做 RRF 融合，无需改动检索逻辑。
 */
@Component
public class DashScopeEmbeddingProvider implements EmbeddingProvider {

    private static final Logger log = LoggerFactory.getLogger(DashScopeEmbeddingProvider.class);

    @Value("${ai.embedding.api-key:}")
    private String apiKey;

    @Value("${ai.embedding.base-url:https://dashscope.aliyuncs.com/compatible-mode/v1}")
    private String baseUrl;

    @Value("${ai.embedding.model:text-embedding-v3}")
    private String model;

    /** 最近一次失败原因，供 /ai/status 展示；成功后清空 */
    private volatile String lastError;

    /** 熔断截止时间戳：连续调用失败后，此时间之前不再尝试，避免每次检索都卡在失败请求上 */
    private volatile long disabledUntil = 0L;

    /** 熔断时长：5 分钟 */
    private static final long COOLDOWN_MS = 5 * 60 * 1000L;

    @Override
    public boolean available() {
        return apiKey != null && !apiKey.trim().isEmpty() && System.currentTimeMillis() >= disabledUntil;
    }

    /** 供状态自检：key 已配置但当前处于熔断中时返回 true */
    public boolean configured() {
        return apiKey != null && !apiKey.trim().isEmpty();
    }

    public boolean circuitOpen() {
        return System.currentTimeMillis() < disabledUntil;
    }

    private void trip(String reason) {
        lastError = reason;
        disabledUntil = System.currentTimeMillis() + COOLDOWN_MS;
        log.warn("[茶道AI] 稠密向量通道熔断 {} 分钟：{}", COOLDOWN_MS / 60000, reason);
    }

    @Override
    public String name() {
        return "dashscope:" + model;
    }

    public String lastError() {
        return lastError;
    }

    @Override
    public float[] embed(String text) {
        if (!available() || text == null || text.trim().isEmpty()) return null;
        HttpURLConnection conn = null;
        try {
            JSONObject body = new JSONObject();
            body.put("model", model);
            body.put("input", text.length() > 2000 ? text.substring(0, 2000) : text);

            URL url = new URL(baseUrl.replaceAll("/+$", "") + "/embeddings");
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(15000);
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            conn.setRequestProperty("Authorization", "Bearer " + apiKey);
            try (OutputStream os = conn.getOutputStream()) {
                os.write(body.toJSONString().getBytes(StandardCharsets.UTF_8));
            }
            int code = conn.getResponseCode();
            InputStream is = code >= 200 && code < 300 ? conn.getInputStream() : conn.getErrorStream();
            String resp = readAll(is);
            if (code < 200 || code >= 300) {
                trip("HTTP " + code + " " + abbreviate(resp));
                return null;
            }
            JSONObject obj = JSON.parseObject(resp);
            JSONArray data = obj == null ? null : obj.getJSONArray("data");
            if (data == null || data.isEmpty()) {
                trip("响应无 data 字段");
                return null;
            }
            JSONArray vec = data.getJSONObject(0).getJSONArray("embedding");
            if (vec == null || vec.isEmpty()) {
                trip("响应无 embedding 字段");
                return null;
            }
            float[] out = new float[vec.size()];
            for (int i = 0; i < vec.size(); i++) out[i] = vec.getFloatValue(i);
            lastError = null;
            return out;
        } catch (Exception e) {
            trip(e.getClass().getSimpleName() + ": " + e.getMessage());
            return null;
        } finally {
            if (conn != null) conn.disconnect();
        }
    }

    private String readAll(InputStream is) throws Exception {
        if (is == null) return "";
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) sb.append(line);
        }
        return sb.toString();
    }

    private String abbreviate(String s) {
        if (s == null) return "";
        return s.length() > 160 ? s.substring(0, 160) + "…" : s;
    }
}
