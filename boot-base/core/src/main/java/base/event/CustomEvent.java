package com.example.boot.base.event;

import lombok.*;
import org.springframework.context.ApplicationEvent;

import java.time.Clock;

/**
 * Created by MingZhe on 2021/9/28 15:15:35
 *
 * @author wmz
 */

public class CustomEvent extends ApplicationEvent {

    @Getter
    @Setter
    private String message;

    public CustomEvent(Object source) {
        super(source);
    }

    public CustomEvent(Object source, Clock clock) {
        super(source, clock);
    }
}
