package com.example.redis.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 */
@Configuration
public class RedissonConfig {

    /**
     * TODO 待优化, 地址是写死的
     */
    @Bean
    public RedissonClient getRedisson() {
        Config config = new Config();
        //单机模式  依次设置redis地址和密码
        config.useSingleServer().setAddress("redis://192.168.13.129:6379");
        config.setCodec(new StringCodec());
        return Redisson.create(config);
    }

    //    @Value("${spring.redis.host}")
    //    private String host;
    //
    //    @Value("${spring.redis.port}")
    //    private String port;
    //
    //    @Value("${spring.redis.lettuce.pool.min-idle:0}")
    //    private int minIdleSize;
    //
    //    @Value("${spring.redis.database:0}")
    //    private int database;
    //
    //    @Value("${spring.redis.timeout:60000}")
    //    private int connectionOut;
    //
    //    @Value("${spring.redis.password:}")
    //    private String password;
    //
    //    @Bean
    //    public RedissonClient redissonClient() {
    //        var config = new Config();
    //        var cfg = config.useSingleServer()
    //                        .setAddress("redis://" + host + ":" + port)
    //                        .setConnectionMinimumIdleSize(minIdleSize)
    //                        .setDatabase(database)
    //                        .setConnectTimeout(connectionOut);
    //        if (StringUtils.hasLength(password)) {
    //            cfg.setPassword(password);
    //        }
    //        return Redisson.create(config);
    //    }
}
