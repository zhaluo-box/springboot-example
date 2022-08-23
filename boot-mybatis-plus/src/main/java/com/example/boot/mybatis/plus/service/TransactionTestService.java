package com.example.boot.mybatis.plus.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.boot.mybatis.plus.entity.User;

/**
 * Created  on 2022/5/6 14:14:46
 *
 * @author wmz
 */
public interface TransactionTestService extends IService<User> {

    /**
     * 测试用户新增 异常正常回滚
     */
    void rollbackSuccessful(User user);

    /**
     * 测试用户新增，回滚失败
     */
    void rollbackFail(User user);

    /**
     * 支持当前事务，如果当前存在事务，就加入该事务，如果当前不存在事务，就以非事务执行
     * 不存在事务， 不会回滚
     */
    void propagationSupportNoRollback(User user);

    /**
     * 支持当前事务，如果当前存在事务，就加入该事务，如果当前不存在事务，就以非事务执行
     * 加入事务回滚
     */
    void propagationSupportRollback(User user);

    /**
     * 测试内部异常，是否回滚
     */
    void propagationSupportInnerError(User user);

    /**
     * 测试内部异常，是否回滚
     */
    void propagationSupportInnerError2(User user);

    /**
     * 存在事务就加入，不存在就抛出异常
     */
    void propagationMandatory(User user);

    /**
     * 存在事务就加入，不存在就抛出异常
     */
    void propagationMandatoryThrowException(User user);

    /**
     * 无论是否存在事务，都创建新事务
     */
    void propagationRequireNew(User user);

    /**
     * 无论是否存在事务，都创建新事务
     * 遇到异常，不会回滚
     */
    void propagationRequireNewNoRollback(User user);
}
