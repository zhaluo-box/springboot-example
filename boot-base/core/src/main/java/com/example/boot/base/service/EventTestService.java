package com.example.boot.base.service;

import com.example.boot.base.common.annotation.EventHandler;
import com.example.boot.base.common.dto.event.TestMessageEvent;
import com.example.boot.base.common.event.EventBus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 */
@Slf4j
@Service
public class EventTestService {

    @Autowired
    private EventBus eventBus;

    public void sendMessage(String message) {
        var testMessageEvent = new TestMessageEvent().setMessage(message).setSize(1);
        eventBus.send(testMessageEvent);
    }

    @EventHandler
    public void OnMessage(TestMessageEvent messageEvent) {
        log.info("事件监听消费 :" + messageEvent);
    }
}
