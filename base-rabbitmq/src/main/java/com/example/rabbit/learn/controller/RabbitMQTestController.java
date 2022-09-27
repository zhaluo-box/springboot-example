package com.example.rabbit.learn.controller;

import com.example.rabbit.learn.common.constant.ExchangeDeclare;
import com.example.rabbit.learn.common.dto.TestMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created  on 2022/9/21 11:11:44
 *
 * @author wmz
 */
@RestController
@RequestMapping("/rabbit-mq-test/")
@Api(tags = "RabbitMQ测试")
public class RabbitMQTestController {

    @Autowired
    private AmqpTemplate amqpTemplate;

    /**
     * 简单队列测试
     */
    @PostMapping("actions/simple-queue/")
    @ApiOperation("RabbitMQ测试：简单队列测试")
    public ResponseEntity<Void> simpleQueue(@RequestBody TestMessage message) {
        amqpTemplate.convertAndSend(ExchangeDeclare.SIMPLE_EXCHANGE, "", message.getData());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * 竞争队列测试
     */

    /**
     * 广播 fanout
     */

    /**
     * 路由 direct route
     */

    /**
     * 通配 Topic
     */

    /**
     * 复杂对象测试
     */

    /**
     * 私信队列测试
     */

    /**
     * confirm 测试
     */

    /**
     * tx 测试
     */
}
