package com.example.boot.approve.entity;

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
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApproveRunningHistory extends ApproveNodeConfig {

    /**
     * 审批意见
     */
    public enum ApproveOpinion {
        APPROVED,//审批通过
        UN_APPROVED,//审批不通过
        REJECTED,//驳回
        CANCELED,//撤销
        INVALID //作废【整个流程审批通过之后才会出现作废按钮， 逻辑归档】
    }

    /**
     * 转办人
     */
    private long transfer;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 审批状态
     */
    private ApproveResult approveResult;

    /**
     * 转办标记
     */
    private boolean transferMark;

}
