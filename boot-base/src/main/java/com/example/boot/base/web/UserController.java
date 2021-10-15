package com.example.boot.base.web;

import com.example.boot.base.entity.jpa.User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users/")
public class UserController {

    @PostMapping
    public User create(@RequestBody User user) {

        return user;
    }

    @PutMapping
    public void update(@RequestBody User user) {

    }

    @DeleteMapping
    public void delete(@RequestBody User user) {

    }
}
