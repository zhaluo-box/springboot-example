package com.example.integration.transformer;

import org.springframework.integration.http.multipart.UploadedMultipartFile;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by MingZhe on 2021/9/9 15:15:22
 *
 * @author wmz
 */
public class CommonTransformer {

    public Message<?> transform(Message<?> message) {

        Object payload = message.getPayload();

        return MessageBuilder.withPayload(payload).copyHeaders(message.getHeaders()).build();
    }
}
