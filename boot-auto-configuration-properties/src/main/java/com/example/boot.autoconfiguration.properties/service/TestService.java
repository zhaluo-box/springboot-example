package com.example.boot.autoconfiguration.properties.service;

import com.example.boot.autoconfiguration.properties.config.TestConfigProperties;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created  on 2022/3/31 16:16:26
 *
 * @author zl
 */
public class TestService {

    @Autowired
    private TestConfigProperties testConfigProperties;

    public String print() {
        return testConfigProperties.toString();
    }

}
