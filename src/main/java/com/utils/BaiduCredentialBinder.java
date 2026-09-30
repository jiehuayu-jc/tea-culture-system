package com.utils;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 百度 / 千帆凭据绑定器。
 *
 * <p>{@link BaiduUtil} 是静态工具类，无法直接使用 @Value 注入。本组件在容器启动后
 * 把外部配置写入其静态字段，使源码与版本库中不再出现任何密钥。
 *
 * <p>配置来源（任选其一，环境变量优先级更高）：
 * <pre>
 * baidu:
 *   app-id: ""        # 或环境变量 BAIDU_APP_ID
 *   api-key: ""       # 或环境变量 BAIDU_API_KEY
 *   secret-key: ""    # 或环境变量 BAIDU_SECRET_KEY
 * qianfan:
 *   access-key: ""    # 或环境变量 QIANFAN_ACCESS_KEY
 *   secret-key: ""    # 或环境变量 QIANFAN_SECRET_KEY
 * </pre>
 */
@Component
public class BaiduCredentialBinder {

    private static final Logger log = LoggerFactory.getLogger(BaiduCredentialBinder.class);

    @Value("${baidu.app-id:${BAIDU_APP_ID:}}")
    private String appId;

    @Value("${baidu.api-key:${BAIDU_API_KEY:}}")
    private String apiKey;

    @Value("${baidu.secret-key:${BAIDU_SECRET_KEY:}}")
    private String secretKey;

    @Value("${qianfan.access-key:${QIANFAN_ACCESS_KEY:}}")
    private String accessKey;

    @Value("${qianfan.secret-key:${QIANFAN_SECRET_KEY:}}")
    private String accessSecretKey;

    @PostConstruct
    public void bind() {
        BaiduUtil.initCredentials(appId, apiKey, secretKey, accessKey, accessSecretKey);
        if (BaiduUtil.credentialsConfigured()) {
            log.info("[凭据] 百度 AI 开放平台凭据已从外部配置注入");
        } else {
            log.info("[凭据] 未配置百度 AI 开放平台凭据，OCR / 图像 / 语音 / 千帆等能力不可用；"
                    + "如需启用请在 config/application.yml 填写 baidu.* 与 qianfan.* 配置项");
        }
    }
}
