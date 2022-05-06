package com.example.boot.mybatis.plus.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.boot.mybatis.plus.entiry.User;
import com.example.boot.mybatis.plus.mapper.UserMapper;
import com.example.boot.mybatis.plus.service.TransactionTestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created  on 2022/5/6 14:14:47
 *
 * @author zl
 */
@Slf4j
@Service
public class DefaultTransactionTestService extends ServiceImpl<UserMapper, User> implements TransactionTestService {

    @Autowired
    private TransactionTestHelper transactionTestHelper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void rollbackSuccessful(User user) {
        System.out.println("==========" + user.toString());
        saveUser(user);
        throw new RuntimeException("1111");
    }

    private void saveUser(User user) {
        log.info("测试 this.method 注解加在public 方法，再调用 this.method()");
        save(user);
        log.info("用户已新增");
    }

    @Override
    public void rollbackFail(User user) {
        saveUserTransactionFail(user);
        throw new RuntimeException("测试事务失效，Bean.method不加注解，this.method（）加注解");
    }

    @Transactional
    public void saveUserTransactionFail(User user) {
        save(user);
    }

    @Override
    public void propagationSupportNoRollback(User user) {
        log.info("支持当前事务，如果当前存在事务，就加入该事务，如果当前不存在事务，就以非事务执行");
        log.info("不存在事务， 非事务执行，不会回滚");
        transactionTestHelper.propagationSupport(user);
        throw new RuntimeException("支持当前事务，如果当前存在事务，就加入该事务，如果当前不存在事务，就以非事务执行");
    }

    @Override
    @Transactional
    public void propagationSupportRollback(User user) {
        log.info("支持当前事务，如果当前存在事务，就加入该事务，如果当前不存在事务，就以非事务执行");
        log.info("存在事务， 加入，存在异常回滚");
        transactionTestHelper.propagationSupport(user);
        throw new RuntimeException("支持当前事务，如果当前存在事务，就加入该事务，如果当前不存在事务，就以非事务执行");
    }

    /**
     * 非事务执行
     */
    @Override
    public void propagationSupportInnerError(User user) {
        log.info("Propagation.SUPPORTS 内部异常");
        transactionTestHelper.propagationSupportInnerError(user);
        log.info("Propagation.SUPPORTS 内部异常");
    }

    /**
     * 加入事务执行
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void propagationSupportInnerError2(User user) {
        log.info("Propagation.SUPPORTS 内部异常，加入事务执行 开始");
        transactionTestHelper.propagationSupportInnerError(user);
        log.info("Propagation.SUPPORTS 内部异常 加入事务执行 结束");
    }

    @Override
    @Transactional
    public void propagationMandatory(User user) {
        transactionTestHelper.propagationMandatory(user);
    }

    @Override
    public void propagationMandatoryThrowException(User user) {
        transactionTestHelper.propagationMandatory(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void propagationRequireNew(User user) {
        User newUser = new User();
        BeanUtils.copyProperties(user, newUser);
        newUser.setName(newUser.getName() + "copy-new");
        save(user);
        transactionTestHelper.propagationRequireNew(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void propagationRequireNewNoRollback(User user) {
        User newUser = new User();
        BeanUtils.copyProperties(user, newUser);
        newUser.setName(newUser.getName() + "copy-new");
        save(user);
        transactionTestHelper.propagationRequireNew(user);
        throw new RuntimeException("另起事务异常测试");
    }
}
