package com.zlb.example.annotation.strategy.impl;

import com.zlb.example.annotation.strategy.ICacheStrategy;
import org.springframework.stereotype.Component;

@Component("LFUCacheStrategy")
public class LFUCacheStrategy implements ICacheStrategy {
    @Override
    public String replace() {
        System.out.println("LFU 缓存淘汰策略!");
        return "LFU 缓存淘汰策略!";
    }
}
