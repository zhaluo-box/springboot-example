package com.example.boot.base.service;

import com.example.boot.base.common.service.AsyncService;
import com.example.boot.base.common.view.UserView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 */
@Slf4j
@Service
@Transactional
public class DefaultAsyncService implements AsyncService, InitializingBean, DisposableBean {

    @Autowired
    private UserView userView;

    private ExecutorService executorService;

    @Override
    public void afterPropertiesSet() {
        executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    }

    @Override
    public void destroy() throws Exception {
        if (executorService != null) {
            executorService.shutdown();
        }
    }

    /**
     * 测试目的
     * 主要测试事务
     * 1. @Transactional 注解 没有@service会不会生效
     */
    @Override
    public void runTask() {
        var asyncExecutor = new AsyncExecutor(userView);
        executorService.execute(asyncExecutor);
        executorService.submit(asyncExecutor);
    }

}
