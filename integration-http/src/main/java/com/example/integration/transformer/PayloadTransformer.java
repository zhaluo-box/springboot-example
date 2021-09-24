package com.example.integration.transformer;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import java.io.IOException;
import java.util.Map;

/**
 * Created by MingZhe on 2021/9/9 12:12:20
 *
 * @author wmz
 */
public class PayloadTransformer {

    public Message<Map<String, Object>> transform(Message<String> message) throws IOException {
        var mapper = new ObjectMapper();
        Map<String, Object> newPayload = mapper.readValue(message.getPayload(), new TypeReference<Map<String, Object>>() {
        });
        return MessageBuilder.withPayload(newPayload).copyHeaders(message.getHeaders()).build();
    }
}
