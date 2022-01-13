package com.example.boot.base.common.event;

import com.example.boot.base.common.service.event.EventDispatchService;
import com.example.boot.base.common.utils.SerializationUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;

/**
 *
 */
@Slf4j
@RestController
public class EventHandlerController {

    @Autowired
    private EventDispatchService eventDispatchService;

    @PostMapping("/events/")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void handleEvents(@RequestBody byte[] eventData) {
        Assert.notNull(eventData, "Domain事件为Null。");
        Assert.isTrue(eventData.length > 0, "Domain事件的内容为空。");

        var event = (EventData) SerializationUtils.deserialize(eventData);
        var metaData = event.getMetaData();

        Assert.notNull(metaData, "事件的元数据不能为空。");

        if (log.isDebugEnabled()) {
            log.debug("收到Domain事件：{}", event);
        }

        // TODO 调用改为异步？
        // 考虑 @Async
        eventDispatchService.dispatch(event);

    }

    /**
     * 处理Rpc调用
     */
    @PostMapping("calls/")
    @ResponseBody
    public byte[] handleCalls(@RequestBody byte[] eventData) {
        Assert.notNull(eventData, "RPC调用的请求为Null。");
        Assert.isTrue(eventData.length > 0, "RPC调用的请求内容为空。");

        Object result = null;
        Object request = null;
        var obj = SerializationUtils.deserialize(eventData);
        if (obj instanceof EventData) {
            request = ((EventData) obj).getEvent();
        } else {
            request = obj;
        }

        if (log.isDebugEnabled()) {
            log.debug("收到RPC请求：{}", request);
        }

        // RPC必须同步！
        result = eventDispatchService.call(request);
        Assert.notNull(request, "RPC请求的结果不能返回空。");

        if (log.isDebugEnabled()) {
            log.debug("RPC处理结果：{}", result);
        }

        if (result instanceof String) {
            return ((String) result).getBytes(StandardCharsets.UTF_8);
        } else if (result instanceof byte[]) {
            return (byte[]) result;
        } else {
            return SerializationUtils.serialize(result);
        }
    }

}
