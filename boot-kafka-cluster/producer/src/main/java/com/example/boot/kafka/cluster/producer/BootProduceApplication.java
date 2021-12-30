package com.example.boot.kafka.cluster.producer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class BootProduceApplication {

    public static void main(String[] args) {
        log.info("生产者启动！");
        SpringApplication.run(BootProduceApplication.class, args);
    }
}
