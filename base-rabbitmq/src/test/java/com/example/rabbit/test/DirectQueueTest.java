package com.example.rabbit.test;

import com.example.rabbit.test.utils.RabbitConnectFactory;
import com.rabbitmq.client.BuiltinExchangeType;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

/**
 * 发布订阅 定向
 * Created  on 2022/9/20 16:16:28
 *
 * @author wmz
 */
@Slf4j
public class DirectQueueTest extends AbstractQueueTest {

    private static final String QUEUE_ONE = DIRECT_QUEUE + ":" + 1;

    private static final String QUEUE_TWO = DIRECT_QUEUE + ":" + 2;

    @Test
    @DisplayName("交换机根据路由Key定向投递")
    public void directQueueProducerTest() {
        try {
            var connection = RabbitConnectFactory.getConnection();
            var channel = connection.createChannel();
            channel.exchangeDeclare(EXCHANGE_DIRECT_NAME, BuiltinExchangeType.DIRECT);

            List<String> keys = Arrays.asList("info", "warn", "error", "debug");

            for (String key : keys) {
                var message = "路由Key定向投送消息" + key;
                channel.basicPublish(EXCHANGE_DIRECT_NAME, key, null, message.getBytes());
            }

            channel.close();
            connection.close();

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    public static void main(String[] args) {
        consumer(QUEUE_ONE, "消费者[1]", EXCHANGE_DIRECT_NAME, "info");
        consumer(QUEUE_TWO, "消费者[2]", EXCHANGE_DIRECT_NAME, "warn", "error", "debug");
    }

}
