package com.zl.box.bootmybaits;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan(basePackages = "com.zl.box.bootmybaits.mapper") // 不能扫描BaseMapper
public class BootMybaitsApplication {

    public static void main(String[] args) {
        SpringApplication.run(BootMybaitsApplication.class, args);
    }

}
