package com.example.boot.autoconfiguration.properties.config;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * Created  on 2022/3/31 16:16:33
 *
 * @author zl
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Import({ BeanConfig.class })
public @interface EnableApprove {
}
