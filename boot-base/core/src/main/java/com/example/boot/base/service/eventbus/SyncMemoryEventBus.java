package com.example.boot.base.service.eventbus;

import com.example.boot.base.common.event.EventBus;
import com.example.boot.base.common.event.EventData;
import com.example.boot.base.common.service.event.EventDispatchService;
import lombok.AllArgsConstructor;

import java.util.List;

/**
 * 同步的, 基于内存调用的事件总线
 */
@AllArgsConstructor
public class SyncMemoryEventBus implements EventBus {

    private EventDispatchService eventDispatchService;

    @Override
    public void send(Object event) {
        eventDispatchService.dispatch((EventData) event);
    }

    @Override
    public <T> List<EventSendResult<T>> sendAndWait(Object event, Class<T> expectedClazz) {
        throw new UnsupportedOperationException("SyncMemoryEventBus不支持返回值.");
    }

    @Override
    public <T> T sendAndWait(String targetInstance, Object event, Class<T> expectedClazz) {
        throw new UnsupportedOperationException("SyncMemoryEventBus不支持返回值.");
    }
}
