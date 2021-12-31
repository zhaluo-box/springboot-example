package com.example.boot.kafka.cluster.producer.config;

import com.example.boot.kafak.cluster.common.constant.TopicConstants;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

/**
 * 副本数量= leader + follower <= broker.size
 */
@Configuration
public class KafkaInitConfig {

    @Bean
    public NewTopic initialTopic() {
        return new NewTopic("topic1", 6, (short) 3);
    }

    @Bean
    public NewTopic newTopic() {
        return TopicBuilder.name(TopicConstants.TOPIC2).partitions(6).replicas(3).build();
    }

}
