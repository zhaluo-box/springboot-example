package com.example.boot.base.common.dto.event;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 *
 */
@Data
@Accessors(chain = true)
public class TestMessageEvent implements Serializable {

    private int size;

    private String message;
}
