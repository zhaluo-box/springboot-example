//package com.zlb.example.bootkafka.config;
//
//import org.apache.kafka.clients.consumer.ConsumerRecord;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.kafka.listener.ConsumerAwareListenerErrorHandler;
//
//import java.util.List;
//
//@Configuration
//public class KafkaErrorHandler {
//    // 新建一个异常处理器，用@Bean注入
////    @Bean
////    public ConsumerAwareListenerErrorHandler consumerAwareErrorHandler() {
////        return (message, exception, consumer) -> {
////            System.out.println("消费异常：" + message.getPayload());
////            return null;
////        };
////    }
//
//
//}
