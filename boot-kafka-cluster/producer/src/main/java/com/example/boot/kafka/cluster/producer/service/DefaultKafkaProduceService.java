package com.example.boot.kafka.cluster.producer.service;

import com.example.boot.kafak.cluster.common.constant.TopicConstants;
import com.example.boot.kafak.cluster.common.entity.Account;
import com.example.boot.kafak.cluster.common.service.KafkaProduceService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

/**
 * Created  on 2021/12/28 21:21:43
 *
 * @author wmz
 */
@Slf4j
@Service
public class DefaultKafkaProduceService implements KafkaProduceService {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public void sendMessage(Account account) {
        kafkaTemplate.send(TopicConstants.TOPIC, account.toString());
    }

    @Override
    public void callback(Account account) {
        kafkaTemplate.send(TopicConstants.TOPIC, account.getAddress()).addCallback(success -> {
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

        kafkaTemplate.send(new ProducerRecord<>(TopicConstants.TOPIC, account.getName()))
                     .addCallback(new ListenableFutureCallback<SendResult<String, Object>>() {
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
    public void batchSend(Account account) {
        System.out.println(account);
        for (int i = 0; i < 10000; i++) {
            kafkaTemplate.send(TopicConstants.TOPIC, i + "");
        }
    }

    @Override
    public void syncSend() {
        try {
            kafkaTemplate.send(TopicConstants.TOPIC2, "同步发送消息: " + RandomStringUtils.random(10)).get();
        } catch (InterruptedException | ExecutionException e) {
            log.error("同步发送异常 ", e);
        }
    }

    /**
     * kafka事务测试
     */
    @Override
    public void testTransaction() {

        kafkaTemplate.executeInTransaction(operations -> {
            operations.send(TopicConstants.TOPIC2, "事务测试消息!,开启事务l");
            throw new RuntimeException("事务测试异常! ignored");
        });

        kafkaTemplate.send(TopicConstants.TOPIC, "事务测试消息,没有开启事务");
    }

    @Override
    public void messageFilter() {
        for (int i = 0; i < 10000; i++) {
            kafkaTemplate.send(TopicConstants.TOPIC, i + "");
        }
    }
}
