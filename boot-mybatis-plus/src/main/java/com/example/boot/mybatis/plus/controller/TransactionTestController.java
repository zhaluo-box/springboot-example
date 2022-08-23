package com.example.boot.mybatis.plus.controller;

import com.example.boot.mybatis.plus.entity.User;
import com.example.boot.mybatis.plus.service.TransactionTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created  on 2022/5/6 14:14:44
 *
 * @author zl
 */
@RestController
@RequestMapping("/transaction-tests/")
public class TransactionTestController {

    @Autowired
    private TransactionTestService transactionTestService;

    /**
     * 测试this.method() 失效
     */
    @PostMapping
    public ResponseEntity<Void> save(@RequestBody User user) {
        transactionTestService.rollbackSuccessful(user);
        return ResponseEntity.ok().build();
    }

    /**
     * 测试this.method() 失效
     */
    @PostMapping("actions/save-fail/")
    public ResponseEntity<Void> saveFail(@RequestBody User user) {
        transactionTestService.rollbackFail(user);
        return ResponseEntity.ok().build();
    }

    /**
     * 支持当前事务，如果当前存在事务，就加入该事务，如果当前不存在事务，就以非事务执行
     */
    @PostMapping("actions/propagation-supports-no-transaction-no-rollback/")
    public ResponseEntity<Void> propagationSupports(@RequestBody User user) {
        transactionTestService.propagationSupportNoRollback(user);
        return ResponseEntity.ok().build();
    }

    /**
     * 存在事务，回滚
     */
    @PostMapping("actions/propagation-support-exist-transaction-rollback/")
    public ResponseEntity<Void> propagationSupportRollback(@RequestBody User user) {
        transactionTestService.propagationSupportRollback(user);
        return ResponseEntity.ok().build();
    }

    /**
     * 存在事务，回滚
     */
    @PostMapping("actions/propagation-support-inner-errors/")
    public ResponseEntity<Void> propagationSupportInnerError(@RequestBody User user) {
        transactionTestService.propagationSupportInnerError(user);
        return ResponseEntity.ok().build();
    }

    /**
     * 存在事务，回滚
     */
    @PostMapping("actions/propagation-support-inner-errors2/")
    public ResponseEntity<Void> propagationSupportInnerError2(@RequestBody User user) {
        transactionTestService.propagationSupportInnerError2(user);
        return ResponseEntity.ok().build();
    }

    /**
     * 存在事务就加入事务，不存在就抛出异常
     */

    @PostMapping("actions/propagation-mandatory/")
    public ResponseEntity<Void> propagationMandatory(@RequestBody User user) {
        transactionTestService.propagationMandatory(user);
        return ResponseEntity.ok().build();
    }

    @PostMapping("actions/propagation-mandatory-no-transaction-throw-exception/")
    public ResponseEntity<Void> propagationMandatoryNoTransactionThrowException(@RequestBody User user) {
        transactionTestService.propagationMandatoryThrowException(user);
        return ResponseEntity.ok().build();
    }

    /**
     * 另起事务
     */
    @PostMapping("actions/propagation-require-new/")
    public ResponseEntity<Void> propagationRequireNew(@RequestBody User user) {
        transactionTestService.propagationRequireNew(user);
        return ResponseEntity.ok().build();
    }

    /**
     * 另起事务，遇到异常，
     */
    @PostMapping("actions/propagation-require-new-no-rollback/")
    public ResponseEntity<Void> propagationRequireNewNoRollback(@RequestBody User user) {
        transactionTestService.propagationRequireNewNoRollback(user);
        return ResponseEntity.ok().build();
    }

}
