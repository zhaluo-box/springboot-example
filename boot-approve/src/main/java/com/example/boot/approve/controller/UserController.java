package com.example.boot.approve.controller;

import com.example.boot.approve.common.utils.SecurityUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created  on 2022/3/18 09:9:12
 *
 * @author zl
 */
@RestController
@RequestMapping("/users/")
public class UserController {

    @GetMapping
    public ResponseEntity<Void> login(@RequestParam long id, @RequestParam String name) {
        SecurityUtil.reset(id, name);
        return ResponseEntity.ok().build();
    }
}
