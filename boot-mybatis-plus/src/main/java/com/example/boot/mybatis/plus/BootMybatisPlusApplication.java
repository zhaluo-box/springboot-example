package com.example.boot.mybatis.plus;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.example.boot.mybatis.plus.mapper")
public class BootMybatisPlusApplication {

    public static void main(String[] args) {
        SpringApplication.run(BootMybatisPlusApplication.class, args);
    }

}
