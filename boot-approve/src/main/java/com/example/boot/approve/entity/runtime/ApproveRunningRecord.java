package com.example.boot.approve.entity.runtime;

import com.example.boot.approve.enums.ApproveResult;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 审批历史 与审批配置基本上相同 多了转办人员， 转办Id
 * Created  on 2022/2/24 17:17:24
 *
 * @author zl
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(of = "id")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApproveRunningRecord {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 审批节点记录ID
     */
    private long nodeRecordId;

    /**
     * 审批指定人
     */
    private long assignee;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 审批结果
     */
    private ApproveResult result;

    /**
     * 权限标识符【具体能够做什么操作由具体业务限定】
     */
    private String permission;

    /**
     * 排序号
     */
    private int orderNum;

    /**
     * 转办标记
     */
    private boolean transferMark;

    /**
     * 转办人 [可以为空] 只能转办给一个人
     */
    private long transfer;

    /**
     * 驳回节点ID
     */
    private long rejectNodeId;

}
