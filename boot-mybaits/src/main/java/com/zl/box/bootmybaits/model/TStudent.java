package com.zl.box.bootmybaits.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "t_student")
public class TStudent implements Serializable {

    private static final long serialVersionUID = -3013348021001693515L;

    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private String sex;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 图片
     */
    private String image;

    /**
     * 地址
     */
    private String address;

}
