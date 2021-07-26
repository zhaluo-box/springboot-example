package com.zlb.example.annotation.strategy;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;


import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class CacheStrategyFactory implements InitializingBean {


    protected static final Map<String, ICacheStrategy> beanMap = new ConcurrentHashMap<>();

    @Autowired
    ApplicationContext applicationContext;

    @Override
    public void afterPropertiesSet() throws Exception {
        final Map<String, ICacheStrategy> beansOfType = applicationContext.getBeansOfType(ICacheStrategy.class);
        beanMap.putAll(beansOfType);
        log.info(beanMap.toString());
    }


    public ICacheStrategy getCacheStrategy(String s) {
        return beanMap.get(s);
    }

}
