package com.example.boot.sentinel.config;

import com.alibaba.csp.sentinel.annotation.aspectj.SentinelResourceAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created  on 2022/3/24 13:13:41
 *
 * @author zl
 */
@Configuration
public class SentinelConfig {

    /**
     * sentinel 注解编程，基于AOP 注解切面拦截，所以需要手动开启，但是spring cloud 不需要
     */
    @Bean
    public SentinelResourceAspect sentinelResourceAspect() {
        return new SentinelResourceAspect();
    }
}
