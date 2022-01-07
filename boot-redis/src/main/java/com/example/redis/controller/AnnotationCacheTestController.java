package com.example.redis.controller;

import com.example.redis.dao.UserCacheDao;
import com.example.redis.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 *
 */
@RestController
@RequestMapping("/cache-test/")
public class AnnotationCacheTestController {

    @Autowired
    private UserCacheDao userCacheDao;

    /**
     * 移除缓存
     */
    @DeleteMapping
    public void delete(@RequestParam String name) {
        userCacheDao.delete(name);
    }

    /**
     * 更新缓存
     */
    @PutMapping
    public void update(@RequestBody User user) {
        userCacheDao.update(user);
    }

    /**
     * 查询并新增缓存
     */
    @PostMapping
    public User save(@RequestBody User user) {
        return userCacheDao.save(user);
    }


    @GetMapping
    public User get(@RequestParam String name){
        return  userCacheDao.get(name);
    }

}
