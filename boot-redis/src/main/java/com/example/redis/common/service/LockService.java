package com.example.redis.common.service;

import java.util.concurrent.TimeUnit;

/**
 *
 */
public interface LockService {

    /**
     * 获取锁 (可重入锁)
     *
     * @param lockKey 锁名称
     */
    void lock(String lockKey);

    /**
     * 获取锁 (可重入锁)
     *
     * @param lockKey   锁名称
     * @param leaseTime 持有锁时间 如果持有锁时间超过指定时间 key自动释放
     * @param unit      时间单位
     */
    void lock(String lockKey, long leaseTime, TimeUnit unit);

    /**
     * 获取锁 （非阻塞）
     *
     * @param lockKey 锁名称
     * @return 获取锁是否成功
     */
    boolean tryLock(String lockKey);

    /**
     * 获取锁 （非阻塞）
     *
     * @param lockKey 锁名称
     * @param time    获取锁等待的最大时长
     * @param unit    时间单位
     * @return 获取锁是否成功
     */
    boolean tryLock(String lockKey, long time, TimeUnit unit);

    /**
     * 获取锁 （非阻塞）
     *
     * @param lockKey   锁名称
     * @param waitTime  获取锁等待的最大时长
     * @param leaseTime 持有锁时间 如果持有锁时间超过指定时间 key自动释放
     * @param unit      时间单位
     * @return 获取锁是否成功
     */
    boolean tryLock(String lockKey, long waitTime, long leaseTime, TimeUnit unit);

    /**
     * 释放锁
     *
     * @param lockKey 锁名称
     */
    void releaseLock(String lockKey);
}
