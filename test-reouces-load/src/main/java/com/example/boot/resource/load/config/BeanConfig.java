package com.example.boot.resource.load.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;

import java.io.IOException;

/**
 * Created  on 2023/7/31 09:9:31
 *
 * @author wmz
 */
@Configuration
public class BeanConfig {

    @Bean
    public String hello() throws IOException {
        java.io.InputStream inputStream = ResourceUtils.getURL("classpath:otf/SourceHanSerifSC-Bold.otf").openStream();

        System.out.println("加载OTF 文件成功！");
        inputStream.close();
        return "hello";
    }
}
