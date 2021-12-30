package com.example.boot.kafka.cluster.producer.service;

import com.example.boot.kafak.cluster.common.entity.Account;
import com.example.boot.kafak.cluster.common.service.KafkaProduceService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.UUID;

/**
 * Created  on 2021/12/28 21:21:43
 *
 * @author wmz
 */
@Slf4j
@Service
public class DefaultKafkaProduceService implements KafkaProduceService {

    private static final String TOPIC1 = "topic1";

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public void sendMessage(Account account) {
        kafkaTemplate.send(TOPIC1, account.toString());
    }

    @Override
    public void callback(Account account) {
        kafkaTemplate.send(TOPIC1, account.getAddress()).addCallback(success -> {
            Assert.notNull(success, "success is null!");
            // 消息发送到的topic
            String topic = success.getRecordMetadata().topic();
            // 消息发送到的分区
            int partition = success.getRecordMetadata().partition();
            // 消息在分区内的offset
            long offset = success.getRecordMetadata().offset();
            System.out.println("发送消息成功:" + topic + "-" + partition + "-" + offset);

        }, failure -> {
            System.out.println("失败的异常信息： " + failure.getMessage());
        });

        kafkaTemplate.send(new ProducerRecord<>(TOPIC1, account.getName())).addCallback(new ListenableFutureCallback<SendResult<String, Object>>() {
            @Override
            public void onFailure(Throwable ex) {
                log.error("消息发送失败", ex);
            }

            @Override
            public void onSuccess(SendResult<String, Object> result) {
                log.info("消息发送成功，消息内容：{} ", result.getProducerRecord());
                log.info("消息发送成功，元数据  ：{} ", result.getRecordMetadata());
            }
        });
    }

    @Override
    public void batchSend() {
        for (int i = 0; i < 10000; i++) {
            kafkaTemplate.send(TOPIC1, RandomStringUtils.random(20));
        }
    }
}
