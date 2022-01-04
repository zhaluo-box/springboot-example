package com.example.redis.test;

import com.example.redis.utils.RandomValue;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.SetOperations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Slf4j
public class SpringDataRedisSetTest extends BaseSpringDateRedisTest {

    protected static final String SET_KEY = "set:key:one";

    protected static final String SET_KEY2 = "set:key:two";

    protected static final String SET_KEY3 = "set:key:three";

    protected static final String SET_KEY4 = "set:key:four";

    protected static final String SET_INNERONE = "set:key:inner:one";

    protected static final String SET_INNERTWO = "set:key:inner:two";

    protected static final String SET_INNERTHREE = "set:key:inner:three";

    //@Before
    public void initSetKey() {
        int length = 5;
        for (int i = 0; i < length; i++) {
            getForSet().add(SET_KEY,
                            new String[] { RandomValue.getChineseName(), RandomValue.getEmail(3, 20), RandomValue.getRoad(), RandomValue.getNum(10, 99) + "" });
        }
        for (int i = 0; i < length; i++) {
            getForSet().add(SET_KEY2,
                            new String[] { RandomValue.getChineseName(), RandomValue.getEmail(3, 20), RandomValue.getRoad(), RandomValue.getNum(10, 99) + "" });
        }
    }

    /**
     * add (k,values..) 添加
     * remove (k,values..) 移除
     * pop (k) 弹出
     * pop(k,count) 弹出几个
     * move (sourcek,value,destK) 从sourceKey 移动value  到destKey
     * size (k) 个数
     */
    @Test
    public void test() {
        getForSet().add(SET_KEY, 1, 1, 2, 2, 3, 4, 5, "12");
        log.info("SetKey 长度: {}", getForSet().size(SET_KEY));
        getForSet().remove(SET_KEY, "12");
        System.out.println(getForSet().size(SET_KEY));
        //getForSet().pop( SET_KEY ,2);
        System.out.println(getForSet().size(SET_KEY));
        getForSet().move(SET_KEY, 2, "bye");
    }

    /**
     * boolean  isMember(key,value) 检查key是否存在,存在返回true  反之false;
     * Set<V> intersect(K key, Collection<K> otherKeys) 两个key的交集;
     * Set<V> intersect(K key, Collection<K> otherKeys) key与多个key的集合的交集;
     * Long intersectAndStore(K key, K otherKey, K destKey) key与otherKey的交集存入destKey;
     * intersectAndStore(K key, Collection<K> otherKeys, K destKey)
     * key与otherKeys的交集存入destKey;
     */
    @Test
    public void isMemberTest() {
        final SetOperations<String, Object> forSet = getForSet();
        final Boolean member = forSet.isMember(SET_KEY, "72");
        log.info("setKey 中是否存在value 72 : {}", member);
        final Set<Object> intersect = forSet.intersect(SET_KEY, SET_KEY2);
        log.info("setkey与set:key:two的交集 : {}", intersect);
        final Long aLong = forSet.intersectAndStore(SET_KEY, SET_KEY2, SET_KEY3);
        log.info("setkey与set:key:two的交集个数: {}", aLong);
        forSet.intersectAndStore(SET_KEY, Arrays.asList("72", "1", "2", "3", "qwewqe", "ma6ryh9unzh@3721.net"), SET_KEY4);
    }

    /**
     * 测试key与集合的交集,差集,并集
     */
    @Test
    public void setInner() {
        final SetOperations<String, Object> forSet = getForSet();
        forSet.add(SET_INNERONE, "acc", "bcc", "123", "22");
        forSet.add(SET_INNERTWO, "bcc", "998");
        forSet.add(SET_INNERTHREE, "22", "121");
        final List<String> otherKeys = getOtherKeys();
        final Set<Object> setinner1 = forSet.intersect(SET_INNERONE, otherKeys);
        System.out.println(forSet.members(SET_INNERONE));
        System.out.println(forSet.members(SET_INNERTWO));
        System.out.println(forSet.members(SET_INNERTHREE));
        System.out.println(setinner1);
        forSet.intersectAndStore(SET_INNERONE, otherKeys, "123");
    }

    /**
     * 并集.
     * Set<V> union(K key, K otherKey); key与otherKey的并集.
     * Set<V> union(K key, Collection<K> otherKeys);key与otherKeys的并集.
     * Long unionAndStore(K key, Collection<K> otherKeys, K destKey);
     * key与otherKeys的并集存入destKey
     * Long unionAndStore(K key, K otherKey, K destKey); key与otherKey的并集存入destKey
     */
    @Test
    public void testUnion() {
        final SetOperations<String, Object> forSet = getForSet();
        final Set<Object> union = forSet.union(SET_INNERONE, SET_INNERTWO);
        log.info("Set<V> union(K key, K otherKey) --> result : {}", union);
        final List<String> otherKeys = getOtherKeys();
        final Set<Object> union1 = forSet.union(SET_INNERONE, otherKeys);
        log.info("Set<V> union(K key, Collection<K> otherKeys) --> result : {} ", union1);
        final Long aLong = forSet.unionAndStore(SET_INNERONE, SET_INNERTWO, SET_INNERTHREE);
        log.info("Long unionAndStore(K key, K otherKey, K destKey) --> result : {}", aLong);
        System.out.println(forSet.members(SET_INNERTHREE));
        final Long aLong1 = forSet.unionAndStore(SET_INNERONE, otherKeys, "123");
    }

    protected List<String> getOtherKeys() {
        final ArrayList<String> otherKeys = new ArrayList<>();
        otherKeys.add(SET_INNERTWO);
        otherKeys.add(SET_INNERTHREE);
        return otherKeys;
    }

    /**
     * Set<V> difference(K key, K otherKey) : key与otherKey比较结果返回key的差集
     * Set<V> difference(K key, Collection<K> otherKeys); key与otherKeys比较结果返回key的差集
     * Long differenceAndStore(K key, K otherKeys, K destKey);
     * key与otherKey比较并将返回的key的差集存入destkey
     * Long differenceAndStore(K key, Collection<K> otherKeys, K destKey);
     * key与otherKeys比较并将返回的key的差集存入destkey
     */
    @Test
    public void testDifference() {
        System.out.println(getForSet().members(SET_INNERONE));
        System.out.println(getForSet().members(SET_INNERTWO));
        System.out.println(getForSet().members(SET_INNERTHREE));
        final Set<Object> difference = getForSet().difference(SET_INNERONE, SET_INNERTWO);
        System.out.println(difference);
        final Set<Object> difference1 = getForSet().difference(SET_INNERONE, getOtherKeys());
        System.out.println(difference1);
        final Long diff1 = getForSet().differenceAndStore(SET_INNERONE, SET_INNERTWO, "diff1");
        final Long diff2 = getForSet().differenceAndStore(SET_INNERONE, getOtherKeys(), "diff2");
        System.out.println(getForSet().members("diff1"));
        System.out.println(getForSet().members("diff2"));
    }

    /**
     * members
     * randomMember
     * distinctRandomMembers
     * randomMembers
     * scan
     */
    @Test
    public void testMembers() {
        System.out.println(getForSet().members("diff1"));
        System.out.println(getForSet().randomMember("diff1"));
        System.out.println(getForSet().randomMembers("diff1", 2L));
        System.out.println(getForSet().distinctRandomMembers(SET_KEY, 10L));
        final ScanOptions scanOptions = ScanOptions.scanOptions().build();
        final Cursor<Object> scan = getForSet().scan(SET_KEY, scanOptions);
        System.out.println(scan.getCursorId());
        System.out.println(scan.getPosition());
        System.out.println(scan.isClosed());
        scan.forEachRemaining(System.out::println);
    }

    /**
     * redis 链表内部编码造数据
     * intset
     * hashtable
     */
    @Test
    public void encodingTest() {

        //1. 元素个数小于 512个;
        getForSet().add("set:key:five", "121121");

        // 元素个数超过512个
        for (int i = 0; i < 513; i++) {
            getForSet().add("set:key:six", i + "");
        }

    }

}
