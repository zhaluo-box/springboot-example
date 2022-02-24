package com.example.boot.approve.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 审批模板
 * Created  on 2022/2/24 17:17:06
 *
 * @author zl
 */

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApproveModel extends BaseEntity {

    /**
     * ID
     */
    private long id;

    /**
     * 模板名称
     */
    private String name;

    /**
     * 描述
     */
    private String description;

    /**
     * 版本号
     */
    private int version;

    /**
     * 默认禁用
     * 启用禁用 true : 禁用，false : 启用
     */
    private boolean disabled = true;


}
