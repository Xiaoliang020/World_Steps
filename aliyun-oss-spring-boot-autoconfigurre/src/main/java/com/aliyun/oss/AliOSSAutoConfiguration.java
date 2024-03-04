package com.aliyun.oss;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(AliOssProperties.class)
public class AliOSSAutoConfiguration {

    @Bean
    public AliOssUtil aliOssUtil(AliOssProperties aliOssProperties) {
        AliOssUtil aliOssUtil = new AliOssUtil();
        aliOssUtil.setAliOssProperties(aliOssProperties);
        return aliOssUtil;
    }
}
