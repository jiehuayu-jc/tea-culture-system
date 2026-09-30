package com.utils;

/**
 * 稠密向量嵌入通道（可插拔）。
 *
 * <p>设计意图：RAG 检索层支持「稀疏 + 稠密」双路召回。
 * 稀疏路为内置 BM25（纯 Java，永远可用）；稠密路依赖外部 Embedding 服务，
 * 当没有任何实现 Bean 时，TeaRagService 自动降级为单路 BM25，功能不中断。
 *
 * <p>接入方式：新增一个 @Component 实现本接口并配置好密钥即可，
 * 无需改动 TeaRagService 的融合逻辑（RRF）。
 */
public interface EmbeddingProvider {

    /** 通道是否可用（未配置密钥、额度耗尽等都应返回 false，触发降级） */
    boolean available();

    /**
     * 将文本编码为向量。
     *
     * @return 归一化后的向量；不可用时返回 null，调用方必须能容忍 null
     */
    float[] embed(String text);

    /** 通道名称，用于状态自检与日志 */
    String name();
}
