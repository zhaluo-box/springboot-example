package com.example.boot.base.web;

import com.example.boot.base.common.service.AsyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 本Controller 主要模拟异步任务的一些情况
 */
@RestController
@RequestMapping("/async/")
public class AsyncController {

    @Autowired
    private AsyncService asyncService;

    /**
     * 整个service 开启事务, 调用JPA 方法
     */
    @GetMapping
    public void get() {
        asyncService.runTask();
    }
}
