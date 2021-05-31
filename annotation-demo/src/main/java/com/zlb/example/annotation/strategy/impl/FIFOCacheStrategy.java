package com.zlb.example.annotation.strategy.impl;

import com.zlb.example.annotation.strategy.ICacheStrategy;
import org.springframework.stereotype.Component;

@Component("FIFOCacheStrategy")
public class FIFOCacheStrategy implements ICacheStrategy {
    @Override
    public String replace() {
        System.out.println( "FIFO 缓存淘汰策略!" );
        return "FIFO 缓存淘汰策略!";
    }
}
