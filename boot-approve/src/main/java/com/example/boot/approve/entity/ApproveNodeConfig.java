package com.example.boot.approve.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
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
public class ApproveNodeConfig extends BaseEntity {

    /**
     * 审批类型
     */
    public enum ApproveType {

        DIRECT, // 直签，配置项对应只有一项
        COUNTERSIGN, // 会签 ，配置项可以多项
        XOF; // 异或审批 ， 配置项可以多项，通常为2项

    }

    /**
     * 配置ID
     */
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
     * 审批节点名称
     */
    private String name;

    /**
     * 审批指定人
     */
    private long assignee;

    /**
     * 审批等级【允许重复】
     */
    private int leave;

    /**
     * 审批类型，目前支持枚举的三种， 默认是直签
     */
    private ApproveType approveType;

    /**
     * 是否支持转办，默认不支持
     */
    private boolean supportTransfer;

    /**
     * 转办人员Id【以数组的形式存放，用逗号 “，”分割】
     */
    private String transferCandidates;

    /**
     * 是否是发起人 【默认不支持转办】
     */
    private boolean initiator;

    /**
     * 权限标识符【权限标识符背后的权限控制，由具体的业务做决定】
     */
    private String permission;

    /**
     * 序号
     */
    private int orderNum;

}
