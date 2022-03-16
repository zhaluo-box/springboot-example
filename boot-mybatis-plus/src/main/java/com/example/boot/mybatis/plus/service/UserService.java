package com.example.boot.mybatis.plus.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.boot.mybatis.plus.entiry.User;
import com.example.boot.mybatis.plus.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created  on 2022/3/16 09:9:20
 *
 * @author wmz
 */
@Slf4j
@Service
public class UserService extends ServiceImpl<UserMapper, User> {

    @Autowired
    private UserMapper userMapper;

    @Transactional(rollbackFor = Exception.class)
    public void insert(User user) {
        userMapper.insert(user);
        log.info(user.getId().toString());
    }
}
