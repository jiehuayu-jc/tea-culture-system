package com.utils;

import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 茶道AI 接口配额：per-IP 滑动窗口限流（每分钟 N 次 / 每天 M 次）+ 调用指标。
 * 限流命中时调用方不得发起任何大模型请求（指标 llm_calls 不增长）。
 */
@Component
public class AiRateLimiter {

    @Value("${ai.rate-limit.minute:10}")
    private int minuteLimit;

    @Value("${ai.rate-limit.day:200}")
    private int dayLimit;

    /** 每分钟滑动窗口 */
    private final ConcurrentHashMap<String, Deque<Long>> minuteWindows = new ConcurrentHashMap<>();
    /** 按天计数：ip -> [dayEpochMillis, count] */
    private final ConcurrentHashMap<String, long[]> dayCounters = new ConcurrentHashMap<>();

    public boolean tryAcquire(String ip) {
        long now = System.currentTimeMillis();
        pruneDay(now);
        long[] day = dayCounters.computeIfAbsent(ip, k -> new long[]{ dayEpoch(now), 0 });
        if (day[0] != dayEpoch(now)) { day[0] = dayEpoch(now); day[1] = 0; }
        if (day[1] >= dayLimit) return false;

        Deque<Long> win = minuteWindows.computeIfAbsent(ip, k -> new ConcurrentLinkedDeque<>());
        synchronized (win) {
            while (!win.isEmpty() && now - win.peekFirst() >= 60_000L) win.pollFirst();
            if (win.size() >= minuteLimit) return false;
            win.addLast(now);
        }
        day[1]++;
        return true;
    }

    public Map<String, Object> snapshot() {
        Map<String, Object> m = new HashMap<>();
        m.put("total_requests", AiMetrics.totalRequests.get());
        m.put("rate_limited", AiMetrics.rateLimited.get());
        m.put("llm_calls", AiMetrics.llmCalls.get());
        m.put("minute_limit", minuteLimit);
        m.put("day_limit", dayLimit);
        return m;
    }

    private static long dayEpoch(long millis) {
        return millis / 86_400_000L;
    }

    private void pruneDay(long now) {
        if (dayCounters.size() < 4096) return;
        Iterator<Map.Entry<String, long[]>> it = dayCounters.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, long[]> e = it.next();
            if (now - e.getValue()[0] * 86_400_000L > 2 * 86_400_000L) it.remove();
        }
    }
}
