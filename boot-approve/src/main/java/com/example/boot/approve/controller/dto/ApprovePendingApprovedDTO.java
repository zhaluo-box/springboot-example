package com.example.boot.approve.controller.dto;

import com.example.boot.approve.entity.runtime.ApproveRunningRecord;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 待我审批记录
 * Created  on 2022/3/17 11:11:19
 *
 * @author zl
 */
@Data
@Accessors(chain = true)
public class ApprovePendingApprovedDTO extends ApproveRunningRecord {

    /**
     * 实例名称继承自模板名称 可以通过下拉选择进行筛选
     */
    private String instanceName;

    /**
     * 发起者的名称
     */
    private String initiateName;

    /**
     * 发起的原因
     */

}
