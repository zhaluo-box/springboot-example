package com.example.boot.base.common.service.event;

import com.example.boot.base.common.event.EventData;
import com.example.boot.base.common.event.MetaData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.util.Assert;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 事件分发服务
 */
@Slf4j
public abstract class EventDispatchService {

    /**
     * 所有的事件处理方法
     */
    private List<Method> eventHandlers = new ArrayList<>();

    /**
     *
     */
    private List<Method> rpcHandlers = new ArrayList<>();

    @Autowired
    private ApplicationContext applicationContext;

    public final List<Method> getRpcHandlers() {
        return rpcHandlers;
    }

    public final void setRpcHandlers(List<Method> rpcHandlers) {
        this.rpcHandlers = rpcHandlers;
    }

    public final void setEventHandlers(List<Method> handlers) {
        this.eventHandlers = handlers;
    }

    public final List<Method> getEventHandlers() {
        return eventHandlers;
    }

    /**
     * 事件分发
     *
     * @param eventData 事件元数据
     */
    public void dispatch(EventData eventData) {

        var event = eventData.getEvent();
        eventHandlers.stream().filter(method -> method.getParameters().length >= 1).filter(method -> {
            var parameter = method.getParameters()[0];
            return parameter.getType() == event.getClass(); // 第一个参数类型要匹配
        }).peek(m -> {
            // TODO 设计冗余, 但是MetaData 里面的属性,可以扩展 修改
            // 参数类型多于一个的时候需要校验,第一个参数必须是metaData类型
            if (m.getParameters().length > 1) {
                Assert.isTrue(m.getParameters().length == 2, "EventHandler方法的参数只能为1个或两个。");
                Assert.isTrue(m.getParameters()[1].getType() == MetaData.class, "EventHandler方法的第二个参数，必须为：MetaData类型。");
            }

        }).forEach(method -> {

            try {
                var parameters = method.getParameters();
                var parameter = parameters[0];
                // 获取声明该方法的bean
                Object bean = applicationContext.getBean(method.getDeclaringClass());

                // 只有一个参数, 并且方法类型与事件类型匹配
                if (parameters.length == 1 && parameter.getType() == event.getClass()) {
                    method.invoke(bean, event);
                }

                //有两个参数，第二个参数必须为MetaData
                if (method.getParameters().length == 2) {
                    method.invoke(bean, event, eventData.getMetaData());
                }

            } catch (InvocationTargetException e) {
                log.error("处理集群事件出错：{}", e.getTargetException().getMessage());
                log.error(e.getTargetException().getMessage(), e);
            } catch (Exception e) {
                log.error("处理集群事件出错：{}", e.getMessage());
                log.error(e.getMessage(), e);
            }

        });

    }

    /**
     * 调用RPC请求的处理者
     *
     * @param rpcRequest rpc的请求
     * @return rpc请求的响应
     */
    public Object call(Object rpcRequest) {
        var methods = rpcHandlers.stream().filter(m -> {
            //参数类型要一致
            var parameter = m.getParameters()[0];
            return parameter.getType() == rpcRequest.getClass();
        }).collect(Collectors.toList());

        Assert.isTrue(!methods.isEmpty(), "RPC请求：" + rpcRequest + "未找到合适的处理方法。");
        Assert.isTrue(methods.size() == 1, "找到多个RPC Handler可以处理事件：" + rpcRequest);

        try {
            var m = methods.get(0);
            var bean = applicationContext.getBean(m.getDeclaringClass());
            //只有一个参数
            return m.invoke(bean, rpcRequest);

        } catch (InvocationTargetException e) {
            // TODO 取真正的Message并返回
            log.error("RPC的原始请求：{}", e.getTargetException().getMessage());
            log.error(e.getTargetException().getMessage(), e);
            throw new RuntimeException(e.getTargetException());
        } catch (Exception e) {
            log.error("RPC的原始请求：{}", rpcRequest);
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

}
