package com.zlb.example.annotation.controller;


import com.zlb.example.HelloService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

@RestController
@Slf4j
public class HelloController {

    @Autowired
    HelloService helloService;

    @GetMapping("/hello")
    public String sayHello(String name) {
        return helloService.sayHello(name);
    }


    @Autowired
    private ExecutorService customExecutorService;


    @GetMapping("/pool")
    public String getPoolInfo() {
        final ArrayList<Integer> doubles = new ArrayList<>(1000);
        for (int i = 0; i < 100; i++) {
            //            double random = Math.random() * 100;
            doubles.add(i);
        }


        log.info(doubles.toString());
        System.out.println(doubles.size());

        int x = 0;
        int offset = 10;
        for (int j = 1; j <= 10; j++) {
            List<Integer> obj;
            if (j * offset < doubles.size()) {
                obj = doubles.subList(x, j * offset);

            } else {
                obj = doubles.subList(x, j * offset);

            }
            x = j * offset;
            customExecutorService.submit(() -> {
                log.info(Thread.currentThread().getName() + obj.toString());
            });
        }
        return "线程池执行!";
    }

}
