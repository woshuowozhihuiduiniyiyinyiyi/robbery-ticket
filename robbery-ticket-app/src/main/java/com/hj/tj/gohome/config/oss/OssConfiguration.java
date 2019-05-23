package com.hj.tj.gohome.config.oss;

import com.aliyun.oss.ClientConfiguration;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.common.auth.CredentialsProvider;
import com.aliyun.oss.common.auth.DefaultCredentialProvider;
import com.aliyun.oss.common.comm.Protocol;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * @author tangj
 * @description
 * @since 2019/5/23 11:54
 */
@EnableConfigurationProperties(OssProperties.class)
@Configuration
public class OssConfiguration {

    @Resource
    private OssProperties ossProperties;

    @Bean
    public ClientConfiguration clientConfiguration() {
        ClientConfiguration configuration = new ClientConfiguration();
        configuration.setMaxConnections(200);
        configuration.setSocketTimeout(50000);
        configuration.setConnectionTimeout(50000);
        configuration.setIdleConnectionTime(60000L);
        configuration.setMaxErrorRetry(3);
        configuration.setSupportCname(true);
        configuration.setSLDEnabled(false);
        configuration.setUserAgent("aliyun-sdk-java");
        configuration.setProtocol(Protocol.HTTPS);
        return configuration;
    }

    @Bean
    public CredentialsProvider credentialsProvider() {
        return new DefaultCredentialProvider(ossProperties.getAccessKeyId(), ossProperties.getAccessKeySecret());
    }

    @Bean
    public String endpoint() {
        return ossProperties.getEndpoint();
    }

    @Bean
    public OSSClient managerClient(String endPoint, CredentialsProvider credentialsProvider, ClientConfiguration clientConfiguration) {
        return new OSSClient(endPoint, credentialsProvider, clientConfiguration);
    }
}
