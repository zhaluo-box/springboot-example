package com.example.boot.base.common.view;

import com.example.boot.base.entity.jpa.User;

import java.util.List;
import java.util.Map;

public interface UserView {

    /**
     * jdbcTemplate 实现
     */
    void insert(User user);

    List<Map<String, Object>> findAllByUsernameLike(String username);

    /**
     * jpa实现
     */
    void save(User user);
}
