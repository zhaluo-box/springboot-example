package com.example.base.rabbit.common.utils;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 *
 */
public class RabbitConnectFactory {

    /**
     * 建立与RabbitMQ的连接
     */
    public static Connection getConnection() throws Exception {
        //定义连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        //设置服务地址
        factory.setHost("127.0.0.1");
        //端口
        factory.setPort(5672);
        //设置账号信息，用户名、密码、vhost
        factory.setVirtualHost("/");
        factory.setUsername("test");
        factory.setPassword("test");
        // 通过工程获取连接
        return factory.newConnection();
    }
}
