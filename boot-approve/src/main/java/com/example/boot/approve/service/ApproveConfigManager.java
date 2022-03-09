package com.example.boot.approve.service;

import com.example.boot.approve.entity.config.ApproveAssigneeConfig;
import com.example.boot.approve.entity.config.ApproveModel;
import com.example.boot.approve.entity.config.ApproveNodeConfig;

import java.util.List;

/**
 * 审批配置管理
 * Created  on 2022/3/9 11:11:45
 *
 * @author zl
 */
public interface ApproveConfigManager {

    void updateModel(ApproveModel model);

    void copyModel(String modelId);

    void publishModel(String modelId);

    List<ApproveNodeConfig> listNodeConfig(String modelId);

    void saveNode(ApproveNodeConfig nodeConfig);

    void updateNode(ApproveNodeConfig nodeConfig);

    void deleteNode(String nodeId);

    List<ApproveAssigneeConfig> listAssignee(String nodeId);

    void saveAssignee(ApproveAssigneeConfig assigneeConfig);

    void deleteAssignee(String assigneeId);

    void updateAssignee(ApproveAssigneeConfig assigneeConfig);

    void preview(String modelId);
}
