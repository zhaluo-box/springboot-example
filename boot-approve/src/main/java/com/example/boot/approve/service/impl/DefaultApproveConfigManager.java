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
import com.example.boot.approve.service.ApproveConfigManager;
import com.example.boot.approve.view.ApproveAssigneeConfigView;
import com.example.boot.approve.view.ApproveModelView;
import com.example.boot.approve.view.ApproveNodeConfigView;
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
    private ApproveModelView approveModelView;

    @Autowired
    private ApproveNodeConfigView approveNodeConfigView;

    @Autowired
    private ApproveAssigneeConfigView approveAssigneeConfigView;

    // ===========================审批模板配置===============================

    @Override
    public List<ApproveModel> listModel(String modelName, String serviceName) {
        LambdaQueryWrapper<ApproveModel> wrapper = new QueryWrapper<ApproveModel>().lambda()
                                                                                   .eq(StringUtils.hasLength(serviceName), ApproveModel::getServiceName,
                                                                                       serviceName)
                                                                                   .eq(StringUtils.hasLength(modelName), ApproveModel::getName, modelName);
        return approveModelView.getBaseMapper().selectList(wrapper);
    }

    @Override
    public IPage<ApproveModel> pageModel(String modelName, String serviceName, BasePageQuery<String, ApproveModel> pageQuery) {
        var page = pageQuery.buildPage();
        LambdaQueryWrapper<ApproveModel> queryWrapper = new QueryWrapper<ApproveModel>().lambda()
                                                                                        .eq(StringUtils.hasLength(serviceName), ApproveModel::getServiceName,
                                                                                            serviceName)
                                                                                        .eq(StringUtils.hasLength(modelName), ApproveModel::getName, modelName);

        return approveModelView.getBaseMapper().selectPage(page, queryWrapper);
    }

    @Override
    @Transactional
    public void saveModel(ApproveModel model) {
        approveModelView.save(model);
    }

    @Override
    public void updateModel(ApproveModel model) {
        approveModelView.getBaseMapper().updateById(model);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void copyModel(long modelId) {

        // 模板复制
        ApproveModel approveModel = approveModelView.getBaseMapper().selectById(modelId);
        List<ApproveNodeConfig> nodeConfigs = listNodeConfig(modelId);
        LambdaQueryWrapper<ApproveAssigneeConfig> query = new QueryWrapper<ApproveAssigneeConfig>().lambda()
                                                                                                   .in(ApproveAssigneeConfig::getApproveNodeId,
                                                                                                       nodeConfigs.stream()
                                                                                                                  .map(ApproveNodeConfig::getId)
                                                                                                                  .collect(Collectors.toUnmodifiableList()));
        List<ApproveAssigneeConfig> assigneeConfigs = approveAssigneeConfigView.getBaseMapper().selectList(query);

        // 版本号自动+1 存为草稿
        int x = approveModelView.getBaseMapper().selectMaxVersionByModelId(modelId);
        approveModel.setVersion(x + 1).setDisabled(true).setStatus(ApproveModel.ApproveModelStatus.DRAFT).setId(null);
        saveModel(approveModel);

        log.info("审批模板ID,{}", approveModel.getId());
        // 节点配置信息复制
        nodeConfigs.forEach(nodeConfig -> nodeConfig.setId(null).setApproveModelId(approveModel.getId()));
        approveNodeConfigView.saveBatch(nodeConfigs);

        //审批人信息复制
        assigneeConfigs.forEach(oldAssigneeConfig -> oldAssigneeConfig.setId(null)
                                                                      .setApproveNodeId(findNewApproveNodeConfigId(oldAssigneeConfig.getApproveNodeName(),
                                                                                                                   nodeConfigs)));

        approveAssigneeConfigView.saveBatch(assigneeConfigs);

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void publishModel(long modelId) {

        ApproveModel approveModel = approveModelView.getBaseMapper().selectById(modelId);

        // 其他版本处理
        List<ApproveModel> otherModels = approveModelView.list(new QueryWrapper<ApproveModel>().lambda()
                                                                                               .eq(ApproveModel::getName, approveModel.getName())
                                                                                               .eq(ApproveModel::getServiceName, approveModel.getServiceName())
                                                                                               .ne(ApproveModel::getId, approveModel.getId()));

        otherModels.forEach(model -> {
            if (model.getStatus().equals(ApproveModel.ApproveModelStatus.OFFICIAL_EDITION)) {
                model.setStatus(ApproveModel.ApproveModelStatus.HISTORY);
            }
            model.setDisabled(true);
        });

        approveModelView.updateBatchById(otherModels);

        // 发布审批模板，当前版本模板禁用按钮变为false, 其他版本审批模板全部变为true
        approveModel.setStatus(ApproveModel.ApproveModelStatus.OFFICIAL_EDITION).setDisabled(false);
        approveModelView.updateById(approveModel);
    }

    @Override
    public ApproveModel findOfficialEditionModel(String modelName) {
        return approveModelView.getBaseMapper()
                               .selectOne(new QueryWrapper<ApproveModel>().lambda()
                                                                          .eq(ApproveModel::getName, modelName)
                                                                          .eq(ApproveModel::getStatus, ApproveModel.ApproveModelStatus.OFFICIAL_EDITION));
    }

    // ===========================审批节点配置===============================

    @Override
    public List<ApproveNodeConfig> listNodeConfig(long modelId) {
        // 查询与模板ID关联的所有审批节点配置
        return approveNodeConfigView.getBaseMapper()
                                    .selectList(new QueryWrapper<ApproveNodeConfig>().lambda().eq(ApproveNodeConfig::getApproveModelId, modelId));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveNode(ApproveNodeConfig nodeConfig) {
        approveNodeConfigView.getBaseMapper().insert(nodeConfig);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateNode(ApproveNodeConfig nodeConfig) {
        approveNodeConfigView.getBaseMapper().updateById(nodeConfig);
    }

    /**
     * TODO @me 考虑节点顺序是否可以任意调整
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteNode(long nodeId) {
        // 删除节点配置与关联的审批人配置
        approveNodeConfigView.getBaseMapper().deleteById(nodeId);
        listAssignee(nodeId).forEach(assigneeConfig -> deleteAssignee(assigneeConfig.getId()));
    }

    @Override
    public List<ApproveAssigneeConfig> listAssignee(long nodeId) {
        // 查询节点关联的所有审批人配置
        return approveAssigneeConfigView.getBaseMapper()
                                        .selectList(new QueryWrapper<ApproveAssigneeConfig>().lambda().eq(ApproveAssigneeConfig::getApproveNodeId, nodeId));
    }

    // ========================审批人================================

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveAssignee(ApproveAssigneeConfig assigneeConfig) {
        // 添加节点审批人
        approveAssigneeConfigView.getBaseMapper().insert(assigneeConfig);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteAssignee(long assigneeId) {
        // 删除审批人
        approveAssigneeConfigView.getBaseMapper().deleteById(assigneeId);

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateAssignee(ApproveAssigneeConfig assigneeConfig) {
        // 更新审批人
        approveAssigneeConfigView.getBaseMapper().updateById(assigneeConfig);
    }

    @Override
    public ApproveModelConfigPreviewDTO preview(long modelId) {

        ApproveModel approveModel = approveModelView.getBaseMapper().selectById(modelId);
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

    private long findNewApproveNodeConfigId(String approveNodeName, List<ApproveNodeConfig> nodeConfigs) {
        return nodeConfigs.stream()
                          .filter(nodeConfig -> nodeConfig.getName().equals(approveNodeName))
                          .map(ApproveNodeConfig::getId)
                          .findFirst()
                          .orElseThrow(() -> new ResourceNotFoundException("未找到审批节点为" + approveNodeName + "的配置信息"));
    }
}
