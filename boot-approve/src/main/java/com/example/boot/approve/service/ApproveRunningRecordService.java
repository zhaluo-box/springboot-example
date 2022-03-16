package com.example.boot.approve.service;

import com.example.boot.approve.entity.runtime.ApproveRunningRecord;

import java.util.List;

/**
 * Created  on 2022/3/15 10:10:14
 *
 * @author zl
 */
public interface ApproveRunningRecordService {
    void batchSave(List<ApproveRunningRecord> runningRecordList);
}
