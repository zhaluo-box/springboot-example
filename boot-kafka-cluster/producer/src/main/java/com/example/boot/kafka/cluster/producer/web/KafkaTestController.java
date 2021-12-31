package com.example.boot.kafka.cluster.producer.web;

import com.example.boot.kafak.cluster.common.entity.Account;
import com.example.boot.kafak.cluster.common.service.KafkaProduceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Kafka 生产者测试
 */
@RestController
@RequestMapping("/kafka-tests/")
public class KafkaTestController {

    @Autowired
    private KafkaProduceService kafkaProduceService;

    /**
     * 生产消息
     */
    @PostMapping("actions/produce-simple-messages/")
    public void produceMessage(@RequestBody Account account) {
        kafkaProduceService.sendMessage(account);
    }

    /**
     * 带回调的生产者
     */
    @PostMapping("actions/produce-callback/")
    public void callback(@RequestBody Account account) {
        kafkaProduceService.callback(account);
    }

    @PostMapping("actions/batch-send/")
    public void batchSend(@RequestBody Account account) {
        kafkaProduceService.batchSend(account);
    }

    /**
     * 同步发送
     */
    @GetMapping("actions/sync-send/")
    public void syncSend() {
        kafkaProduceService.syncSend();
    }

    /**
     * kafka 事务管理
     */
    @GetMapping("actions/transaction/")
    public void testTransaction() {
        kafkaProduceService.testTransaction();
    }

    /**
     * 消息过滤
     */
    @GetMapping
    public void messageFilter(){
        kafkaProduceService.messageFilter();
    }

}
