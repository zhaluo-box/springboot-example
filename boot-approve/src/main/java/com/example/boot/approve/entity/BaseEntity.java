package com.example.boot.approve.entity;

import lombok.Data;

import java.util.Date;

/**
 * Created  on 2022/2/24 17:17:17
 *
 * @author zl
 */
@Data
public class BaseEntity {

    private long createBy;

    private Date createTime;

    private long lastModifyBy;

    private Date lastModifyTime;
}
