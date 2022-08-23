package com.example.boot.mybatis.plus.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * Created  on 2022/2/10 22:22:08
 *
 * @author zl
 */
@Data
@ToString
@Accessors(chain = true)
@TableName(value = "user", autoResultMap = true)
public class User {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;

    private Integer age;

    private String email;
}
