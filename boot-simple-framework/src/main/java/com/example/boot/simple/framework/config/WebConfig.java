package com.example.boot.simple.framework.config;

import com.example.boot.simple.framework.config.intercetpor.AuditLogInterceptor;
import com.example.boot.simple.framework.config.intercetpor.AuthInterceptor;
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

        // 注册认证过过滤拦截器
        registry.addInterceptor(new AuthInterceptor()).excludePathPatterns("/systems/actions/logout/", "/systems/actions/login/");
        // 注册审计日志拦截器
        registry.addInterceptor(new AuditLogInterceptor());
    }
}
