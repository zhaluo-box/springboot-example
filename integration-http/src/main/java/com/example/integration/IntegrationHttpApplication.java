package com.example.integration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ImportResource(locations = "classpath:integration/http-config.xml")
public class IntegrationHttpApplication {

    public static void main(String[] args) {
        SpringApplication.run(IntegrationHttpApplication.class, args);
    }

}
