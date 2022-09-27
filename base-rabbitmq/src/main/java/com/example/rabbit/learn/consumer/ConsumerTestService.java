package com.example.rabbit.learn.consumer;

import com.example.rabbit.learn.common.constant.ExchangeDeclare;
import com.example.rabbit.learn.common.constant.QueueDeclare;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

/**
 * Created  on 2022/9/21 12:12:24
 *
 * @author wmz
 */
@Slf4j
@Service
public class ConsumerTestService {

    @RabbitListener(bindings = @QueueBinding(value = @Queue(value = QueueDeclare.SIMPLE_QUEUE), exchange = @Exchange(value = ExchangeDeclare.SIMPLE_EXCHANGE), key = ""))
    public void simpleQueueLister(String message) {
        log.info("简单消息测试: 消费者[1],消息 ： {}", message);
    }

    @RabbitListener(bindings = @QueueBinding(value = @Queue(value = QueueDeclare.SIMPLE_QUEUE), exchange = @Exchange(value = ExchangeDeclare.SIMPLE_EXCHANGE), key = ""))
    public void simpleQueueLister2(String message) {
        log.info("简单消息测试: 消费者[2],消息 ： {}", message);
    }

    //    //        @RabbitListener(bindings = @QueueBinding(value = @Queue(value = QueueDeclare.WORK_QUEUE), exchange = @Exchange(value = ExchangeDeclare.WORK_EXCHANGE), key = ""))
    //    @RabbitListener(queues = QueueDeclare.WORK_QUEUE)
    //    public void workQueueLister(String message) {
    //        log.info("work-queue消息测试: 消费者[1],消息 ： {}", message);
    //    }
    //
    //    //    @RabbitListener(bindings = @QueueBinding(value = @Queue(value = QueueDeclare.WORK_QUEUE), exchange = @Exchange(value = ExchangeDeclare.WORK_EXCHANGE), key = ""))
    //    @RabbitListener(queues = QueueDeclare.WORK_QUEUE)
    //    public void workQueueLister2(String message) {
    //        log.info("work-queue简单消息测试: 消费者[2],消息 ： {}", message);
    //    }
}
