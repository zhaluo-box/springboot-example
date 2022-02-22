package com.example.boot.simple.framework.common.bootstrap;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.Ordered;

/**
 * Created  on 2022/2/21 22:22:29
 *
 * @author zl
 */
public interface BootstrapService extends ApplicationListener<ApplicationReadyEvent>, Ordered {

    enum Order {
        API_RESOURCE
    }
}
