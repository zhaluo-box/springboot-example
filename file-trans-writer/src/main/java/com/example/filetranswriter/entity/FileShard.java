package com.example.filetranswriter.entity;

import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(of = {"key", "fileId", "id"})
public class FileShard {

    private String id;

    /**
     * 源文件ID
     */
    @Column(name = "file_id")
    private String fileId;

    /**
     * 文件CRC 值
     */
    private long key;

    /**
     * 分片文件名
     */
    @Column(name = "shard_name")
    private String shardName;

    /**
     * 分片路径
     */
    @Column(name = "shard_path")
    private String shardPath;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 是否已删除
     */
    @Column(name = "is_deleted")
    private String isDeleted;

    /**
     * 分片大小
     */
    @Column(name = "shard_size")
    private long shardSize;

    /**
     * 分片序号
     */
    @Column(name = "shard_sequence")
    private int shardSequence;


}
