package com.example.redis.test;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.*;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BaseSpringDateRedisTest {

    @Autowired
    protected RedisTemplate<String, Object> redisTemplate;

    public ValueOperations<String, Object> getForValue() {
        return redisTemplate.opsForValue();
    }

    public SetOperations<String, Object> getForSet() {
        return redisTemplate.opsForSet();
    }

    public ListOperations<String, Object> getForList() {
        return redisTemplate.opsForList();
    }

    public ZSetOperations<String, Object> getForZSet() {
        return redisTemplate.opsForZSet();
    }

    public HashOperations<String, Object, Object> getForHash() {
        return redisTemplate.opsForHash();
    }

    public HyperLogLogOperations<String, Object> getForHyperLogLog() {
        return redisTemplate.opsForHyperLogLog();
    }

    public GeoOperations<String, Object> getForGeo() {
        return redisTemplate.opsForGeo();
    }

    public ClusterOperations<String, Object> getForCluster() {
        return redisTemplate.opsForCluster();
    }
}
