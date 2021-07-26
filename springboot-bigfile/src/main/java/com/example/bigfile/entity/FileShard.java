package com.example.bigfile.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Data
@Entity
@Table
@EqualsAndHashCode(of = "contextMD5")
public class FileShard {

    @Id
    private String id;

    /**
     * 文件的ID
     */
    private String fileId;

    /**
     * 排序id
     */
    private long sortId;

    /**
     * 当前分片的名称
     */
    private String name;

    /**
     * 当前分片大小
     */
    private int size;

    /**
     * 文件摘要
     */
    private String contextMD5;

    /**
     * 用于判断文件是否已经处理
     */
    private boolean processed;
}
