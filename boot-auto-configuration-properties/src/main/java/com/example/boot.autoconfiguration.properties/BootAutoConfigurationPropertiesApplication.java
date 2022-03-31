package com.example.boot.autoconfiguration.properties;

import com.example.boot.autoconfiguration.properties.config.BeanConfig;
import com.example.boot.autoconfiguration.properties.config.EnableApprove;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
//@Import(BeanConfig.class)
@EnableApprove
public class BootAutoConfigurationPropertiesApplication {

    public static void main(String[] args) {
        SpringApplication.run(BootAutoConfigurationPropertiesApplication.class, args);
    }

}
