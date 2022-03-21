package com.example.boot.approve.service.impl;

import com.example.boot.approve.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 消息服务
 * Created  on 2022/3/15 14:14:05
 *
 * @author zl
 */
@Slf4j
@Service
public class DefaultMessageService implements MessageService {

    @Override
    @Transactional
    public void notifyMessage(List<Long> receivers, String message) {
        // 考虑批量保存，不走循环
        receivers.forEach(r -> this.notifyMessage(r, message));
    }

    @Override
    @Transactional
    public void notifyMessage(long receiver, String message) {
        // TODO 消息保存到数据库

        log.debug("================接受人 ： {}， 消息 ：  {}", receiver, message);
    }
}
