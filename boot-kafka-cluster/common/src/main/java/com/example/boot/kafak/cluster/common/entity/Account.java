package com.example.boot.kafak.cluster.common.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Account implements Serializable {

    /**
     * id
     */
    private String identifier;

    /**
     * 名称
     */
    private String name;

    /**
     * 卡号
     */
    private String IdCard;

    /**
     * 地址
     */
    private String address;

}
