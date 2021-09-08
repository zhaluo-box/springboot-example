package com.zl.example;


import com.rometools.rome.feed.synd.SyndEntry;
import com.zl.example.ftp.adapter.FtpServiceTest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
//import org.springframework.integration.dsl.channel.MessageChannels;
//import org.springframework.integration.dsl.core.Pollers;
//import org.springframework.integration.dsl.file.Files;
//import org.springframework.integration.dsl.mail.Mail;
import org.springframework.integration.feed.inbound.FeedEntryMessageSource;
import org.springframework.integration.file.support.FileExistsMode;
import org.springframework.integration.scheduling.PollerMetadata;

import java.io.File;
import java.io.IOException;

import static java.lang.System.getProperty;

@SpringBootApplication
@IntegrationComponentScan
public class boot {

    public static void main(String[] args) {
//        ConfigurableApplicationContext context = SpringApplication.run(boot.class);
        ConfigurableApplicationContext context =
                        new SpringApplicationBuilder(boot.class)
//                                        .web(false)
                                        .run(args);
        FtpServiceTest.MyGateway gateway = context.getBean(FtpServiceTest.MyGateway.class);
        gateway.sendToFtp("xxxxxx.txt");
    }

//    @Value("https://spring.io/blog.atom") // 1 通过注解自动获取url的资源
//    private Resource resource;
//
//    @Bean(name = PollerMetadata.DEFAULT_POLLER)
//    public PollerMetadata poller() { // 2 使用fluent API 和 pollers 配置默认的轮训方式
//        return Pollers.fixedRate(500).get();
//    }
//
//    /**
//     * 构造入栈通道适配器.
//     *
//     * @return
//     * @throws IOException
//     */
//    @Bean
//    public FeedEntryMessageSource feedMessageSource() throws IOException { //3
//        FeedEntryMessageSource messageSource = new FeedEntryMessageSource(resource.getURL(), "news");
//        return messageSource;
//    }
//
//    /**
//     * @return
//     * @throws IOException
//     */
//    @Bean
//    public IntegrationFlow myFlow() throws IOException {
//        return IntegrationFlows.from(feedMessageSource()) //4 流程从from 方法开始
//                .<SyndEntry, String>route(payload -> payload.getCategories().get(0).getName(),//5 通过路由方法由route来选择路由
//                        mapping -> mapping.channelMapping("releases", "releasesChannel") //6 通过不同分类的值 转向不同的消息通道
//                                .channelMapping("engineering", "engineeringChannel")
//                                .channelMapping("news", "newsChannel"))
//
//                .get(); // 7
//    }
//
//    @Bean
//    public IntegrationFlow releasesFlow() {
//        return IntegrationFlows.from(MessageChannels.queue("releasesChannel", 10)) //1 从消息通道releaseChannel 获取消息数据
//
//                .<SyndEntry, String>transform(
//                        payload -> "《" + payload.getTitle() + "》 " + payload.getLink() + getProperty("line.separator")) //2 消息转换
//                .handle(Files.outboundAdapter(new File("/home/zhauo/dev_software/workspace/my-hair2/springboot-example")) //3
//                        .fileExistsMode(FileExistsMode.APPEND) //4
//                        .charset("UTF-8") //5
//                        .fileNameGenerator(message -> "releases.txt") //6
//                        .get())
//                .get();
//    }
//
//    @Bean
//    public IntegrationFlow engineeringFlow() {
//        return IntegrationFlows.from(MessageChannels.queue("engineeringChannel", 10))
//                .<SyndEntry, String>transform(
//                        payload -> "《" + payload.getTitle() + "》 " + payload.getLink() + getProperty("line.separator"))
//                .handle(Files.outboundAdapter(new File("/home/zhauo/dev_software/workspace/my-hair2/springboot-example"))
//                        .fileExistsMode(FileExistsMode.APPEND)
//                        .charset("UTF-8")
//                        .fileNameGenerator(message -> "engineering.txt")
//                        .get())
//                .get();
//    }
//
//    @Bean
//    public IntegrationFlow newsFlow() {
//        return IntegrationFlows.from(MessageChannels.queue("newsChannel", 10))
//                .<SyndEntry, String>transform(
//                        payload -> "《" + payload.getTitle() + "》 " + payload.getLink() + getProperty("line.separator"))
//                .enrichHeaders( //1
//                        Mail.headers()
//                                .subject("来自Spring的新闻")
//                                .to("zhaluobox@163.com")
//                                .from("zhaluobox@163.com"))
//                .handle(Mail.outboundAdapter("smtp.136.com") //2
//                        .port(25)
//                        .protocol("smtp")
//                        .credentials("zhaluobox@163.com", "xxxxxxxxxxx")
//                        .javaMailProperties(p -> p.put("mail.debug", "false")), e -> e.id("smtpOut"))
//                .get();
//    }

}
