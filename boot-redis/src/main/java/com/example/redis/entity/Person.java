package com.example.redis.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * Title:       [redis示例 — entity类]
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Person implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    private String name;

    private String age;

}
