package com.example.integration.serviceactivator;

import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;


public class HttpResponseServiceActivator {

    public Message<String> handle(Message<String> message) {
        var payload = message.getPayload();
        return MessageBuilder.withPayload(payload).copyHeaders(message.getHeaders()).build();
    }

}
