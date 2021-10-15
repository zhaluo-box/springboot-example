package com.example.boot.base.web;

import com.example.boot.base.event.CustomEvent;
import com.example.boot.base.event.CustomEventPublish;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 事件测试
 */
@Slf4j
@RestController
@RequestMapping("/events/")
public class EventController {

    @Autowired
    private CustomEventPublish eventPublish;

    @Autowired
    private ApplicationContext applicationContext;

    @GetMapping
    public void get(String message) {
        log.debug(message);
        var event = new CustomEvent(applicationContext);
        event.setMessage(message);
        eventPublish.publish(event);
    }
}
