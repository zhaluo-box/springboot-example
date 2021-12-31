package com.example.boot.kafka.cluster.producer.config;

import com.example.boot.kafak.cluster.common.constant.TopicConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;

import java.util.Map;

/**
 * 自定义分区器,针对topic2 进行分区
 * 在配置文件指定自定义分区器
 * TODO kafka 能否给将topic 绑定给指定的分区器
 */
@Slf4j
public class CustomPartitioner implements Partitioner {

    @Override
    public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {

        log.info("集群信息 : 所有节点信息 : {} ", cluster.nodes());
        log.info("集群信息 : 集群资源,集群ID : {} ", cluster.clusterResource().clusterId());
        log.info("集群信息 : 节点信息Controller : {} ", cluster.controller().toString());
        log.info("集群信息 : 可用分区 : {} ", cluster.availablePartitionsForTopic(topic));

        // 如果是topic2 则全部发送到 1号分区
        if (topic.equals(TopicConstants.TOPIC2)) {
            return 1;
        }
        return 0;
    }

    @Override
    public void close() {

    }

    @Override
    public void configure(Map<String, ?> configs) {

    }
}
