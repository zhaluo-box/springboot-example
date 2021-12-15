package com.example.boot.base.common.service.appplugin;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.net.URLClassLoader;

/**
 *
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(of = { "appId" })
@RequiredArgsConstructor
public class ApplicationContextInfo {

    @NonNull
    private String appId;

    private ThreadPoolTaskScheduler taskScheduler;

    private AnnotationConfigApplicationContext applicationContext;

    private URLClassLoader classLoader;

}
