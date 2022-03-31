package com.example.boot.sentinel.controller;

import com.example.boot.sentinel.service.SentinelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created  on 2022/3/24 12:12:02
 *
 * @author wmz
 */
@RestController
@RequestMapping("/sentinels/")
public class SentinelController {

    @Autowired
    private SentinelService sentinelService;

    @GetMapping("actions/test-annotation/")
    public String testSentinelAnnotation(@RequestParam(required = false) String param) {
        return sentinelService.testAnnotation(param);
    }

    @GetMapping("actions/test-code-processes/")
    public String testSentinelCodeProcess() {
        return sentinelService.testCodeProcess();
    }

}
