package com.example.integration.ftp;

import org.apache.commons.net.ftp.FTPFile;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.*;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.file.FileHeaders;
import org.springframework.integration.file.FileNameGenerator;
import org.springframework.integration.file.remote.session.CachingSessionFactory;
import org.springframework.integration.file.remote.session.SessionFactory;
import org.springframework.integration.file.support.FileExistsMode;
import org.springframework.integration.ftp.dsl.Ftp;
import org.springframework.integration.ftp.outbound.FtpMessageHandler;
import org.springframework.integration.ftp.session.DefaultFtpSessionFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.test.context.ContextConfiguration;

import java.io.File;
import java.lang.annotation.Annotation;

@SpringBootApplication
//@ContextConfiguration({"classpath:ftp/ftp.xml"})
@IntegrationComponentScan
public class FtpApplication {



    public static void main(String[] args) {
        ConfigurableApplicationContext context =
                new SpringApplicationBuilder(FtpApplication.class)
                        .web(WebApplicationType.NONE)
                        .run(args);
        MyGateway gateway = context.getBean(MyGateway.class);
        gateway.sendToFtp(new File("/home/zhauo/桌面/spring-integration.rtf"));
    }

    @Bean
    public SessionFactory<FTPFile> ftpSessionFactory() {
        DefaultFtpSessionFactory defaultFtpSessionFactory = new DefaultFtpSessionFactory();
        defaultFtpSessionFactory.setHost("192.168.13.129");
        defaultFtpSessionFactory.setPort(60021);
        defaultFtpSessionFactory.setUsername("fftp");
        defaultFtpSessionFactory.setPassword("zxcff1234");
        return new CachingSessionFactory(defaultFtpSessionFactory, 5);
//        sf.setTestSession(true);
    }

//    @Bean
//    @ServiceActivator(inputChannel = "toFtpChannel")
//    public MessageHandler handler() {
//        FtpMessageHandler handler = new FtpMessageHandler(ftpSessionFactory());
////        handler.setRemoteDirectoryExpressionString("headers['remote-target-dir']");
//        handler.setAutoCreateDirectory(true);
//        handler.setRemoteDirectoryExpressionString("'/test/file/temp/'");
//        handler.setFileNameGenerator(new FileNameGenerator() {
//            @Override
//            public String generateFileName(Message<?> message) {
//                return "xxxxxxxxx";
//            }
//        });
//        return handler;
//    }




    @MessagingGateway
    public interface MyGateway {

        @Gateway(requestChannel = "toFtpChannel")
        void sendToFtp(File file);

    }


    @Bean
    public IntegrationFlow ftpOutboundFlow() {
        return IntegrationFlows.from("toFtpChannel")
                .handle(Ftp.outboundAdapter(ftpSessionFactory(), FileExistsMode.REPLACE)
                        .useTemporaryFileName(false)
                        .autoCreateDirectory(true)
//                        .fileNameExpression("headers['" + FileHeaders.FILENAME + "']")
                        .remoteDirectory("/test/ftp/123/")
                ).get();
    }



}
