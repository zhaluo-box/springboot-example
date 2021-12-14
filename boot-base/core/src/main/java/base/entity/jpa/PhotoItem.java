package com.example.boot.base.entity.jpa;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table
@Data
@Accessors(chain = true)
public class PhotoItem {

    @Id
    private String id;

    /**
     * 文件名称
     */
    private String name;

    /**
     * 文件大小
     */
    private Long size;

    /**
     * 存放路径
     */
    private String uri;

    /**
     * 标识
     */
    private String tag;

    /**
     * 所属相册
     */
    private String AlbumId;

    /**
     * 默认允许下载!
     */
    private Boolean isAllowDownload = true;

    /**
     * 删除时间
     */
    private Date deleteTime;

    /**
     * 是否删除, 物理删除
     */
    private Boolean deleted;

    @CreatedBy
    private String createUser;

    @CreatedDate
    private Date uploadTime;

    @LastModifiedBy
    private String modifyUser;

    @LastModifiedDate
    private Date lastModifyTime;

}
