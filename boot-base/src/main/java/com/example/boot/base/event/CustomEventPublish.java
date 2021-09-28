package com.example.boot.base.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * 事件发布
 *
 */
@Component
public class CustomEventPublish {

    @Autowired
    private ApplicationContext applicationContext;

    public void publish(CustomEvent event){
        applicationContext.publishEvent(event);
    }
}
