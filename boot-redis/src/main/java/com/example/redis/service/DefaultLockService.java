package com.example.redis.service;

import com.example.redis.common.service.LockService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.concurrent.TimeUnit;

/**
 *
 */
@Slf4j
@Service
public class DefaultLockService implements LockService {

    @Autowired
    private RedissonClient redissonClient;

    @Override
    public void lock(String lockKey) {
        Assert.hasLength(lockKey, "锁名称不能为空。");
        var lock = redissonClient.getLock(lockKey);
        lock.lock();
    }

    @Override
    public void lock(String lockKey, long leaseTime, TimeUnit unit) {
        Assert.hasLength(lockKey, "锁名称不能为空。");
        var lock = redissonClient.getLock(lockKey);
        lock.lock(leaseTime, unit);
    }

    @Override
    public boolean tryLock(String lockKey) {
        Assert.hasLength(lockKey, "锁名称不能为空。");
        var lock = redissonClient.getLock(lockKey);
        return lock.tryLock();
    }

    @Override
    public boolean tryLock(String lockKey, long time, TimeUnit unit) {
        Assert.hasLength(lockKey, "锁名称不能为空。");
        var lock = redissonClient.getLock(lockKey);
        try {
            return lock.tryLock(time, unit);
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException("获取锁[" + lockKey + "]失败:" + e.getMessage());
        }
    }

    @Override
    public boolean tryLock(String lockKey, long waitTime, long leaseTime, TimeUnit unit) {
        Assert.hasLength(lockKey, "锁名称不能为空。");
        var lock = redissonClient.getLock(lockKey);
        try {
            return lock.tryLock(waitTime, leaseTime, unit);
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException("获取锁[" + lockKey + "]失败:" + e.getMessage());
        }
    }

    @Override
    public void releaseLock(String lockKey) {
        Assert.hasLength(lockKey, "锁名称不能为空。");
        var lock = redissonClient.getLock(lockKey);
        lock.unlock();
    }

}
