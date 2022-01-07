package com.example.boot.base.service.eventbus;

import com.example.boot.base.common.event.EventBus;
import com.example.boot.base.common.rest.DefaultRestClient;
import com.example.boot.base.common.rest.RestClient;
import com.example.boot.base.common.utils.SerializationUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

import java.util.List;

/**
 *
 */
@Slf4j
public class RestEventBus implements EventBus {

    @Override
    public void send(Object event) {
        Assert.notNull(event, "事件内容不能为空。");
        byte[] serializeBytes = SerializationUtils.serialize(event);
        doSend("instance", serializeBytes, event);
    }

    @Override
    public <T> List<EventSendResult<T>> sendAndWait(Object event, Class<T> expectedClazz) {
        Assert.notNull(event, "事件内容不能为空。");
        var sendResult = new EventSendResult<T>();
        try {
            T t = sendAndWait("instance", event, expectedClazz);
            sendResult.setResult(t);
            sendResult.setSuccess(true);
        } catch (Exception e) {
            sendResult.setSuccess(false);
            sendResult.setMessage(e.getMessage());
        }
        return List.of(sendResult);
    }

    @Override
    public <T> T sendAndWait(String targetInstance, Object event, Class<T> expectedClazz) {
        if (log.isDebugEnabled()) {
            log.debug("到节点：{}的RPC请求：{}", targetInstance, event);
        }
        var callResult = getClient("instance").call(SerializationUtils.serialize(event));
        Assert.notNull(callResult, () -> "RPC请求收到和响应为空。原始请求：{}" + event);
        Assert.isTrue(callResult.length > 0, () -> "RPC请求收到的数据为空。原始请求：{}" + event);
        return (T) SerializationUtils.deserialize(callResult);
    }

    /**
     * @param instanceId     TODO 优化 暂时冗余, 没有多节点
     * @param serializeBytes 消息体
     * @param event          事件本身
     */
    private void doSend(String instanceId, byte[] serializeBytes, Object event) {
        if (log.isDebugEnabled()) {
            log.debug("到节点：{}的事件：{}", instanceId, event);
        }
        getClient(instanceId).postEvent(serializeBytes);
    }

    private RestClient getClient(String instanceId) {
        return new DefaultRestClient(10000, 30000);
    }
}
