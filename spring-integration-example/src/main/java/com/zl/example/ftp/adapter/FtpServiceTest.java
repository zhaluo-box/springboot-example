package com.zl.example.ftp.adapter;

import org.apache.commons.net.ftp.FTPFile;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.*;
import org.springframework.integration.file.FileNameGenerator;
import org.springframework.integration.file.remote.session.CachingSessionFactory;
import org.springframework.integration.file.remote.session.SessionFactory;
import org.springframework.integration.ftp.outbound.FtpMessageHandler;
import org.springframework.integration.ftp.session.DefaultFtpSessionFactory;
import org.springframework.integration.transformer.StreamTransformer;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;

import java.io.File;

/**
 * @Author: zl
 * @Description:
 * @Date: 2021/6/28 14:27
 */
@Configuration
public class FtpServiceTest {

    @Bean
    public SessionFactory<FTPFile> ftpSessionFactory() {
        DefaultFtpSessionFactory sf = new DefaultFtpSessionFactory();
        sf.setHost("192.168.2.34");
        sf.setPort(21);
        sf.setUsername("nest-ftp");
        sf.setPassword("123456");
        //        sf.setTestSession(true);
        return new CachingSessionFactory<FTPFile>(sf);
    }

    @Bean
    @ServiceActivator(inputChannel = "data")
    public MessageHandler handler() {
        FtpMessageHandler handler = new FtpMessageHandler(ftpSessionFactory());
        //        handler.setRemoteDirectoryExpressionString("headers['remote-target-dir']");
        handler.setRemoteDirectoryExpressionString("'/ftpadapter/'");
        handler.setFileNameGenerator(new FileNameGenerator() {
            @Override
            public String generateFileName(Message<?> message) {
                return "custom-filename.txt";
            }
        });
        return handler;
    }


    @Bean
    @Transformer(inputChannel = "toFtpChannel", outputChannel = "data")
    public StreamTransformer streamToBytes() {
        return new StreamTransformer(); // transforms to byte[]
    }


    @MessagingGateway
    public interface MyGateway {
        @Gateway(requestChannel = "toFtpChannel")
        void sendToFtp(String  file);

    }

}
