package com.example.boot.kafak.cluster.common.service;

import com.example.boot.kafak.cluster.common.entity.Account;

/**
 * Created  on 2021/12/28 21:21:40
 *
 * @author wmz
 */
public interface KafkaProduceService {

    void sendMessage(Account account);

    void callback(Account account);

    void batchSend();
}
