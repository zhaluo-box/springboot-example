package com.example.boot.base.service;

import com.example.boot.base.common.entity.jpa.User;
import com.example.boot.base.common.service.UserService;
import com.example.boot.base.common.view.UserView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Map;

@Service
public class DefaultUserService implements UserService {

    @Autowired
    private UserView userView;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Override
    @Transactional
    public void save(User user) {
        userView.insert(user);
    }

    @Override
    public List<Map<String, Object>> findAllByUsername(String username) {
        var list = userView.findAllByUsernameLike(username);
        Assert.isTrue(!list.isEmpty(), "结果为空!");
        return list;
    }

    @Override
    public void addUser(User user) {
        transactionTemplate.executeWithoutResult((status) -> {
            userView.save(user);
        });
    }
}
