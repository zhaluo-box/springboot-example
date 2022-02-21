package com.example.boot.simple.framework.common.annotation;

import org.springframework.core.annotation.AliasFor;

import javax.naming.Name;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created  on 2022/2/21 21:21:22
 *
 * @author zl
 */

@Target({ ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiResource {

    /**
     * API的值。 用于权限使用
     *
     * @return API名称
     */
    String value();

    /**
     * API的名称
     *
     * @return API名称
     */
    String name();

    /**
     * API是否启用安全（基于角色）校验 ，通常登录登出，不需要权限校验
     *
     * @return API是否启用安全（基于角色）校验
     */
    boolean security() default true;

}
