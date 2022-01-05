package com.example.redis.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisCacheConfig {

    // 缓存设置

    /**
     * redis 设置
     */
    @Bean
    @SuppressWarnings("all")
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory jedisConnectionFactory) {
        //        RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
        //        template.setConnectionFactory(factory);
        //        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        //        ObjectMapper om = new ObjectMapper();
        //        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        //        om.setDateFormat(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"));
        //        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        //        jackson2JsonRedisSerializer.setObjectMapper(om);
        //        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        //        template.setKeySerializer(stringRedisSerializer);
        //        template.setHashKeySerializer(stringRedisSerializer);
        //        template.setHashValueSerializer(jackson2JsonRedisSerializer);
        //        template.setDefaultSerializer(stringRedisSerializer);
        //        template.afterPropertiesSet();
        //        return template;

        RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
        template.setConnectionFactory(jedisConnectionFactory);
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        // key采用String的序列化方式
        template.setKeySerializer(stringRedisSerializer);
        // hash的key也采用String的序列化方式
        template.setHashKeySerializer(stringRedisSerializer);
        // value序列化方式采用jackson
        template.setValueSerializer(stringRedisSerializer);
        // hash的value序列化方式采用jackson
        template.setHashValueSerializer(jackson2JsonRedisSerializer);
        template.setDefaultSerializer(stringRedisSerializer);
        template.afterPropertiesSet();
        return template;

    }

}
