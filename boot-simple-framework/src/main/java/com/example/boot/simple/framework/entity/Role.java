package com.example.boot.simple.framework.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 角色
 * Created  on 2022/2/21 21:21:57
 *
 * @author zl
 */
@Data
@Table
@Entity
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Role {

    @Id
    private Integer identifier;

    /**
     * 名称
     */
    @Column(length = 64)
    private String name;

    /**
     * 描述
     */
    @Column(length = 255)
    private String description;

    /**
     * 启用，禁用
     */
    @Column
    private boolean disabled;

}
