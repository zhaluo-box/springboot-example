package com.example.rabbit.test.utils;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 现目前使用的是 / vhost
 * TODO 后期扩展自己的Vhost
 */
public class RabbitConnectFactory {

    /**
     * 建立与RabbitMQ的连接
     */
    public static Connection getConnection() throws IOException, TimeoutException {
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
