package com.zlb.example.annotation.pool;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 我的线程池.
 */
@Configuration
public class MyThreadPool {

    @Value("${thread.coreSize:15}")
    private int coreSize = 15;


    @Bean("customExecutorService")
    public ExecutorService customThreadPool() {
        final ExecutorService myThreadPool = Executors.newFixedThreadPool(coreSize, new NameThreadFactory("myThreadPool"));
        return myThreadPool;
    }


    /**
     * 定义线程名称.
     */
    protected static class NameThreadFactory implements ThreadFactory {

        private String name;

        private AtomicInteger tag = new AtomicInteger(0);

        public NameThreadFactory(String name) {
            super();
            this.name = name;
        }

        @Override
        public Thread newThread(Runnable r) {
            final Thread thread = new Thread(r);
            thread.setName(name + "-" + tag.getAndIncrement());
            return thread;
        }
    }

}
