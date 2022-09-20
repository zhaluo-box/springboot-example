package com.example.rabbit.test;

import com.example.rabbit.test.utils.RabbitConnectFactory;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

/**
 * 工作队列测试 又名竞争队列
 * 工作队列与普通队列几乎无差别 只是需要注意使用 basicQos 规定每次 prefetchCount 的数量
 * Created  on 2022/9/20 13:13:31
 *
 * @author wmz
 */
@Slf4j
public class WorkQueueTest extends AbstractQueueTest {

    @Test
    @DisplayName("工作队列测试: 生产者")
    public void workQueueProducerTest() {
        try (var connection = RabbitConnectFactory.getConnection(); var channel = connection.createChannel();) {
            log.info("简单消息队列，消息发送开始！");
            // durable ： 持久化
            // exclusive : 独占
            // autoDelete : 服务器不再使用的时候自动删除
            channel.queueDeclare(AbstractQueueTest.WORK_QUEUE, true, false, false, null);
            for (int i = 0; i < 10; i++) {
                var message = "工作消息队列测试" + i;
                channel.basicPublish("", AbstractQueueTest.WORK_QUEUE, null, message.getBytes(StandardCharsets.UTF_8));
            }
            log.info("简单消息队列，消息发送完毕！");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @SneakyThrows
    public static void main(String[] args) {
        consumer(AbstractQueueTest.WORK_QUEUE, "消费者[1]: ", false);
        consumer(AbstractQueueTest.WORK_QUEUE, "消费者[2]: ", true);
    }

}
