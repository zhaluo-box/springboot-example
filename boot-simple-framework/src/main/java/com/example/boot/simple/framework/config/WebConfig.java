package com.example.boot.simple.framework.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Created  on 2022/2/22 14:14:32
 *
 * @author zl
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * TODO 添加跨域配置
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        WebMvcConfigurer.super.addCorsMappings(registry);
    }

    /**
     * TODO 添加拦截器
     * 拦截登录 校验token
     * 拦截记录 执行历史
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        WebMvcConfigurer.super.addInterceptors(registry);
    }
}
