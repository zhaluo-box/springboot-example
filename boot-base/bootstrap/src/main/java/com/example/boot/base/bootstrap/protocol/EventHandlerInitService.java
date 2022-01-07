package com.example.boot.base.bootstrap.protocol;

import com.example.boot.base.common.annotation.EventHandler;
import com.example.boot.base.common.annotation.RpcHandler;
import com.example.boot.base.common.bootstrap.BootstrapService;
import com.example.boot.base.common.service.event.EventDispatchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.HashSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by MingZhe on 2022/1/7 15:15:45
 *
 * @author wmz
 */
@Slf4j
@Service
public class EventHandlerInitService implements BootstrapService {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private EventDispatchService eventDispatchService;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        //  获取所有带有EventHandler 注解的方法
        var eventHandlers = Stream.of(applicationContext.getBeanDefinitionNames())
                                  .map(beanDefinitionName -> applicationContext.getBean(beanDefinitionName))
                                  .map(AopUtils::getTargetClass)
                                  .flatMap(clazz -> Stream.of(clazz.getDeclaredMethods()))
                                  .filter(method -> method.isAnnotationPresent(EventHandler.class))
                                  .peek(method -> {
                                      if (log.isDebugEnabled()) {
                                          var args = Stream.of(method.getParameters()).map(p -> p.getType().getName()).collect(Collectors.joining(","));
                                          log.debug("注册Event Handler:{}.{}({})", method.getDeclaringClass().getName(), method.getName(), args);
                                      }
                                  })
                                  .collect(Collectors.toList());
        eventDispatchService.setEventHandlers(eventHandlers);
        log.info("初始化同步事件EventHandler初始化完成。");

        var handlerSet = new HashSet<>();
        var rpcHandlers = Stream.of(applicationContext.getBeanDefinitionNames())
                                .map(bName -> applicationContext.getBean(bName))
                                .map(AopUtils::getTargetClass)
                                .flatMap(c -> Stream.of(c.getMethods()))
                                .filter(m -> m.isAnnotationPresent(RpcHandler.class))
                                .peek(m -> Assert.isTrue(m.getParameters().length == 1, "RpcHandler方法，只能接收一个参数。"))
                                .peek(m -> {
                                    var argClassName = m.getParameters()[0].getType().getName();
                                    Assert.isTrue(!handlerSet.contains(argClassName),
                                                  "对RPC请求类型：" + argClassName + "已经存在处理方法，类名：" + m.getDeclaringClass().getName() + "，方法名：" + m.getName());
                                    handlerSet.add(argClassName);
                                })
                                .peek(m -> {
                                    if (log.isDebugEnabled()) {
                                        var args = Stream.of(m.getParameters()).map(p -> p.getType().getName()).collect(Collectors.joining(","));
                                        log.debug("注册Rpc Handler:{}.{}({})", m.getDeclaringClass().getName(), m.getName(), args);
                                    }
                                })
                                .collect(Collectors.toList());
        eventDispatchService.setRpcHandlers(rpcHandlers);
    }

    @Override
    public int getOrder() {
        return Order.EVENT.ordinal();
    }
}
