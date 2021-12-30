package com.example.boot.kafak.cluster.common.service;

import com.example.boot.kafak.cluster.common.entity.Account;

/**
 * Created  on 2021/12/28 21:21:41
 *
 * @author wmz
 */
public interface KafkaConsumerService {

    void retrieveMessage(Account account);
}
