package com.example.boot.autoconfiguration.properties.controller;

import com.example.boot.autoconfiguration.properties.config.TestConfigProperties;
import com.example.boot.autoconfiguration.properties.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created  on 2022/3/31 15:15:24
 *
 * @author zl
 */
@RestController
@RequestMapping("/tests/")
public class TestController {

    @Autowired
    private TestService testService;

    @GetMapping
    public void printProperties() {
        System.out.println(testService.print());
    }
}
