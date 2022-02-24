package com.example.boot.approve.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * 审批配置与审批模板关联
 * Created  on 2022/2/24 17:17:09
 *
 * @author zl
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApproveConfig extends BaseEntity {

    /**
     * 审批类型
     */
    public enum ApproveType {

        DIRECT("直接审批"), COUNTERSIGN("会签"), XOF("异或审批");

        @Getter
        private String value;

        ApproveType(String value) {
            this.value = value;
        }
    }

    private long id;

    /**
     * 审批模板Id
     */
    private long approveModelId;

    /**
     * 审批模板版本号
     */
    private int approveModelVersion;

    /**
     * 审批标题， 通常与部门名称相同
     */
    private String title;

    /**
     * 指定人
     */
    private long assignee;

    /**
     * 部门Id 前端自动转为部门名称 部门数据进行缓存
     */
    private long departmentId;

    /**
     * 审批等级
     */
    private int leave;

    /**
     * 审批类型，目前支持枚举的三种
     */
    private ApproveType approveType;

}
