package com.example.boot.base.web;

import com.example.boot.base.event.CustomEvent;
import com.example.boot.base.event.CustomEventPublish;
import com.example.boot.base.service.EventTestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.*;

/**
 * 事件测试
 */
@Slf4j
@RestController
@RequestMapping("/event-test/")
public class EventController {

    @Autowired
    private CustomEventPublish eventPublish;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private EventTestService eventTestService;

    @GetMapping
    public void get(@RequestParam String message) {
        log.debug(message);
        var event = new CustomEvent(applicationContext);
        event.setMessage(message);
        eventPublish.publish(event);
    }

    @PostMapping("event-bus/")
    public void eventBusTest(@RequestParam String message) {
        eventTestService.sendMessage(message);

    }
}
