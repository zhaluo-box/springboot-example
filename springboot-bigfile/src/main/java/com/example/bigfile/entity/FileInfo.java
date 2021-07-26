package com.example.bigfile.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table
@Data
@Accessors(chain = true)
@EqualsAndHashCode(of = "contextMD5")
public class FileInfo {

    @Id
    private String id;

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 文件路径
     */
    private String filePath;

    /**
     * 文件总大小
     */
    private long total;

    /**
     * 文件后缀格式
     */
    private String suffix;

    /**
     * 文件摘要
     */
    private String contextMD5;

    /**
     * 用于判断文件是否已经处理
     */
    private boolean processed;

}
