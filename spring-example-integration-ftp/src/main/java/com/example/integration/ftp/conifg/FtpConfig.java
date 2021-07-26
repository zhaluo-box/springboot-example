package com.example.integration.ftp.conifg;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.file.remote.session.CachingSessionFactory;
import org.springframework.integration.file.remote.session.Session;
import org.springframework.integration.file.remote.session.SessionFactory;
import org.springframework.integration.ftp.session.DefaultFtpSessionFactory;

@Configuration
public class FtpConfig {

//    @Bean
//    public SessionFactory sessionFactory() {
//        DefaultFtpSessionFactory defaultFtpSessionFactory = new DefaultFtpSessionFactory();
//        defaultFtpSessionFactory.setHost("192.168.13.129");
//        defaultFtpSessionFactory.setPort(60021);
//        defaultFtpSessionFactory.setUsername("fftp");
//        defaultFtpSessionFactory.setPassword("zxcff1234");
//        CachingSessionFactory factory = new CachingSessionFactory(defaultFtpSessionFactory);
//        Session session = factory.getSession();
//        factory.resetCache();
//
//        return factory;
//    }



}
