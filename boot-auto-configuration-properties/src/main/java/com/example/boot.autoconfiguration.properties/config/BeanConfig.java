package com.example.boot.autoconfiguration.properties.config;

import com.example.boot.autoconfiguration.properties.service.TestService;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created  on 2022/3/31 16:16:27
 *
 * @author zl
 */
public class BeanConfig {

    @Bean
    @ConfigurationProperties(prefix = "test.properties")
    public TestConfigProperties testConfigProperties() {
        return new TestConfigProperties();
    }

    @Bean
    public TestService testService() {
        return new TestService();
    }
}
