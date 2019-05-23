package com.hj.tj.gohome.config.oss;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author tangj
 * @description
 * @since 2019/5/23 11:49
 */
@ConfigurationProperties(prefix = "oss")
@Data
public class OssProperties {

    /**
     * accessKeyId
     */
    private String accessKeyId;

    /**
     * 访问密钥
     */
    private String accessKeySecret;

    /**
     * endpoint
     */
    private String endpoint;

    /**
     *
     */
    private String bucketName;
}
