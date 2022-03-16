package com.example.boot.approve.service;

import com.example.boot.approve.entity.runtime.ApproveNodeRecord;

import java.util.List;

/**
 * 审批节点记录服务
 * Created  on 2022/3/15 10:10:09
 *
 * @author zl
 */
public interface ApproveNodeRecordService {
    void batchSave(List<ApproveNodeRecord> nodeRecords);
}
