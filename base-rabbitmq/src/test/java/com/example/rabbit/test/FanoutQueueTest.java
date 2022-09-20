package com.example.rabbit.test;

import com.example.rabbit.test.utils.RabbitConnectFactory;
import com.rabbitmq.client.BuiltinExchangeType;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Created  on 2022/9/20 15:15:05
 *
 * @author wmz
 */
@Slf4j
public class FanoutQueueTest extends AbstractQueueTest {

    private static final String QUEUE_ONE = PUBLISH_SUBSCRIBE_QUEUE + ":" + 1;

    private static final String QUEUE_TWO = PUBLISH_SUBSCRIBE_QUEUE + ":" + 2;

    @Test
    @DisplayName("订阅模型(广播):生产者")
    public void fanoutQueueProducerTest() {
        try {
            var connection = RabbitConnectFactory.getConnection();
            var channel = connection.createChannel();
            channel.exchangeDeclare(EXCHANGE_FANOUT_NAME, BuiltinExchangeType.FANOUT);
            var message = "广播消息";
            channel.basicPublish(EXCHANGE_FANOUT_NAME, "", null, message.getBytes());
            channel.close();
            connection.close();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    public static void main(String[] args) {
        consumer(QUEUE_ONE, "消费者[1]", EXCHANGE_FANOUT_NAME, "");
        consumer(QUEUE_TWO, "消费者[2]", EXCHANGE_FANOUT_NAME, "");
    }

}
