package com.example.boot.base.common.service;

import com.example.boot.base.entity.jpa.User;

import java.util.List;
import java.util.Map;

public interface UserService {

    void save(User user);

    List<Map<String, Object>> findAllByUsername(String username);

    void addUser(User user);
}
