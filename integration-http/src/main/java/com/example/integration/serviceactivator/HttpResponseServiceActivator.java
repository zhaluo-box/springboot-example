package com.example.integration.serviceactivator;

import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import java.util.Locale;

public class HttpResponseServiceActivator {

    public Message<String> handle(Message<String> message) {

        var payload = message.getPayload();

        return MessageBuilder.withPayload(payload.toUpperCase(Locale.ROOT)).copyHeaders(message.getHeaders()).build();
    }

}
