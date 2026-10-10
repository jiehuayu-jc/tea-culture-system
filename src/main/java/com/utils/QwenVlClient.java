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
 * 千问 VL 视觉模型客户端（阿里云百炼 OpenAI 兼容接口）。
 *
 * <p>用途：多模态输入。用户上传茶叶 / 茶具 / 茶席照片后，先由视觉模型转成文字描述，
 * 再把描述作为上下文交给 Agent，走原有的检索与工具调用链路 —— 这样多模态与
 * RAG、工具编排是串联的，而不是孤立的一次识图。
 *
 * <p>凭据默认复用 {@code ai.embedding.*}（百炼同一把 key 同时开通了 text-embedding
 * 与 qwen-vl 系列），也可用 {@code ai.vision.*} 单独指定。
 */
@Component
public class QwenVlClient {

    private static final Logger log = LoggerFactory.getLogger(QwenVlClient.class);

    @Value("${ai.vision.api-key:${ai.embedding.api-key:}}")
    private String apiKey;

    @Value("${ai.vision.base-url:${ai.embedding.base-url:https://dashscope.aliyuncs.com/compatible-mode/v1}}")
    private String baseUrl;

    @Value("${ai.vision.model:qwen3-vl-plus}")
    private String model;

    @Value("${ai.vision.prompt:你是茶领域视觉专家。请用简体中文简要描述这张图片：若为茶叶，判断可能的茶类、外形与品质特征；若为茶具，说明器型与材质；若为茶席或场景，概述画面。控制在120字内，直接输出描述，不要客套话。}")
    private String prompt;

    public boolean isConfigured() {
        return apiKey != null && !apiKey.trim().isEmpty();
    }

    public String name() {
        return "dashscope:" + model;
    }

    /**
     * 识别单张图片并返回文字描述（原有图片上传入口）。
     *
     * @param imageDataUrl 形如 {@code data:image/jpeg;base64,xxxx} 的 Data URL
     * @return 描述文本；未配置或调用失败返回 null（调用方需降级处理）
     */
    public String describe(String imageDataUrl) {
        return describeFrames(java.util.Collections.singletonList(imageDataUrl), null);
    }

    /**
     * 视频抽帧理解：多帧 + 可选用户问题 → 综合描述。
     *
     * <p>前端对上传视频按时间轴均匀抽帧（默认 5 帧以内）后传 Data URL 数组，
     * 视觉模型把多帧视为同一事件的连续画面作综合判断（如茶艺手法、冲泡过程），
     * 描述文本随后并入 Agent 对话上下文，走与图片相同的决策链路。
     *
     * @param frameDataUrls 视频帧 Data URL 列表（1~6 帧）
     * @param question      用户想问的问题，可为 null（仅描述）
     * @return 描述文本；未配置或调用失败返回 null
     */
    public String describeFrames(java.util.List<String> frameDataUrls, String question) {
        if (!isConfigured() || frameDataUrls == null || frameDataUrls.isEmpty()) return null;
        try {
            String q = (question == null || question.trim().isEmpty())
                    ? "" : "用户想了解的问题：" + question.trim() + "。";
            String p = "你是茶领域视觉专家。以下是用户上传视频按时间顺序抽取的连续帧"
                    + "（同一画面的不同时刻）。请综合判断视频内容：若为泡茶/茶艺演示，"
                    + "说明流程步骤与手法要点；若展示茶叶/茶具，判断茶类、器型与品质特征。"
                    + q + "用简体中文输出180字内的连贯描述，直接输出内容，不要客套话。";

            JSONObject txtPart = new JSONObject();
            txtPart.put("type", "text");
            txtPart.put("text", p);
            JSONArray content = new JSONArray();
            content.add(txtPart);
            int n = Math.min(frameDataUrls.size(), 6);
            for (int i = 0; i < n; i++) {
                JSONObject imageUrl = new JSONObject();
                imageUrl.put("url", frameDataUrls.get(i));
                JSONObject imgPart = new JSONObject();
                imgPart.put("type", "image_url");
                imgPart.put("image_url", imageUrl);
                content.add(imgPart);
            }
            return postVision(content);
        } catch (Exception e) {
            log.warn("[茶道AI] 视频帧理解异常：{}", e.getMessage());
            return null;
        }
    }

    /** 多模态 chat/completions 单轮调用：content 由调用方组装（文本 + N 张图） */
    private String postVision(JSONArray content) throws Exception {
        HttpURLConnection conn = null;
        try {
            JSONObject msg = new JSONObject();
            msg.put("role", "user");
            msg.put("content", content);
            JSONArray messages = new JSONArray();
            messages.add(msg);

            JSONObject body = new JSONObject();
            body.put("model", model);
            body.put("messages", messages);
            body.put("max_tokens", 400);

            URL url = new URL(baseUrl.replaceAll("/+$", "") + "/chat/completions");
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(60000);
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            conn.setRequestProperty("Authorization", "Bearer " + apiKey);
            try (OutputStream os = conn.getOutputStream()) {
                os.write(body.toJSONString().getBytes(StandardCharsets.UTF_8));
            }

            int code = conn.getResponseCode();
            InputStream is = (code >= 200 && code < 300) ? conn.getInputStream() : conn.getErrorStream();
            String resp = readAll(is);
            if (code < 200 || code >= 300) {
                log.warn("[茶道AI] 视觉模型调用失败：HTTP {} {}", code, abbreviate(resp));
                return null;
            }
            JSONObject obj = JSON.parseObject(resp);
            JSONArray choices = obj == null ? null : obj.getJSONArray("choices");
            if (choices == null || choices.isEmpty()) return null;
            JSONObject m = choices.getJSONObject(0).getJSONObject("message");
            String text = m == null ? null : m.getString("content");
            return text == null ? null : text.trim();
        } catch (Exception e) {
            log.warn("[茶道AI] 视觉模型异常：{}", e.getMessage());
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
            while ((line = br.readLine()) != null) sb.append(line).append('\n');
        }
        return sb.toString();
    }

    private String abbreviate(String s) {
        if (s == null) return "";
        return s.length() > 160 ? s.substring(0, 160) + "…" : s;
    }
}
