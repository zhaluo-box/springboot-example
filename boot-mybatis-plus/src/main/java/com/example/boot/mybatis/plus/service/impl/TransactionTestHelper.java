package com.example.boot.mybatis.plus.service.impl;

import com.example.boot.mybatis.plus.entity.User;
import com.example.boot.mybatis.plus.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created  on 2022/5/6 15:15:46
 *
 * @author zl
 */
@Component
public class TransactionTestHelper {

    @Autowired
    private UserService userService;

    @Transactional(propagation = Propagation.SUPPORTS)
    public void propagationSupport(User user) {
        userService.save(user);
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
    public void propagationSupportInnerError(User user) {
        userService.save(user);
        throw new RuntimeException("Propagation.SUPPORTS 内部异常");
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.MANDATORY)
    public void propagationMandatory(User user) {
        userService.save(user);
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public void propagationRequireNew(User user) {
        userService.save(user);
    }
}
