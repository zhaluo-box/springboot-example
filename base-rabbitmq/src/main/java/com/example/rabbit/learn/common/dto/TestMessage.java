package com.example.rabbit.learn.common.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * Created  on 2022/9/21 11:11:54
 *
 * @author wmz
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TestMessage {

    private String data;

}
