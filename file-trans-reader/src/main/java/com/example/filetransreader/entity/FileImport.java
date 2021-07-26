package com.example.filetransreader.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(of = {"key", "id"})
public class FileImport implements Serializable {

    private static final long serialVersionUID = -2977691544859575640L;

    /**
     * 文件名
     */
    private String filename;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 文件名 MD5 值
     */
    private String id;

    private String sourcePath;

    /**
     * 是否已处理
     */
    private String isProcessed;

    /**
     * 是否分片 Y 分片 N-不分片
     */
    private String shardFlag;

    /**
     * 分片数量
     */
    private int shardNum;

    /**
     * 文件CRC值
     */
    private long key;

    /**
     * 文件大小
     */
    private long fileSize;

    /**
     * 确认标识,代表已合并; Y: writer端已确认, N: 未确认 [默认值]
     */
    private String confirm;

}
