package com.example.redis.test;


import org.junit.Assert;
import org.junit.Test;
import org.springframework.data.redis.connection.RedisZSetCommands;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.ZSetOperations;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SpringDateRedisZSet extends BaseSpringDateRedisTest {


    static final String ZSet_Key1 = "ZSet:One:Key";
    static final String ZSet_Key2 = "ZSet:Two:Key";
    static final String ZSet_Key3 = "ZSet:Three:Key";
    static final String ZSet_Key4 = "ZSet:Four:Key";

    /**
     * 测试ZSet 新增修改API
     */
    @Test
    public void testAdd() {
        final Boolean add = getForZSet().add( ZSet_Key1, 99, 100.0D );
        final Boolean add2 = getForZSet().add( ZSet_Key1, 89, 200.0D );
        final Boolean add3 = getForZSet().add( ZSet_Key1, 79, 300.0D );
        final Boolean add4 = getForZSet().add( ZSet_Key1, 19, 400.0D );
        final Set<ZSetOperations.TypedTuple<Object>> tuples = new HashSet<>();
        final DefaultTypedTuple<Object> runManMrDeng = new DefaultTypedTuple<>( "邓超",
                89.99D );
        final DefaultTypedTuple<Object> runManMrLu = new DefaultTypedTuple<>( "鹿晗",
                89.99D );

        tuples.add( runManMrDeng );
        tuples.add( runManMrLu );
        getForZSet().add( ZSet_Key2, tuples );
    }

    /**
     * 测试ZSet 删除API
     * remove
     * removeRange
     * removeRangeByScore
     */
    @Test
    public void testDelete() {
        getForZSet().remove( ZSet_Key1, 99 );
        //getForZSet().removeRange( ZSet_Key1,0,2 );
        getForZSet().removeRangeByScore( ZSet_Key1, 100.00D, 300.00D );

    }

    /**
     * 测试ZSet 查询API
     * Long count(K key, double min, double max); 统计key在min和max分数之间的value值数量
     * getOperations
     * incrementScore
     * intersectAndStore
     * intersectAndStore
     */
    @Test
    public void testSelect() {
        System.out.println( getForZSet().count( ZSet_Key1, 0, 500.00D ) );
        System.out.println( getForZSet().incrementScore( ZSet_Key1, 99, 12.25D ) );
        System.out.println( getForZSet().intersectAndStore( ZSet_Key1, ZSet_Key2, ZSet_Key3 ) );
        final List<String> otherKeys = Arrays.asList( ZSet_Key2, ZSet_Key3 );
        System.out.println( getForZSet().intersectAndStore( ZSet_Key1, otherKeys, ZSet_Key4 ) );
    }

    /**
     * Set<V> range(K key, long start, long end);
     * ------------------------------------------------------------------------------
     * Set<V> rangeByLex(K key, Range range): 获取限定范围内的值;
     * Range.range().gt/gte/lt/lte 限定范围
     * Set<V> rangeByLex(K key, Range range, Limit limit): 获取限定范围内的值,
     * 并在range限定的基础上取limit范围的值;
     * limit.limit().count().offset();
     * 取值实在range的基础上 再次限定 count 是取值的数量, offset 偏移量,也可以理解为索引[以0开始],包头不包尾;
     * 注意: rangeByLex key的所有值必须是字符串[否则报错],切分数相同,否则结果不准确; 因为rangByLex 是redis API
     * ZRangByLex命令, 其本质是通过ASCLL字符集编码进行排序;成员字符串作为二进制数组的字节数进行比较;
     * ------------------------------------------------------------------------
     * rangeByScore
     * rangeByScore
     * rangeByScoreWithScores
     * rangeByScoreWithScores
     * rangeWithScores
     * Long rank(K key, Object o): 确定o 在key中的位置排名[从0开始];
     */
    @Test
    public void testSelect2() {
        //init();
        System.out.println( getForZSet().range( ZSet_Key1, 0, 2 ) );
        System.out.println( getForZSet().rangeByLex( ZSet_Key4,
                RedisZSetCommands.Range.range().gt( "12" ) ) );
        System.out.println( getForZSet().rangeByLex( ZSet_Key4, RedisZSetCommands.Range.range().gt( "12" ),
                RedisZSetCommands.Limit.limit().count( 2 ).offset( 1 ) ) );
        System.out.println( getForZSet().rangeByScore( ZSet_Key1, 100D, 300D ) );
        final Set<ZSetOperations.TypedTuple<Object>> typedTuples = getForZSet().rangeByScoreWithScores( ZSet_Key1, 100D, 300D );
        Assert.assertNotNull( " 不能为null", typedTuples );
        typedTuples.forEach( e -> {
            System.out.println( e.getValue() + " : " + e.getScore() );
        } );
        getForZSet().rangeWithScores( ZSet_Key1, 1, 3 ).forEach( e -> {
            System.out.println( "withScores : " + e.getValue() + " : " + e.getScore() );
        } );

        System.out.println( getForZSet().rank( ZSet_Key1, 19 ) );
    }

    /**
     * Set<V> reverseRange(K key, long start, long end): 按照高分到低分的排序方式,获取Start-end排名的值,
     * reverseRangeByScore
     * reverseRangeByScore
     * reverseRangeByScoreWithScores
     * reverseRangeByScoreWithScores
     * reverseRangeWithScores
     * reverseRank
     * scan
     * score
     * size
     * unionAndStore
     * unionAndStore
     * zCard
     */
    @Test
    public void testSelectThree() {
        System.out.println( getForZSet().range( ZSet_Key1, 0, 2 ) );
        System.out.println( "compare top And Bottom" );
        System.out.println( getForZSet().reverseRange( ZSet_Key1, 0, 2 ) );

        System.out.println( "ZCard compare with Size: ------------ look down " );

        final Long aLong = getForZSet().zCard( ZSet_Key1 );
        final Long size = getForZSet().size( ZSet_Key1 );
        System.out.println( aLong );
        System.out.println( size );
    }


    public void init() {
        getForZSet().add( ZSet_Key4, "12", 0 );
        getForZSet().add( ZSet_Key4, "13", 0 );
        getForZSet().add( ZSet_Key4, "16", 0 );
        getForZSet().add( ZSet_Key4, "131", 0 );
        getForZSet().add( ZSet_Key4, "11", 0 );
        getForZSet().add( ZSet_Key4, "188", 0 );
        getForZSet().add( ZSet_Key4, "115", 0 );
        getForZSet().add( ZSet_Key4, "1125", 0 );
        getForZSet().add( ZSet_Key4, "10", 0 );
        getForZSet().add( ZSet_Key4, "18", 0 );
        getForZSet().add( ZSet_Key4, "17", 0 );
    }


    /**
     * redis 有序集合内部编码造数据
     * ziplist
     * skiplist
     */
    @Test
    public void encodingTest() {
        //1. 元素个数小于 128个;
        //2. 每个元素值小于64字节
        getForZSet().add( "zset:key:one", "rabbit", 99.0 );

        // 元素个数超过128个
        for (int i = 0; i < 129; i++) {
            getForZSet().add( "zset:key:two", "noddles" + i, 88.88 );
        }
    }
}
