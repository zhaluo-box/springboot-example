package com.example.boot.approve.service.impl;

import com.example.boot.approve.service.ApproveService;
import org.springframework.stereotype.Service;

/**
 * Created  on 2022/3/8 15:15:53
 *
 * @author wmz
 */
@Service
public class DefaultApproveService implements ApproveService {

    @Override
    public void approve(long instanceId, String remark) {

        // 获取当前登录人信息

        // 通过审批实例获取审批节点等级， 通过审批节点等级精确定位审批历史记录
        // 如果无法定位具体记录，就检查当前人员是否为转办人员
        // 检查是否已审批避免重复提交， 默认审批实例添加的时候，所有审批历史都新增，

        // 直签

        // 异或

        // 会签

        // 通知下一级审批人
    }

    @Override
    public void refuse(long instanceId, String remark) {

    }

    @Override
    public void cancel(long instanceId, String remark) {

    }

    @Override
    public void reject(long instanceId, String remark, int level, long assignee) {

    }

    @Override
    public void transform(long instanceId, String remark, long transfer) {

    }
}
