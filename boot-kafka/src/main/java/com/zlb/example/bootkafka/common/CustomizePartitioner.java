//package com.zlb.example.bootkafka.common;
//
//import lombok.extern.slf4j.Slf4j;
//import org.apache.kafka.clients.producer.Partitioner;
//import org.apache.kafka.common.Cluster;
//
//import java.util.Map;
//import java.util.Random;
//
//@Slf4j
//public class CustomizePartitioner implements Partitioner {
//
//    @Override
//    public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
//        // 自定义分区规则(这里假设全部发到0号分区)
//        // ......
//        Random random = new Random();
//        int i = random.nextInt(8);
//        log.info("主题: {}, key: {} 分区: {}", topic, key, i);
//        return i;
//    }
//
//    @Override
//    public void close() {
//    }
//
//    @Override
//    public void configure(Map<String, ?> configs) {
//    }
//}
