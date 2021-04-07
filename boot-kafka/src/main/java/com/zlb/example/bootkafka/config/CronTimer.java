//package com.zlb.example.bootkafka.config;
//
//import org.apache.kafka.clients.consumer.ConsumerRecord;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
//import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
//import org.springframework.kafka.core.ConsumerFactory;
//import org.springframework.scheduling.annotation.EnableScheduling;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
///**
// * 6、定时启动、停止监听器
// * 默认情况下，当消费者项目启动的时候，监听器就开始工作，监听消费发送到指定topic的消息，
// * 那如果我们不想让监听器立即工作，想让它在我们指定的时间点开始工作，或者在我们指定的时间点停止工作，
// * 该怎么处理呢——使用KafkaListenerEndpointRegistry，下面我们就来实现：
// * <p>
// * ① 禁止监听器自启动；
// * ② 创建两个定时任务，一个用来在指定时间点启动定时器，另一个在指定时间点停止定时器；
// * <p>
// * 新建一个定时任务类，用注解@EnableScheduling声明，KafkaListenerEndpointRegistry
// * 在SpringIO中已经被注册为Bean，直接注入，设置禁止KafkaListener自启动
// * ————————————————
// * 版权声明：本文为CSDN博主「Felix-Yuan」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。
// * 原文链接：https://blog.csdn.net/yuanlong122716/article/details/105160545
// */
//@EnableScheduling
//@Component
//public class CronTimer {
//
//    /**
//     * @KafkaListener注解所标注的方法并不会在IOC容器中被注册为Bean， 而是会被注册在KafkaListenerEndpointRegistry中，
//     * 而KafkaListenerEndpointRegistry在SpringIOC中已经被注册为Bean
//     **/
//    @Autowired
//    private KafkaListenerEndpointRegistry registry;
//
//    @Autowired
//    private ConsumerFactory consumerFactory;
//
////    // 监听器容器工厂(设置禁止KafkaListener自启动)
////    @Bean
////    public ConcurrentKafkaListenerContainerFactory delayContainerFactory() {
////        ConcurrentKafkaListenerContainerFactory container = new ConcurrentKafkaListenerContainerFactory();
////        container.setConsumerFactory(consumerFactory);
////        //禁止KafkaListener自启动
////        container.setAutoStartup(false);
////        return container;
////    }
//
//    // 监听器
////    @KafkaListener(id = "timingConsumer", topics = "topic1", containerFactory = "delayContainerFactory")
////    public void onMessage1(ConsumerRecord<?, ?> record) {
////        System.out.println("消费成功：" + record.topic() + "-" + record.partition() + "-" + record.value());
////    }
//
////    // 定时启动监听器
////    @Scheduled(cron = "0 42 11 * * ? ")
////    public void startListener() {
////        System.out.println("启动监听器...");
////        // "timingConsumer"是@KafkaListener注解后面设置的监听器ID,标识这个监听器
////        if (!registry.getListenerContainer("timingConsumer").isRunning()) {
////            registry.getListenerContainer("timingConsumer").start();
////        }
////        //registry.getListenerContainer("timingConsumer").resume();
////    }
////
////    // 定时停止监听器
////    @Scheduled(cron = "0 45 11 * * ? ")
////    public void shutDownListener() {
////        System.out.println("关闭监听器...");
////        registry.getListenerContainer("timingConsumer").pause();
////    }
//}
