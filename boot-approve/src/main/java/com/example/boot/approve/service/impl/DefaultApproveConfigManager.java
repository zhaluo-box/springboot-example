package com.example.boot.approve.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.boot.approve.common.exception.ResourceNotFoundException;
import com.example.boot.approve.common.mvc.BasePageQuery;
import com.example.boot.approve.controller.dto.ApproveAssigneeConfigDTO;
import com.example.boot.approve.controller.dto.ApproveModelConfigPreviewDTO;
import com.example.boot.approve.controller.dto.ApproveNodeConfigDTO;
import com.example.boot.approve.entity.common.BaseApproveNode;
import com.example.boot.approve.entity.config.ApproveAssigneeConfig;
import com.example.boot.approve.entity.config.ApproveModel;
import com.example.boot.approve.entity.config.ApproveNodeConfig;
import com.example.boot.approve.mapper.ApproveAssigneeConfigMapper;
import com.example.boot.approve.mapper.ApproveModelMapper;
import com.example.boot.approve.mapper.ApproveNodeConfigMapper;
import com.example.boot.approve.service.ApproveConfigManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 审批配置管理
 * Created  on 2022/3/9 13:13:16
 *
 * @author zl
 */
@Slf4j
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
    public IPage<ApproveModel> pageModel(String modelName, String serviceName, BasePageQuery<String, ApproveModel> pageQuery) {
        var page = pageQuery.buildPage();
        LambdaQueryWrapper<ApproveModel> queryWrapper = new QueryWrapper<ApproveModel>().lambda()
                                                                                        .eq(StringUtils.hasLength(serviceName), ApproveModel::getServiceName,
                                                                                            serviceName)
                                                                                        .eq(StringUtils.hasLength(modelName), ApproveModel::getName, modelName);

        return approveModelMapper.selectPage(page, queryWrapper);
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
    public void copyModel(long modelId) {

        // 模板复制
        ApproveModel approveModel = approveModelMapper.selectById(modelId);
        List<ApproveNodeConfig> oldNodeConfigs = listNodeConfig(modelId);
        List<ApproveNodeConfig> oldNodeConfigSnapshot = copyListBean(oldNodeConfigs); // 节点信息快照

        LambdaQueryWrapper<ApproveAssigneeConfig> query = new QueryWrapper<ApproveAssigneeConfig>().lambda()
                                                                                                   .in(ApproveAssigneeConfig::getApproveNodeId,
                                                                                                       oldNodeConfigs.stream()
                                                                                                                     .map(ApproveNodeConfig::getId)
                                                                                                                     .collect(Collectors.toUnmodifiableList()));
        List<ApproveAssigneeConfig> oldAssigneeConfigList = approveAssigneeConfigMapper.selectList(query);

        // 版本号自动+1 存为草稿
        int x = approveModelMapper.selectMaxVersionByModelId(modelId);
        approveModel.setVersion(x + 1).setDisabled(true).setStatus(ApproveModel.ApproveModelStatus.DRAFT).setId(null);
        saveModel(approveModel);

        // 节点信息复制 通过版本号，名称，应用名称 查出最新复制的审批模板
        ApproveModel newModel = approveModelMapper.selectOne(new QueryWrapper<ApproveModel>().lambda()
                                                                                             .eq(ApproveModel::getVersion, x + 1)
                                                                                             .eq(ApproveModel::getName, approveModel.getName())
                                                                                             .eq(ApproveModel::getServiceName, approveModel.getServiceName()));

        oldNodeConfigs.stream()
                      .peek(nodeConfig -> nodeConfig.setApproveModelId(newModel.getId()))
                      .peek(approveNodeConfig -> approveNodeConfig.setId(null))
                      .forEach(approveNodeConfigMapper::insert);

        //审批人信息复制
        List<ApproveNodeConfig> newNodeConfigs = approveNodeConfigMapper.selectList(
                        new QueryWrapper<ApproveNodeConfig>().lambda().eq(ApproveNodeConfig::getApproveModelId, newModel.getId()));

        oldAssigneeConfigList.stream().peek(oldAssigneeConfig -> oldAssigneeConfig.setId(null)).peek(oldAssigneeConfig -> {
            long oldNodeId = oldAssigneeConfig.getApproveNodeId();
            Long newNodeId = findNewApproveNodeConfigId(oldNodeId, oldNodeConfigSnapshot, newNodeConfigs);
            oldAssigneeConfig.setApproveNodeId(newNodeId);
        }).forEach(approveAssigneeConfigMapper::insert);

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void publishModel(long modelId) {

        ApproveModel approveModel = approveModelMapper.selectById(modelId);

        // 其他版本全部禁用
        ApproveModel model = new ApproveModel().setDisabled(true);
        approveModelMapper.update(model, new QueryWrapper<ApproveModel>().lambda()
                                                                         .eq(ApproveModel::getName, approveModel.getName())
                                                                         .eq(ApproveModel::getServiceName, approveModel.getServiceName()));
        model.setStatus(ApproveModel.ApproveModelStatus.HISTORY);

        // 当前生效的版本 置为历史版
        approveModelMapper.update(model, new QueryWrapper<ApproveModel>().lambda()
                                                                         .eq(ApproveModel::getStatus, ApproveModel.ApproveModelStatus.OFFICIAL_EDITION)
                                                                         .eq(ApproveModel::getName, approveModel.getName())
                                                                         .eq(ApproveModel::getServiceName, approveModel.getServiceName()));

        // 发布审批模板，当前版本模板禁用按钮变为false, 其他版本审批模板全部变为true
        approveModel.setStatus(ApproveModel.ApproveModelStatus.OFFICIAL_EDITION).setDisabled(false);
        approveModelMapper.updateById(approveModel);
    }

    // ===========================审批节点配置===============================

    @Override
    public List<ApproveNodeConfig> listNodeConfig(long modelId) {
        // 查询与模板ID关联的所有审批节点配置
        return approveNodeConfigMapper.selectList(new QueryWrapper<ApproveNodeConfig>().lambda().eq(ApproveNodeConfig::getApproveModelId, modelId));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveNode(ApproveNodeConfig nodeConfig) {
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
    public void deleteNode(long nodeId) {

        // TODO 检查当前审批模板是否只有一级节点 如果只剩下一级节点 不允许删除，至少需要一级审批人

        approveNodeConfigMapper.deleteById(nodeId);
        //删除关联审批人
        listAssignee(nodeId).forEach(assigneeConfig -> deleteAssignee(assigneeConfig.getId()));
    }

    @Override
    public List<ApproveAssigneeConfig> listAssignee(long nodeId) {
        // 查询节点关联的所有审批人配置
        return approveAssigneeConfigMapper.selectList(new QueryWrapper<ApproveAssigneeConfig>().lambda().eq(ApproveAssigneeConfig::getApproveNodeId, nodeId));
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
    public ApproveModelConfigPreviewDTO preview(long modelId) {

        ApproveModel approveModel = approveModelMapper.selectById(modelId);
        // 获取审批节点相关信息
        List<ApproveNodeConfig> approveNodeConfigs = listNodeConfig(modelId);

        // 审批节点信息转换
        List<ApproveNodeConfigDTO> nodes = approveNodeConfigs.stream().map(node -> {
            ApproveNodeConfigDTO configDTO = new ApproveNodeConfigDTO();
            BeanUtils.copyProperties(node, configDTO);
            List<ApproveAssigneeConfig> approveAssigneeConfigs = listAssignee(node.getId());
            List<ApproveAssigneeConfigDTO> assigneeConfigDTOList = assigneeTransform(approveAssigneeConfigs);
            configDTO.setApproveAssigneeConfigs(assigneeConfigDTOList);
            return configDTO;
        }).sorted(Comparator.comparing(BaseApproveNode::getLevel)).collect(Collectors.toList());

        ApproveModelConfigPreviewDTO previewDTO = new ApproveModelConfigPreviewDTO();
        BeanUtils.copyProperties(approveModel, previewDTO);
        previewDTO.setApproveNodeConfigs(nodes);

        return previewDTO;
    }

    /**
     * 查找新的节点配置信息
     *
     * @param oldNodeId 旧的节点Id
     * @param oldNodes  旧的节点配置列表
     * @param newNodes  新的节点配置列表
     * @return 新的节点配置ID
     */
    private Long findNewApproveNodeConfigId(long oldNodeId, List<ApproveNodeConfig> oldNodes, List<ApproveNodeConfig> newNodes) {
        ApproveNodeConfig oldNodeConfig = oldNodes.stream()
                                                  .filter(oldNode -> oldNode.getId().equals(oldNodeId))
                                                  .findFirst()
                                                  .orElseThrow(() -> new ResourceNotFoundException("节点复制异常，通过节点Id找不到旧的的节点配置信息，节点ID" + oldNodeId));
        ApproveNodeConfig newNodeConfig = newNodes.stream()
                                                  .filter(newNode -> newNode.getName().equals(oldNodeConfig.getName()))
                                                  .findFirst()
                                                  .orElseThrow(() -> new ResourceNotFoundException("节点复制异常，通过节点名称找不到复制的节点配置信息，节点ID" + oldNodeId));
        return newNodeConfig.getId();
    }

    /**
     * TODO 待优化
     */
    private List<ApproveNodeConfig> copyListBean(List<ApproveNodeConfig> old) {
        List<ApproveNodeConfig> nodeConfigs = new ArrayList<>(old.size());
        old.forEach(o -> {
            ApproveNodeConfig approveNodeConfig = new ApproveNodeConfig();
            BeanUtils.copyProperties(o, approveNodeConfig);
            nodeConfigs.add(approveNodeConfig);
        });

        return nodeConfigs;
    }

    private List<ApproveAssigneeConfigDTO> assigneeTransform(List<ApproveAssigneeConfig> approveAssigneeConfigs) {
        return approveAssigneeConfigs.stream().map(assigneeConfig -> {
            ApproveAssigneeConfigDTO approveAssigneeConfigDTO = new ApproveAssigneeConfigDTO();
            BeanUtils.copyProperties(assigneeConfig, approveAssigneeConfigDTO);
            approveAssigneeConfigDTO.setAssigneeName(assigneeConfig.getAssignee() + "name TODO 待修改!");
            // 候选人信息转换
            List<Long> transferCandidates = assigneeConfig.getTransferCandidates();
            if (transferCandidates.size() > 0) {
                List<String> transferCandidateNames = new ArrayList<>();
                transferCandidates.forEach(transfer -> transferCandidateNames.add(transfer + " 转办人姓名 TODO待修改转换！"));
                approveAssigneeConfigDTO.setTransferCandidateNames(transferCandidateNames);
            }

            return approveAssigneeConfigDTO;
        }).sorted(Comparator.comparing(ApproveAssigneeConfig::getOrderNum)).collect(Collectors.toList());
    }
}
