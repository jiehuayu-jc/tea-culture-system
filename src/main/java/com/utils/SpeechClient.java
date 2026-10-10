package com.utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
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
 * 语音通道（阿里云百炼，OpenAI 兼容接口）：语音识别 ASR + 语音合成 TTS。
 *
 * <p>与 {@link QwenVlClient} 同构：凭据默认复用 {@code ai.embedding.*}（百炼同一把
 * key 同时开通了 text-embedding / qwen-vl / qwen3-asr / qwen3-omni 系列），也可用
 * {@code ai.speech.*} 单独指定。未配置密钥时 {@link #isConfigured()} 返回 false，
 * 上层接口返回提示，主流程（文本对话）不受影响——多模态均为可选增强。
 *
 * <p>两个方向：
 * <ul>
 *   <li>ASR：qwen3-asr-flash，前端录音（WAV Data URL）→ 文本，识别结果进入
 *       原有 RAG / Agent 对话链路；</li>
 *   <li>TTS：qwen3-omni-flash 流式输出音频分片（裸 PCM 24kHz 16bit 单声道），
 *       服务端拼接后补 WAV 头返回，前端直接播放。</li>
 * </ul>
 */
@Component
public class SpeechClient {

    private static final Logger log = LoggerFactory.getLogger(SpeechClient.class);

    /** omni 系列音频输出采样率（文档值 24000Hz），可通过配置跟随服务端调整 */
    private static final int SAMPLE_RATE = 24000;

    @Value("${ai.speech.api-key:${ai.embedding.api-key:}}")
    private String apiKey;

    @Value("${ai.speech.base-url:${ai.embedding.base-url:https://dashscope.aliyuncs.com/compatible-mode/v1}}")
    private String baseUrl;

    @Value("${ai.speech.asr-model:qwen3-asr-flash}")
    private String asrModel;

    @Value("${ai.speech.tts-model:qwen3-omni-flash}")
    private String ttsModel;

    @Value("${ai.speech.voice:Cherry}")
    private String voice;

    public boolean isConfigured() {
        return apiKey != null && !apiKey.trim().isEmpty();
    }

    public String name() {
        return "dashscope:" + asrModel + "/" + ttsModel;
    }

    /**
     * 语音转文字。
     *
     * @param audioDataUrl 形如 {@code data:audio/wav;base64,xxx} 的录音 Data URL（≤10MB）
     * @return 识别文本；未配置或调用失败返回 null（调用方需降级处理）
     */
    public String transcribe(String audioDataUrl) {
        if (!isConfigured() || audioDataUrl == null || audioDataUrl.trim().isEmpty()) return null;

        JSONObject audioPart = new JSONObject();
        audioPart.put("type", "input_audio");
        JSONObject inputAudio = new JSONObject();
        inputAudio.put("data", audioDataUrl);
        audioPart.put("input_audio", inputAudio);

        JSONArray content = new JSONArray();
        content.add(audioPart);
        return chatOnce(content, null);
    }

    /**
     * 语音合成：把一段回答文本合成为 WAV 音频。
     *
     * @param text 待合成文本（超长自动截断到 300 字）
     * @return WAV 字节（24000Hz/16bit/单声道）；未配置或调用失败返回 null
     */
    public byte[] synthesize(String text) {
        if (!isConfigured() || text == null || text.trim().isEmpty()) return null;
        String clean = stripMarkdown(text);
        if (clean.length() > 300) clean = clean.substring(0, 300);

        JSONObject txtPart = new JSONObject();
        txtPart.put("type", "text");
        txtPart.put("text", clean);
        JSONArray content = new JSONArray();
        content.add(txtPart);

        HttpURLConnection conn = null;
        try {
            JSONObject body = new JSONObject();
            body.put("model", ttsModel);
            body.put("stream", true);
            // omni 系列语音输出：modalities 声明音频模态，audio 指定音色
            JSONArray modalities = new JSONArray();
            modalities.add("text");
            modalities.add("audio");
            body.put("modalities", modalities);
            JSONObject audio = new JSONObject();
            audio.put("voice", voice);
            audio.put("format", "wav");
            body.put("audio", audio);
            body.put("messages", messagesOf(content));

            URL url = new URL(baseUrl.replaceAll("/+$", "") + "/chat/completions");
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(90000);
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            conn.setRequestProperty("Authorization", "Bearer " + apiKey);
            conn.setRequestProperty("Accept", "text/event-stream");
            try (OutputStream os = conn.getOutputStream()) {
                os.write(body.toJSONString().getBytes(StandardCharsets.UTF_8));
            }

            int code = conn.getResponseCode();
            InputStream is = (code >= 200 && code < 300) ? conn.getInputStream() : conn.getErrorStream();
            String resp = readAll(is);
            if (code < 200 || code >= 300) {
                log.warn("[茶道AI] 语音合成失败：HTTP {} {}", code, abbreviate(resp));
                return null;
            }
            // SSE 流：data: {"choices":[{"delta":{"audio":{"data":"<base64 片段>"}}}]}
            ByteArrayOutputStream pcm = new ByteArrayOutputStream(64 * 1024);
            for (String line : resp.split("\n")) {
                line = line.trim();
                if (!line.startsWith("data:") || line.contains("[DONE]")) continue;
                try {
                    JSONObject obj = JSON.parseObject(line.substring(5).trim());
                    JSONArray choices = obj.getJSONArray("choices");
                    if (choices == null || choices.isEmpty()) continue;
                    JSONObject delta = choices.getJSONObject(0).getJSONObject("delta");
                    if (delta == null) continue;
                    JSONObject a = delta.getJSONObject("audio");
                    if (a == null) continue;
                    String b64 = a.getString("data");
                    if (b64 != null && !b64.isEmpty()) pcm.write(java.util.Base64.getDecoder().decode(b64));
                } catch (Exception ignore) {
                    // 单片解析失败不影响整体音频
                }
            }
            byte[] raw = pcm.toByteArray();
            if (raw.length == 0) {
                log.warn("[茶道AI] 语音合成未返回音频数据：响应 {} 字符，头 240：{}", resp.length(), abbreviate(resp));
                return null;
            }
            return wrapWav(raw, SAMPLE_RATE, 1, 16);
        } catch (Exception e) {
            log.warn("[茶道AI] 语音合成异常：{}", e.getMessage());
            return null;
        } finally {
            if (conn != null) conn.disconnect();
        }
    }

    /** 复用 chat/completions 的单轮调用（ASR / 未来语音多轮共用） */
    private String chatOnce(JSONArray content, String systemPrompt) {
        HttpURLConnection conn = null;
        try {
            JSONArray messages = new JSONArray();
            if (systemPrompt != null && !systemPrompt.isEmpty()) {
                JSONObject sys = new JSONObject();
                sys.put("role", "system");
                sys.put("content", systemPrompt);
                messages.add(sys);
            }
            JSONObject msg = new JSONObject();
            msg.put("role", "user");
            msg.put("content", content);
            messages.add(msg);

            JSONObject body = new JSONObject();
            body.put("model", asrModel);
            body.put("stream", false);
            body.put("messages", messages);

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
                log.warn("[茶道AI] 语音识别失败：HTTP {} {}", code, abbreviate(resp));
                return null;
            }
            JSONObject obj = JSON.parseObject(resp);
            JSONArray choices = obj == null ? null : obj.getJSONArray("choices");
            if (choices == null || choices.isEmpty()) return null;
            JSONObject m = choices.getJSONObject(0).getJSONObject("message");
            String text = m == null ? null : m.getString("content");
            return text == null ? null : text.trim();
        } catch (Exception e) {
            log.warn("[茶道AI] 语音识别异常：{}", e.getMessage());
            return null;
        } finally {
            if (conn != null) conn.disconnect();
        }
    }

    /** 给裸 PCM 数据补 44 字节 WAV 头（RIFF），浏览器 audio 可直接播放 */
    private static byte[] wrapWav(byte[] pcm, int sampleRate, int channels, int bitsPerSample) {
        int byteRate = sampleRate * channels * bitsPerSample / 8;
        int align = channels * bitsPerSample / 8;
        int dataLen = pcm.length;
        java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream(44 + dataLen);
        java.io.DataOutputStream d = new java.io.DataOutputStream(out);
        try {
            d.writeBytes("RIFF");
            d.writeInt(Integer.reverseBytes(36 + dataLen));
            d.writeBytes("WAVE");
            d.writeBytes("fmt ");
            d.writeInt(Integer.reverseBytes(16));
            d.writeShort(Short.reverseBytes((short) 1));
            d.writeShort(Short.reverseBytes((short) channels));
            d.writeInt(Integer.reverseBytes(sampleRate));
            d.writeInt(Integer.reverseBytes(byteRate));
            d.writeShort(Short.reverseBytes((short) align));
            d.writeShort(Short.reverseBytes((short) bitsPerSample));
            d.writeBytes("data");
            d.writeInt(Integer.reverseBytes(dataLen));
            d.write(pcm);
        } catch (java.io.IOException e) {
            return null;
        }
        return out.toByteArray();
    }

    /** 朗读前剥离常见 Markdown 标记，避免读出符号 */
    private static String stripMarkdown(String s) {
        return s.replaceAll("```[a-zA-Z]*", " ").replaceAll("[*_#`>\\[\\]]", " ")
                .replaceAll("\\s+", " ").trim();
    }

    private static JSONArray messagesOf(JSONArray content) {
        JSONObject m = new JSONObject();
        m.put("role", "user");
        m.put("content", content);
        JSONArray arr = new JSONArray();
        arr.add(m);
        return arr;
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
