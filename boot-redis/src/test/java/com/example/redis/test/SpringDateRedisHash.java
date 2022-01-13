package com.example.redis.test;

import com.example.redis.entity.Person;
import com.example.redis.entity.User;
import com.example.redis.utils.BeanTransUtil;
import org.junit.Test;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.ScanOptions;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class SpringDateRedisHash extends BaseSpringDateRedisTest {

    static final String person = "hash:common:person";

    static final String tag = "hash:common:tag";

    @Test
    public void test() {
        getForHash().put(tag, "name", "秀儿");
        final String uuid = UUID.randomUUID().toString();
        final Person person1 = new Person(uuid, "小熊猫", "35");
        final Map map = BeanTransUtil.objectToMap(person1);
        getForHash().putAll(person, map);
        final User user = new User("2B", 23, 55.5);
        final Map userMap = BeanTransUtil.objectToMap(user);
        getForHash().putAll(person, userMap);
    }

    @Test
    public void testPut() {
        final String uuid = UUID.randomUUID().toString();
        final Person person1 = new Person(uuid, "小熊猫", "35");
        String keyOne = "oras:3u:step";
        getForHash().put(keyOne, "1", person1);
        getForHash().put(keyOne, "2", person1);
        getForHash().put(keyOne, "3", person1);
        redisTemplate.expire(keyOne, 30, TimeUnit.MINUTES);
        final List<Object> values = getForHash().values(keyOne);

        for (Object value : values) {
            Person p = (Person) value;
            System.out.println(p.getName());
        }
    }

    /**
     * delete 删除
     * entries 获取一个迭代器
     * get  获取
     * getOperations
     * Boolean hasKey(H key, Object hashKey): 确定一个key 是否存在
     * Long increment(H key, HK hashKey, long delta): 给对应的hashKey增加一个Long值;
     * Double increment(H key, HK hashKey, double delta): 给对应的hashKey增加一个Double值;
     */
    @Test
    public void testDelete() {
        getForHash().delete(tag, "name");
        getForHash().entries(person).forEach((k, v) -> {
            System.out.println(k + " : " + v);
        });
        System.out.println(getForHash().hasKey(tag, "name"));
        System.out.println(getForHash().hasKey(person, "name"));
        System.out.println(getForHash().increment(person, "age", 20L));
        System.out.println(getForHash().increment(person, "weight", 5.5D));
        // System.out.println( getForHash().increment( tag, "age", 20L ) );
        System.out.println(getForHash().get(person, "name"));
        final RedisOperations<String, ?> operations = getForHash().getOperations();
    }

    /**
     * Set<HK> keys(H key): 获取所有hashKey;
     * List<HV> multiGet(H key, Collection<HK> hashKeys): 获取给定hashkeys的值;
     * Boolean putIfAbsent(H key, HK hashKey, HV value): 如果存在则不更新不覆盖;反之添加;
     * Cursor<Map.Entry<HK, HV>> scan(H key, ScanOptions options): 返回一个迭代器
     * Long size(H key): 获取key的hashKey个数;
     * List<HV> values(H key): 获取key的所有值;
     */
    @Test
    public void test2() {
        getForHash().keys(person).forEach(System.out::println);
        final List<Object> list = Arrays.asList("name", "age");
        System.out.println("----------------------------------------");
        getForHash().multiGet(person, list).forEach(System.out::println);
        final Boolean absent = getForHash().putIfAbsent(person, "name", "张三");
        System.out.println(absent);
        final Cursor<Map.Entry<Object, Object>> scan = getForHash().scan(person, ScanOptions.scanOptions().build());
        scan.forEachRemaining(e -> {
            System.out.println("Key : " + e.getKey());
            System.out.println("value : " + e.getValue());
        });
        System.out.println(getForHash().size(person));
        final List<Object> values = getForHash().values(person);
        values.forEach(System.out::println);
    }
}
