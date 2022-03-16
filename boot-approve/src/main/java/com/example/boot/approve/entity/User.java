package com.example.boot.approve.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Created  on 2022/3/16 11:11:00
 *
 * @author zl
 */
@Data
@AllArgsConstructor
@Accessors(chain = true)
public class User {

    private Long id;

    private String name;

}
