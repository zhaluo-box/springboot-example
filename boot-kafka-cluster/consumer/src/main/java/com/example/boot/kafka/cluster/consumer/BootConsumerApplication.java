package com.example.boot.kafka.cluster.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 *
 */

@Slf4j
@SpringBootApplication
public class BootConsumerApplication {

    public static void main(String[] args) {
        System.out.println("消费者启动！ sout");
        log.info("消费者启动！");
        SpringApplication.run(BootConsumerApplication.class, args);
    }
}
