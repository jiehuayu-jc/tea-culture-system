package com.utils;

import java.util.concurrent.atomic.AtomicLong;

/**
 * 茶道AI 全局调用指标（供 /ai/status 暴露）：
 * llm_calls 只在真正发起大模型请求时增长；rate_limited 命中的请求不计入 llm_calls。
 */
public final class AiMetrics {

    public static final AtomicLong totalRequests = new AtomicLong();
    public static final AtomicLong rateLimited = new AtomicLong();
    public static final AtomicLong llmCalls = new AtomicLong();

    private AiMetrics() { }
}
