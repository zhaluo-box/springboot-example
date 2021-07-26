package com.zlb.example.annotation.strategy.impl;

import com.zlb.example.annotation.strategy.ICacheStrategy;
import org.springframework.stereotype.Component;

@Component("LRUCacheStrategy")
public class LRUCacheStrategy implements ICacheStrategy {
    @Override
    public String replace() {
        System.out.println("LRU 缓存淘汰策略!");
        return "LRU";
    }
}
