package com.example.rabbit.test;

import com.example.rabbit.test.utils.RabbitConnectFactory;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

/**
 * Created  on 2022/9/20 11:11:49
 *
 * @author wmz
 */

@Slf4j
public class SimpleQueueTest extends AbstractQueueTest {

    @Test
    @DisplayName("简单队列测试:生产者")
    public void simpleQueueProducerTest() {
        try (var connection = RabbitConnectFactory.getConnection(); var channel = connection.createChannel();) {
            log.info("简单消息队列，消息发送开始！");
            // durable      ： 持久化
            // exclusive    :  独占
            // autoDelete   :  服务器不再使用的时候自动删除
            channel.queueDeclare(SIMPLE_QUEUE, true, false, false, null);
            for (int i = 0; i < 10; i++) {
                var message = "简单消息队列测试" + i;
                channel.basicPublish("", SIMPLE_QUEUE, null, message.getBytes(StandardCharsets.UTF_8));
            }
            log.info("简单消息队列，消息发送完毕！");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @SneakyThrows
    public static void main(String[] args) {
        consumer(SIMPLE_QUEUE, "消费者[1]： ", false);
        consumer(SIMPLE_QUEUE, "消费者[2]： ", false);
    }

}
