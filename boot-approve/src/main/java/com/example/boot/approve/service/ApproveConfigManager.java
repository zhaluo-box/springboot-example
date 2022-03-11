package com.example.boot.approve.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.boot.approve.common.mvc.BasePageQuery;
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

    List<ApproveModel> listModel(String modelName, String serviceName);

    IPage<ApproveModel> pageModel(String modelName, String serviceName, BasePageQuery<String, ApproveModel> pageQuery);

    void saveModel(ApproveModel model);

    void updateModel(ApproveModel model);

    void copyModel(long modelId);

    void publishModel(long modelId);

    List<ApproveNodeConfig> listNodeConfig(long modelId);

    void saveNode(ApproveNodeConfig nodeConfig) throws Exception;

    void updateNode(ApproveNodeConfig nodeConfig);

    /**
     * 删除节点配置， 节点自身的序号不变
     */
    void deleteNode(long nodeId);

    List<ApproveAssigneeConfig> listAssignee(long nodeId);

    void saveAssignee(ApproveAssigneeConfig assigneeConfig);

    void deleteAssignee(long assigneeId);

    void updateAssignee(ApproveAssigneeConfig assigneeConfig);

    void preview(long modelId);

}
