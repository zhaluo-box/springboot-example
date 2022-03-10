package com.example.boot.approve.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.boot.approve.entity.config.ApproveAssigneeConfig;
import com.example.boot.approve.entity.config.ApproveModel;
import com.example.boot.approve.entity.config.ApproveNodeConfig;
import com.example.boot.approve.mapper.ApproveAssigneeConfigMapper;
import com.example.boot.approve.mapper.ApproveModelMapper;
import com.example.boot.approve.mapper.ApproveNodeConfigMapper;
import com.example.boot.approve.service.ApproveConfigManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 审批配置管理
 * Created  on 2022/3/9 13:13:16
 *
 * @author zl
 */
@Service
public class DefaultApproveConfigManager implements ApproveConfigManager {

    @Autowired
    private ApproveModelMapper approveModelMapper;

    @Autowired
    private ApproveNodeConfigMapper approveNodeConfigMapper;

    @Autowired
    private ApproveAssigneeConfigMapper approveAssigneeConfigMapper;

    // ===========================审批模板配置===============================

    @Override
    public List<ApproveModel> listModel(String modelName, String serviceName) {
        LambdaQueryWrapper<ApproveModel> wrapper = new QueryWrapper<ApproveModel>().lambda()
                                                                                   .eq(StringUtils.hasLength(serviceName), ApproveModel::getServiceName,
                                                                                       serviceName)
                                                                                   .eq(StringUtils.hasLength(modelName), ApproveModel::getName, modelName);
        return approveModelMapper.selectList(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveModel(ApproveModel model) {
        approveModelMapper.insert(model);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateModel(ApproveModel model) {
        approveModelMapper.updateById(model);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void copyModel(String modelId) {
        // 复制审批模板配置
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void publishModel(String modelId) {
        // 发布自己之前检查所有审批节点下的审批人配置信息 每个审批节点必须包含一个审批人配置， 否则提示异常
        // 发布审批模板，当前版本模板禁用按钮变为false, 其他版本审批模板全部变为true
    }

    // ===========================审批节点配置===============================

    @Override
    public List<ApproveNodeConfig> listNodeConfig(long modelId) {
        // 查询与模板ID关联的所有审批节点配置
        return approveNodeConfigMapper.selectList(new QueryWrapper<ApproveNodeConfig>().eq(ApproveNodeConfig.ColumnConstant.APPROVE_MODEL_ID, modelId));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveNode(ApproveNodeConfig nodeConfig) throws Exception {
        approveNodeConfigMapper.insert(nodeConfig);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateNode(ApproveNodeConfig nodeConfig) {
        approveNodeConfigMapper.updateById(nodeConfig);
    }

    /**
     * TODO @me 考虑节点顺序是否可以任意调整
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteNode(String nodeId) {
        approveNodeConfigMapper.deleteById(nodeId);

    }

    @Override
    public List<ApproveAssigneeConfig> listAssignee(String nodeId) {
        // 查询节点关联的所有审批人配置
        return approveAssigneeConfigMapper.selectList(
                        new QueryWrapper<ApproveAssigneeConfig>().eq(ApproveAssigneeConfig.ColumnConstant.APPROVE_NODE_ID, nodeId));
    }

    // ========================审批人================================

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveAssignee(ApproveAssigneeConfig assigneeConfig) {
        // 添加节点审批人
        approveAssigneeConfigMapper.insert(assigneeConfig);
    }

    @Override
    public void deleteAssignee(long assigneeId) {
        // 删除审批人
        approveAssigneeConfigMapper.deleteById(assigneeId);

    }

    @Override
    public void updateAssignee(ApproveAssigneeConfig assigneeConfig) {
        // 更新审批人
        approveAssigneeConfigMapper.updateById(assigneeConfig);
    }

    @Override
    public void preview(long modelId) {
        // TODO 预览返回值待定
    }

}
