package com.zlb.example.annotation.controller;

import com.zlb.example.annotation.strategy.CacheStrategyFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import sun.misc.Cache;

@RestController
public class StrategyController {

    @Autowired
    CacheStrategyFactory factory;

    @GetMapping("/strategy/{str}")
    public String getStrategy(@PathVariable ("str") String str){

        return  factory.getCacheStrategy( str ).replace();

    }

}
