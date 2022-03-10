package com.example.boot.approve;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.boot.approve.mapper")
public class BootApproveApplication {

    public static void main(String[] args) {
        SpringApplication.run(BootApproveApplication.class, args);
    }

}
