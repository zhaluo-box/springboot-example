package com.zl.example.strategy;

import org.springframework.integration.dispatcher.LoadBalancingStrategy;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;

import java.util.Collection;
import java.util.Iterator;

/**
 * @Author: zl
 * @Description:
 * @Date: 2021/6/28 17:58
 */
public class CustomChannelLoadBalancingStrategy implements LoadBalancingStrategy {
    @Override
    public Iterator<MessageHandler> getHandlerIterator(Message<?> message, Collection<MessageHandler> handlers) {
        return null;
    }
}
