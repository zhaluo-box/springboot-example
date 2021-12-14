package com.example.boot.base.web;

import com.example.boot.base.common.service.UserService;
import com.example.boot.base.entity.jpa.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/users/")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public User create(@RequestBody User user) {
        user.setId(UUID.randomUUID().toString());
        userService.save(user);
        return user;
    }

    @PutMapping
    public void update(@RequestBody User user) {

    }

    @DeleteMapping
    public void delete(@RequestBody User user) {

    }

    @GetMapping
    public List<Map<String, Object>> listAll(@RequestParam String username) {
        return userService.findAllByUsername(username);
    }

    /**
     * 基于transactionTemplate 操作
     */

    @PostMapping("actions/add-user/")
    public void addUser(@RequestBody User user) {
        userService.addUser(user);
    }
}
