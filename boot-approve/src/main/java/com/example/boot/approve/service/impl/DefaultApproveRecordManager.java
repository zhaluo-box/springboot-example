package com.example.boot.approve.service.impl;

import com.example.boot.approve.entity.config.ApproveModel;
import com.example.boot.approve.service.ApproveConfigManager;
import com.example.boot.approve.service.ApproveRecordManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 审批记录管理
 * Created  on 2022/3/9 13:13:17
 *
 * @author zl
 */
@Service
public class DefaultApproveRecordManager implements ApproveRecordManager {

    @Autowired
    private ApproveConfigManager approveConfigManager;

    @Override
    public void createInstance(long paramId, ApproveModel modelName) {

        // 根据最新的审批模板直接生成所有的节点记录与审批记录
        
        // 对于审批人是发起人的做特殊处理

    }
}
