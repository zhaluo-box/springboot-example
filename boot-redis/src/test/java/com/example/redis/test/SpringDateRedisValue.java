package com.example.redis.test;

import org.junit.Test;
import org.springframework.data.redis.core.RedisCallback;

import java.util.List;

public class SpringDateRedisValue extends BaseSpringDateRedisTest {

    static final String STRING_KEY_ONE = "String:Key:One";

    static final String STRING_KEY_TWO = "String:Key:Two";

    static final String STRING_KEY_THREE = "String:Key:Three";

    static final String STRING_KEY_FOUR = "String:Key:Four";

    /**
     * set
     * set
     * set
     * setBit
     * setIfAbsent
     * size
     */
    @Test
    public void testSet() {
        getForValue().set(STRING_KEY_ONE, "12345678");
        System.out.println(getForValue().get(STRING_KEY_ONE));
        getForValue().set(STRING_KEY_ONE, "h", 0);
        System.out.println(getForValue().get(STRING_KEY_ONE));
        getForValue().setBit(STRING_KEY_ONE, 2, true);

        // 测试有问题, 检查序列化方式等
        //        final User user = new User("小明同学", 23, 55.7D);
        //        getForValue().set(STRING_KEY_THREE, user);
        //        final User o = (User) getForValue().get(STRING_KEY_THREE);
        //        System.out.println(o);
    }

    /**
     * get
     * get
     * getAndSet
     * getBit
     * getOperations
     */
    @Test
    public void testGet() {
        System.out.println(getForValue().get(STRING_KEY_ONE));
        System.out.println(getForValue().get(STRING_KEY_ONE, 1, 2));
        //        getForValue().getAndSet(  )
    }

    /**
     * increment
     * increment
     * multiGet
     * multiSet
     * multiSetIfAbsent
     */
    @Test
    public void testOtherApi() {
        // 测试管道
        final List<Object> objects = redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
            connection.openPipeline();
            //                final Boolean exists = connection.keyCommands().exists( STRING_KEY_ONE.getBytes() );
            //                final Boolean exist2 =
            //                        connection.keyCommands().exists( STRING_KEY_TWO.getBytes() );
            final byte[] dump = connection.keyCommands().dump(STRING_KEY_THREE.getBytes());
            return null;
        });
        objects.forEach(System.out::println);
    }

    /**
     * redis 编码测试
     */
    @Test
    public void encodingTest() {
        getForValue().set(STRING_KEY_FOUR, "8653");
        RedisCallback callback = connection -> {
            connection.setCommands();

            return null;
        };
        //        redisTemplate.execute( callback );
    }

    /**
     * 测试scan 命令 scan 命令基于迭代器, 游标,
     * scan  key 0 match f*
     * key : 建
     * 0 : 游标
     * match : 匹配
     * f*  : 匹配的表达式
     * TODO scan 的实现
     */
    @Test
    public void testScan() {

    }
}
