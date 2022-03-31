package com.example.boot.autoconfiguration.properties.config;

import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created  on 2022/3/31 15:15:20
 *
 * @author zl
 */
@Data
@ToString
public class TestConfigProperties {

    private String desc;

    private List<String> commons;

    private List<UserInfo> userInfos;

    @Data
    @ToString
    public static class UserInfo {

        private String name;

        private String address;

    }

}
