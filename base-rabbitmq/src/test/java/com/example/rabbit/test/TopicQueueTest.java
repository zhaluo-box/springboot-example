package com.example.rabbit.test;

import com.example.rabbit.test.utils.RabbitConnectFactory;
import com.rabbitmq.client.BuiltinExchangeType;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

/**
 * 通配 TOPIC 队列测试
 * Created  on 2022/9/20 16:16:55
 *
 * @author wmz
 */

@Slf4j
public class TopicQueueTest extends AbstractQueueTest {

    private static final String QUEUE_ONE = TOPIC_QUEUE + ":" + 1;

    private static final String QUEUE_TWO = TOPIC_QUEUE + ":" + 2;

    private static final String QUEUE_THREE = TOPIC_QUEUE + ":" + 3;

    @Test
    @DisplayName("交换机根据路由Key匹配投递")
    public void directQueueProducerTest() {
        try (var connection = RabbitConnectFactory.getConnection(); var channel = connection.createChannel();) {
            channel.exchangeDeclare(EXCHANGE_TOPIC_NAME, BuiltinExchangeType.TOPIC);
            List<String> keys = Arrays.asList("test.info", "test.warn", "test.error", "test.debug", "test.trace.level");
            for (String key : keys) {
                var message = "路由Key定向投送消息" + key;
                channel.basicPublish(EXCHANGE_TOPIC_NAME, key, null, message.getBytes());
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    public static void main(String[] args) {
        // 匹配下一个单词
        consumer(QUEUE_ONE, "消费者[1](*)", EXCHANGE_TOPIC_NAME, "test.*");
        // 精确匹配
        consumer(QUEUE_TWO, "消费者[2]", EXCHANGE_TOPIC_NAME, "test.warn", "test.error");
        // # 匹配多个词
        consumer(QUEUE_THREE, "消费者[3](#)", EXCHANGE_TOPIC_NAME, "test.#");
    }

}
