package com.example.boot.base.common.event;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 *
 */
public interface EventBus {

    /**
     * 异步发送事件事
     *
     * @param event 事件对像
     */
    void send(Object event);

    /**
     * 同步发送事件到。并等待全部节点的返回
     *
     * @param event         事件对像
     * @param expectedClazz 返回类型
     * @param <T>           返回类型
     * @return 全部节点的处理结果
     */
    <T> List<EventSendResult<T>> sendAndWait(Object event, Class<T> expectedClazz);

    /**
     * 同步将请求发送到指定节点
     *
     * @param targetInstance 要调用的请求节点的ID
     * @param event          事件对像
     * @param expectedClazz  返回类型
     * @param <T>            返回类型
     * @return 节点处理后返回的结果
     */
    <T> T sendAndWait(String targetInstance, Object event, Class<T> expectedClazz);

    @Data
    @Accessors(chain = true)
    class EventSendResult<T> {

        //        private String runtimeNodeId;

        private boolean success;

        private T result;

        private String message;

    }
}
