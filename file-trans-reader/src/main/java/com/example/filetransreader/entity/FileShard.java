package com.example.filetransreader.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(of = {"key", "fileId"})
public class FileShard {

    private String id;

    /**
     * 源文件ID
     */
    private String fileId;

    /**
     * 文件CRC 值
     */
    private long key;

    /**
     * 分片文件名
     */
    private String shardName;

    /**
     * 分片路径
     */
    private String shardPath;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 是否已删除
     */
    private String isDeleted;

    /**
     * 分片大小
     */
    private long shardSize;

    /**
     * 分片序号
     */
    private int shardSequence;


}
