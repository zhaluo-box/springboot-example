package com.example.boot.mybatis.plus.entiry;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.apache.ibatis.type.JdbcType;

import java.util.Date;

/**
 * Created  on 2022/2/10 22:22:08
 *
 * @author zl
 */
@Data
// TODO　验证schema 结合filter 实现schema 切换
@TableName("user")
public class User {

    @TableId(type = IdType.AUTO)
    private Integer id;

    @TableField(value = "name", jdbcType = JdbcType.VARCHAR)
    private String name;

    private int age;

    private int tel;

    private Date create_time;

    private Date update_time;

    private int version;

}
