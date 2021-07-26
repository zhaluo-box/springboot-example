package com.example.filetranswriter.publish;

import com.example.filetranswriter.event.MergeFileEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class MergePublish {
    @Autowired
    private ApplicationContext applicationContext;

    public void publish(String filed) {
        applicationContext.publishEvent(new MergeFileEvent(filed));
    }
}
