package com.example.redis.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class User implements Serializable {

    private static final long serialVersionUID = 6372380584473177016L;

    private String name;

    private int age;

    private double weight;
}
