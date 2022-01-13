package com.example.boot.kafka.cluster.consumer.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.listener.ConsumerAwareListenerErrorHandler;
import org.springframework.kafka.listener.ListenerExecutionFailedException;
import org.springframework.messaging.Message;

/**
 *
 */
@Slf4j
@Configuration
public class KafkaConsumerConfig {

    /**
     * 自定义异常处理器
     */
    @Bean
    public ConsumerAwareListenerErrorHandler consumerAwareListenerErrorHandler() {
        return new CustomConsumerAwareListenerErrorHandler();
    }

    /**
     * 自定义消息过滤
     */
    @Bean
    public ConcurrentKafkaListenerContainerFactory filterContainerFactory(ConsumerFactory consumerFactory) {

        ConcurrentKafkaListenerContainerFactory factory = new ConcurrentKafkaListenerContainerFactory();
        factory.setConsumerFactory(consumerFactory);
        //        factory.getContainerProperties().setSyncCommits(false); 设置syncCommit 属性, 默认true 是同步提交, false 为默认提交
        // 被过滤的消息将被丢弃
        factory.setAckDiscarded(true);

        //消息过滤策略
        factory.setRecordFilterStrategy(consumerRecord -> {
            // 偶数消息直接过滤
            if (Integer.parseInt(consumerRecord.value().toString()) % 2 == 0) {
                return false;
            }
            //返回true消息则被过滤
            return true;
        });

        return factory;

    }

    public static class CustomConsumerAwareListenerErrorHandler implements ConsumerAwareListenerErrorHandler {

        @Override
        public Object handleError(Message<?> message, ListenerExecutionFailedException exception, Consumer<?, ?> consumer) {

            System.out.println("============自定义异常处理器===========");
            System.out.println(message);
            log.error("异常: ", exception);
            return null;
        }
    }

}
