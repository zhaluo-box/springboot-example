package com.example.boot.kafka.cluster.consumer.service;

import com.example.boot.kafak.cluster.common.entity.Account;
import com.example.boot.kafak.cluster.common.service.KafkaConsumerService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

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

}
