package com.example.redis.test;

import org.junit.Test;
import org.springframework.data.redis.core.RedisOperations;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

public class SpringDataRedisList extends BaseSpringDateRedisTest {

    private static final String LIST_KEY = "list:key:one";
    private static final String KEY_TWO = "list:key:two";
    private static final String KEY_THREE = "list:key:three";

    @Test
    public void listTest() {
        redisTemplate.opsForList().leftPush( LIST_KEY, 3 );
        final Long pushAll = redisTemplate.opsForList().leftPushAll( "listKey", "王刚", "嘟嘟", "123" );
        final Long rightPushAll = redisTemplate.opsForList().rightPushAll( "listKey", "你们好", "老王", "老廖", "老李" );
        System.out.println( rightPushAll );
        System.out.println( pushAll );
    }

    /**
     * leftPush(k,v) 向链表的最左边添加一个值
     * Long leftPushAll(K key, V... values);向链表的最左边添加多个值
     * leftPushAll(k,Collection<V> values)向链表的最左边添加多个值
     * leftPushIfPresent() 如果key存在则添加
     * leftPush(k,pivot,v) 在左边第一次发现pivot的左边添加4
     * --同理leftPush API --
     * rightPush(k,v)
     * rightPushAll(k,values)
     * rightPushAll(k,Collection<V> values)
     * rightPushIfPresent()
     * rightPush(k,pivot,v)
     */
    @Test
    public void pushTest() {
        //leftPush
        redisTemplate.opsForList().leftPush( LIST_KEY, 3 );
        // 在左边第一发现3的左边添加4
        redisTemplate.opsForList().leftPush( LIST_KEY, 3, 4 );
        // 在链表最左边添加5
        redisTemplate.opsForList().leftPush( LIST_KEY, 5 );
        // 添加多个值
        final List<? extends Serializable> asList = Arrays.asList( "想想就可怕", "蓝瘦", "香菇", 777 );
        redisTemplate.opsForList().leftPushAll( LIST_KEY, "你好", "redis链表", 666 );
        redisTemplate.opsForList().leftPushAll( LIST_KEY, asList );
        redisTemplate.opsForList().leftPushIfPresent( LIST_KEY, "立花雷藏" );
        // rightPush
        redisTemplate.opsForList().rightPush( LIST_KEY, "你么好,我是rightPush" );
        //  余下API类比leftPush
    }


    /**
     * trim(key,start,end) 截取链表
     * range(key,start,end) 获取列表中从start-end索引的值, 其中索引可以为负数,-1 为倒数第一,-2 倒数第二,一次排列
     */
    @Test
    public void rangeTest() {
        redisTemplate.opsForList().trim( LIST_KEY, 0, -5 );
        final List range = redisTemplate.opsForList().range( LIST_KEY, 0, -1 );
        System.out.println( range );
        System.out.println( redisTemplate.opsForList().size( LIST_KEY ) );
    }


    /**
     * set (key,index,value) 设置索引为index的值为value
     * remove (key,count,value) 移除count个value
     * index (key,index) 查询并返回index对应的value
     */
    @Test
    public void setTest() {
        redisTemplate.opsForList().set( LIST_KEY, 17, "牛顿" );
        redisTemplate.opsForList().remove( LIST_KEY, 1, 777 );
        final Object index = redisTemplate.opsForList().index( LIST_KEY, 2 );
        System.out.println( index );
    }

    /**
     * leftPop(K) 从K链表的左边弹出一个元素并返回这个元素的值
     * leftPop(K , timeout, TimeUnit );  从K链表的左边在指定时间内弹出一个元素并返回这个元素的值
     * rightPop (K)
     * rightPop (K , timeout, TimeUnit )
     * rightPopAndLeftPush(sourceKey,destinationKey) 从sourceKey右边弹出一个元素并添加到destinationkey的左边
     * rightPopAndLeftPush()
     * getOperations
     */
    @Test
    public void popTest() {
        //final Object o = redisTemplate.opsForList().leftPop( LIST_KEY, 1, TimeUnit.MICROSECONDS );
        // System.out.println( o );
        System.out.println( redisTemplate.opsForList().range( LIST_KEY, 0, -1 ) );
        redisTemplate.opsForList().rightPopAndLeftPush( LIST_KEY, "pushAllVerify" );
        System.out.println( redisTemplate.opsForList().range( "pushAllVerify", 0, -1 ) );
        final RedisOperations<String, Object> operations = redisTemplate.opsForList().getOperations();
    }

    /**
     * redis 链表内部编码造数据
     */
    @Test
    public void encodingTest() {
        //1. 元素个数小于 512个;
        //2. 元素值小于64字节
        getForList().rightPush( LIST_KEY, "998" );

        // 元素值大于64字节
        getForList().leftPush( KEY_TWO, "烟波　瀰漫掩樯橹　眨眼汹涌吞噬　没江底酆都\n" +
                "屠苏　落喉几朝暮　冬去春寒未了　伴冷夜暗雨\n" +
                "刀斧　剑芒遍五湖　遥祭散骸豪骨　焉回首眷顾" );

        // 元素个数超过512个
        for (int i = 0; i < 513; i++) {
            getForList().leftPush( KEY_THREE, i+"" );

        }
    }

}
