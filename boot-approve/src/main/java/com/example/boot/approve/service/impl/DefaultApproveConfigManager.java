package com.example.boot.approve.service.impl;

import com.example.boot.approve.entity.config.ApproveAssigneeConfig;
import com.example.boot.approve.entity.config.ApproveModel;
import com.example.boot.approve.entity.config.ApproveNodeConfig;
import com.example.boot.approve.service.ApproveConfigManager;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 审批配置管理
 * Created  on 2022/3/9 13:13:16
 *
 * @author zl
 */
@Service
public class DefaultApproveConfigManager implements ApproveConfigManager {

    @Override
    public void updateModel(ApproveModel model) {

    }

    @Override
    public void copyModel(String modelId) {

    }

    @Override
    public void publishModel(String modelId) {

    }

    @Override
    public List<ApproveNodeConfig> listNodeConfig(String modelId) {
        return null;
    }

    @Override
    public void saveNode(ApproveNodeConfig nodeConfig) {

    }

    @Override
    public void updateNode(ApproveNodeConfig nodeConfig) {

    }

    @Override
    public void deleteNode(String nodeId) {

    }

    @Override
    public List<ApproveAssigneeConfig> listAssignee(String nodeId) {
        return null;
    }

    @Override
    public void saveAssignee(ApproveAssigneeConfig assigneeConfig) {

    }

    @Override
    public void deleteAssignee(String assigneeId) {

    }

    @Override
    public void updateAssignee(ApproveAssigneeConfig assigneeConfig) {

    }

    @Override
    public void preview(String modelId) {

    }
}
