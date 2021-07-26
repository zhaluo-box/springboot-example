package com.example.dbhikricp.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.*;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SpringBeanFlow implements InitializingBean, DisposableBean, BeanNameAware, BeanFactoryAware, ApplicationContextAware {
    @Override
    public void destroy() throws Exception {
        log.error("bean 销毁的! destroy() method!");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.error("bean initializing  afterPropertiesSet() method");
    }

    @Override
    public void setBeanName(String s) {
        log.error("BeanNameAware setBeanName {} ", s);
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        log.error("beanFactoryAware setBeanFactory");
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        log.error("ApplicationContextAware setApplicationContext,{}", applicationContext);
    }

//    @Override
//    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
//        log.error("postProcessBeforeInitialization");
//        return null;
//
//    }
//
//    @Override
//    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
//        log.error("postProcessAfterInitialization");
//        return null;
//    }

    public void handle() {
        log.error("-------------------handle ----------------");
        return;
    }
}
