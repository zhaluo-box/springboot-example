package com.example.integration.controller;

import com.example.integration.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/hello/")
public class HelloController {

    @PostMapping
    public Map<String, Object> get(@RequestBody User user) {
        log.debug(user.toString());
        return Map.of("name", "zhangsan");
    }
}
