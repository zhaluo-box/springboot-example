package com.example.boot.approve.service;

import java.util.List;

/**
 * TODO 消息服务 暂时不做实现
 * Created  on 2022/3/15 14:14:03
 *
 * @author zl
 */
public interface MessageService {

    /**
     * 消息通知的本质是将消息保存在数据库，由前端不断pull与自己相关的未读消息实现
     *
     * @param receivers 接收者
     * @param message   消息
     */
    void notifyMessage(List<Long> receivers, String message);
}
