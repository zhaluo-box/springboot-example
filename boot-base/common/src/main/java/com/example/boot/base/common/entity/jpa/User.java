package com.example.boot.base.common.entity.jpa;

import com.example.boot.base.common.converts.MapConvert;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Table
@Entity
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Accessors(chain = true)
public class User {

    public enum SexType {
        MAN("男"), WOMAN("女");

        @Getter
        private String value;

        SexType(String value) {
        }
    }

    @Id
    private String id;

    /**
     * 姓名
     */
    @Column
    private String username;

    /**
     * 年龄
     */
    @Column
    private int age;

    /**
     * 性别
     */
    @Column
    @Enumerated(EnumType.STRING)
    private SexType sex;

    /**
     * 地址
     */
    private String address;

    /**
     * 电话
     */
    private String telephone;

    /**
     * 身高
     */
    private BigDecimal height;

    /**
     * 体重
     */
    private BigDecimal weight;

    @Convert(converter = MapConvert.class)
    private Map<String, String> hobby = new HashMap<>();
}
