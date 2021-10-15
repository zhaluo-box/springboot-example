package com.example.integration.http;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

@SpringBootTest
class IntegrationHttpApplicationTests {

    @Test
    void contextLoads() throws JsonProcessingException {
        String content = "{\"name\":\"zhang San\"}";
        var mapper = new ObjectMapper();
        Map<String, Object> jsonMap = mapper.readValue(content, new TypeReference<>() {
        });
        System.out.println(jsonMap);
    }

}
