package com.example.boot.approve.entity.common;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Created  on 2022/2/24 17:17:17
 *
 * @author zl
 */
@Setter
@Getter
public class BaseEntity {

    private long createBy;

    private Date createTime;

    private long lastModifyBy;

    private Date lastModifyTime;
}
