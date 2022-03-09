package com.example.boot.approve.entity.config;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 审批人配置
 * Created  on 2022/3/8 21:21:27
 *
 * @author zl
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(of = "id")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApproveAssigneeConfig {

    /**
     * 主键Id
     */
    private Long id;

    /**
     * 指定审批人
     */
    private long assignee;

    /**
     * 审批配置节点Id
     */
    private long approveNodeId;

    /**
     * 序号
     */
    private int orderNum;

    /**
     * 权限标识符【权限标识符背后的权限控制，由具体的业务做决定】
     */
    private String permission;

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

}
