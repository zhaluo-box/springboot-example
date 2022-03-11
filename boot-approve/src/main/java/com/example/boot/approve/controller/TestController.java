package com.example.boot.approve.controller;

import com.example.boot.approve.common.exception.ResourceConflictException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created  on 2022/3/11 09:9:53
 *
 * @author zl
 */
@RestController
@RequestMapping("/tests/")
public class TestController {

    @GetMapping
    public ResponseEntity<Void> exceptionTest() {

        throw new ResourceConflictException("资源冲突");
    }
}
