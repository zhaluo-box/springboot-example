package com.example.boot.base.common.bootstrap;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.Ordered;

/**
 *
 */
public interface BootstrapService extends ApplicationListener<ApplicationReadyEvent>, Ordered {

    enum Order {
        PROTOCOL, EVENT
    }
}
