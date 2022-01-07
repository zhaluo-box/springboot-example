package com.example.redis.dao;

import com.example.redis.entity.User;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 *
 */

@Service
public class UserCacheDao {

    /**
     * 移除缓存
     */
    @CacheEvict(value = "user", key = "#root.args[0]")
    public void delete(String name) {
        System.out.println("do nothing ! delete cache !" + name);
    }

    /**
     * 将返回值 用于缓存
     */
    @CachePut(value = "user", key = "#user.name")
    public User update(User user) {
        return user;
    }

    public User save(User user) {
        return new User().setName("张三").setAge(12);
    }

    @Cacheable(value = "user", key = "#name")
    public User get(String name) {
        System.out.println("执行方法");
        return new User().setName(name).setAge(12);
    }
}
