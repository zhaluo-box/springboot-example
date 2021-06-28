package com.zl.example.filter;

import org.springframework.integration.core.MessageSelector;
import org.springframework.messaging.Message;

/**
 * @Author: zl
 * @Description: message  selector  官方推荐过滤器实现的选择器 ,主要方法返回一个 Boolean
 * @Date: 2021/6/28 14:10
 */
public class CustomMessageSelector implements MessageSelector {

    @Override
    public boolean accept(Message<?> message) {
        return false;
    }
}
