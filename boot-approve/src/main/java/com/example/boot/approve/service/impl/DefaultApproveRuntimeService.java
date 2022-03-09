package com.example.boot.approve.service.impl;

import com.example.boot.approve.service.ApproveRuntimeService;
import org.springframework.stereotype.Service;

/**
 * 默认的审批运行时服务实现
 * Created  on 2022/3/8 15:15:53
 *
 * @author wmz
 */
@Service
public class DefaultApproveRuntimeService implements ApproveRuntimeService {

    @Override
    public void approve(long instanceId, String remark) {

    }

    @Override
    public void refuse(long instanceId, String remark) {

    }

    @Override
    public void cancel(long instanceId, String remark) {

    }

    @Override
    public void reject(long instanceId, String remark, long nodeId) {

    }

    @Override
    public void transform(long instanceId, String remark, long transfer) {

    }
}
