package com.example.boot.kafka.cluster.consumer.service;

import com.example.boot.kafak.cluster.common.constant.TopicConstants;
import com.example.boot.kafak.cluster.common.entity.Account;
import com.example.boot.kafak.cluster.common.service.KafkaConsumerService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created  on 2021/12/28 21:21:44
 *
 * @author wmz
 */
@Slf4j
@Service
public class DefaultKafkaConsumerService implements KafkaConsumerService {

    @Override
    public void retrieveMessage(Account account) {

    }

    @KafkaListener(topics = { "topic1" })
    public void onMessage1(ConsumerRecord<?, ?> record) {
        log.info("收到的消息: {} ", record);
        // 消费的哪个topic、partition的消息,打印出消息内容
        log.info("简单消费：" + record.topic() + "-" + record.partition() + "-" + record.value());
    }

    @KafkaListener(topics = { "topic2" })
    public void onMessage2(ConsumerRecord<String, Object> record) {
        log.info("TOPIC2 收到的消息: {} ", record);
        // 消费的哪个topic、partition的消息,打印出消息内容
        log.info("TOPIC2 简单消费：" + record.topic() + "-" + record.partition() + "-" + record.value());
    }

    /**
     * 批量消费
     */
    @KafkaListener(topics = { TopicConstants.TOPIC })
    public void batchConsumeMessage(List<ConsumerRecord<String, Object>> records) {
        System.out.println("批量消费开始!");
        records.forEach(System.out::println);
        System.out.println("批量消费结束!");
    }

    /**
     * 指定异常处理器
     */
    @KafkaListener(topics = { TopicConstants.TOPIC }, errorHandler = "consumerAwareListenerErrorHandler")
    public void customErrorHandle(ConsumerRecord<String, Object> record) {
        log.info("异常处理:{}", record);
        throw new RuntimeException("异常处理演示!");
    }

    /**
     * 消息过滤
     */
    @KafkaListener(topics = TopicConstants.TOPIC, containerFactory = "filterContainerFactory")
    public void messageFilter(ConsumerRecord<String, Object> record) {

        System.out.println("消息过滤" + record.value());

    }

    /**
     * 消息转发
     */
    @KafkaListener(topics = { TopicConstants.TOPIC })
    @SendTo(TopicConstants.TOPIC2)
    public String messageForward(ConsumerRecord<String, Object> record) {
        return record.value() + "forward message";
    }

    /**
     * 消费者组
     */
}
