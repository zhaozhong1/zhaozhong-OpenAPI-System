package com.zhaozhong.config;

import com.zhaozhong.OpenAPIClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;

@Data
@Configuration
@ComponentScan
@ConfigurationProperties("zhaozhong.client")
public class OpenApiSdkConfig {

    private String accessKey;
    private String secretKey;

    @Bean
    public OpenAPIClient getClient(){
        return new OpenAPIClient(accessKey,secretKey);
    }

}
