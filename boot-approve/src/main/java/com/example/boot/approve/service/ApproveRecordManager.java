package com.example.boot.approve.service;

import com.example.boot.approve.entity.config.ApproveModel;
import com.example.boot.approve.entity.runtime.ApproveNodeRecord;

import java.util.List;

/**
 * 审批记录管理
 * Created  on 2022/3/9 11:11:39
 *
 * @author zl
 */
public interface ApproveRecordManager {

    /**
     * 创建审批实例
     *
     * @param paramId 审批参数与发起审批的数据关联 通常为具有唯一性的主键ID
     * @param model   正式启用的审批模板
     * @param reason  发起原因
     */
    void createInstance(long paramId, ApproveModel model, String reason);

    List<ApproveNodeRecord> listApprovedNode(long instanceId);

    List<ApproveNodeRecord> listRejectedAbleNode(long instanceId, long currNodeId, List<ApproveNodeRecord> nodeRecords);
}
