package com.example.boot.base.common.entity.jpa;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 *
 */
@Entity
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Accessors(chain = true)
public class AppPlugin {

    @Id
    @Column(length = 36)
    private String identifier;

    /**
     * 插件名称
     */
    @Column(length = 256)
    private String name;

    /**
     * 描述
     */
    @Column(length = 256)
    private String description;

    /**
     * 版本号
     */
    @Column(length = 32)
    private String version;

    /**
     * Jar包内容
     */
    @Column
    private byte[] jarContent;

    /**
     * 额外的工作配置项
     */
    @Column(columnDefinition = "TEXT")
    private String config;
}
