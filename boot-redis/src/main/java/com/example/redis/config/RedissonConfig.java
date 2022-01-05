package com.example.redis.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 */
@Configuration
public class RedissonConfig {

    @Autowired
    private RedisProperties redisProperties;

    /**
     * TODO 待优化, 地址是写死的
     *
     */
    @Bean
    public RedissonClient getRedisson() {
        Config config = new Config();
        String address = "redis://" + redisProperties.getHost() + ":" + redisProperties.getPort();
        //单机模式  依次设置redis地址和密码
        config.useSingleServer().setAddress("redis://192.168.13.129:6379");
        config.setCodec(new StringCodec());
        return Redisson.create(config);
    }

}
