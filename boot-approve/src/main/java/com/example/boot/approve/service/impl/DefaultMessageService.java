package com.example.boot.approve.service.impl;

import com.example.boot.approve.service.MessageService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 消息服务
 * Created  on 2022/3/15 14:14:05
 *
 * @author zl
 */
@Service
public class DefaultMessageService implements MessageService {

    @Override
    @Transactional
    public void notifyMessage(List<Long> receivers, String message) {
        // 消息保存到数据库
    }
}
