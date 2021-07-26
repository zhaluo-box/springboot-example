package com.example.filetranswriter.event;

import lombok.AllArgsConstructor;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@AllArgsConstructor
@Getter
@Setter
public class MergeFileEvent extends ApplicationEvent {

    private String fileId;

    public MergeFileEvent(Object source) {
        super(source);
    }
}
