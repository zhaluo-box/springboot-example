package com.example.boot.simple.framework.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 与注解ApiResource 搭配使用， 用于记录日志详情和权限分组
 * Created  on 2022/2/21 21:21:41
 *
 * @author zl
 * @see com.example.boot.simple.framework.common.annotation.ApiResource
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiGroup {

    String name();

}
