package com.example.redis.test;

import org.junit.Test;

public class EncodingTest extends BaseSpringDateRedisTest {

    private static final String LIST_KEY = "list:key:one";
    private static final String KEY_TWO = "list:key:two";
    private static final String KEY_THREE = "list:key:three";
    static final String person = "hash:common:person";
    static final String tag = "hash:common:tag";

    @Test
    public void dataInit() {

        // String 内部编码 int embstr raw
        //8个字节 39个字节 大于39个字节
        getForValue().set( "string:key:one","9988" );
        getForValue().set( "string:key:two","北龙归心号苍穹，竞曰风云山河" );
        getForValue().set( "string:key:three","星耀自古晦明时，不持太阿误剑诗!十方萧索无涯·千古夕阳有主·诗仙纵横·刀剑茫茫去不还" );
        // hash 内部编码
        // ziplist  元素个数小于512, 每个元素值小于64字节
        // hashtable
        getForHash().put( tag,"name","苦海女神龙" );
        getForHash().put("hash:common:key","hashtable",
                "一腔热肠，两坛吊儿，三五知己，醉了十部幽曲。六弦古琴，七觑红尘，八九豪情，乱了四方风云,一排轻筏，两处闲情，三五鸿雁，别了十里清溪。六发绝招，七具尸体，八九回合，杀了四年仇人");


        // 链表---  其实ziplist linkedlist 在3.2版本后就被quicklist 代替, quicklist 结合了前两者的优点
        //1. 元素个数小于 512个;
        //2. 元素值小于64字节
        getForList().rightPush( LIST_KEY, "998" );

        // 元素值大于64字节
        getForList().leftPush( KEY_TWO, "烟波　瀰漫掩樯橹　眨眼汹涌吞噬　没江底酆都\n" +
                "屠苏　落喉几朝暮　冬去春寒未了　伴冷夜暗雨\n" +
                "刀斧　剑芒遍五湖　遥祭散骸豪骨　焉回首眷顾" );

        // 元素个数超过512个
        for (int i = 0; i < 513; i++) {
            getForList().leftPush( KEY_THREE, i + "" );

        }

        //集合
        // intset 元素个数小于512
        // hashtable
        getForSet().add( "set:key:one","1","2" );

        for (int i = 0; i < 513; i++) {
            getForSet().add( "set:key:two",i+"" );
        }


        // 有序集合: ziplist
        //1. 元素个数小于 128个;
        //2. 每个元素值小于64字节
        getForZSet().add( "zset:key:one", "rabbit", 99.0 );

        // skipList
        // 元素个数超过128个
        for (int i = 0; i < 129; i++) {
            getForZSet().add( "zset:key:two", "noddles" + i, 88.88 );
        }
    }
}
