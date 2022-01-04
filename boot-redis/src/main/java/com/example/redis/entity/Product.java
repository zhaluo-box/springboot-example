package com.example.redis.entity;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class Product {

    private String name;

    /**
     * 生产日期
     */
    private Date createtime;

    private String address;

    private List<Consumer> consumerList;

    /**
     * 保质期
     */
    private Integer days;
}
