package com.example.boot.approve.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.boot.approve.common.exception.ResourceConflictException;
import com.example.boot.approve.common.exception.ResourceNotFoundException;
import com.example.boot.approve.common.mvc.BasePageQuery;
import com.example.boot.approve.entity.common.BaseApproveNode;
import com.example.boot.approve.entity.config.ApproveAssigneeConfig;
import com.example.boot.approve.entity.config.ApproveModel;
import com.example.boot.approve.entity.config.ApproveNodeConfig;
import com.example.boot.approve.mapper.ApproveAssigneeConfigMapper;
import com.example.boot.approve.mapper.ApproveModelMapper;
import com.example.boot.approve.mapper.ApproveNodeConfigMapper;
import com.example.boot.approve.service.ApproveConfigManager;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

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

    @Autowired
    private SqlSessionTemplate sqlSessionBatchTemplate;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void copyModel(long modelId) {

        // 模板复制
        ApproveModel approveModel = approveModelMapper.selectById(modelId);
        List<ApproveNodeConfig> approveNodeConfigs = listNodeConfig(modelId);
        LambdaQueryWrapper<ApproveAssigneeConfig> query = new QueryWrapper<ApproveAssigneeConfig>().lambda()
                                                                                                   .in(ApproveAssigneeConfig::getApproveNodeId,
                                                                                                       approveNodeConfigs.stream()
                                                                                                                         .map(ApproveNodeConfig::getId)
                                                                                                                         .collect(Collectors.toUnmodifiableList()));
        List<ApproveAssigneeConfig> assigneeConfigList = approveAssigneeConfigMapper.selectList(query);

        // 版本号自动+1 存为草稿
        int x = approveModelMapper.selectMaxVersionByModelId(modelId);
        approveModel.setVersion(x + 1).setDisabled(true).setStatus(ApproveModel.ApproveModelStatus.DRAFT).setId(null);

        approveNodeConfigs.forEach(approveNodeConfig -> approveNodeConfig.setId(null));
        assigneeConfigList.forEach(assigneeConfig -> assigneeConfig.setId(null));

        saveModel(approveModel);
        approveNodeConfigs.forEach(approveNodeConfigMapper::insert);
        assigneeConfigList.forEach(approveAssigneeConfigMapper::insert);

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void publishModel(long modelId) {

        ApproveModel approveModel = approveModelMapper.selectById(modelId);

        checkAssignee(modelId);

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
    public void saveNode(ApproveNodeConfig nodeConfig) throws Exception {
        approveNodeConfigMapper.insert(nodeConfig);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateNode(ApproveNodeConfig nodeConfig) {

        ApproveNodeConfig oldApproveNodeConfig = approveNodeConfigMapper.selectById(nodeConfig.getId());

        // 节点类型没有发生变化
        if (oldApproveNodeConfig.getType().equals(nodeConfig.getType())) {
            approveNodeConfigMapper.updateById(nodeConfig);
            return;
        }
        // TODO 节点类型发生变化

        // 查询以前的节点类型 以前是直签 转为异或 会签不发生任何变化
        boolean direct = BaseApproveNode.ApproveType.DIRECT.equals(oldApproveNodeConfig.getType());
        // 如果异或转会签 也不发生变化
        boolean xofToCountersign = BaseApproveNode.ApproveType.XOF.equals(oldApproveNodeConfig.getType()) && BaseApproveNode.ApproveType.COUNTERSIGN.equals(
                        nodeConfig.getType());

        if (direct || xofToCountersign) {
            approveNodeConfigMapper.updateById(nodeConfig);
            return;
        }

        // 会签转异或 审批人数量大于2 只保留两个，删除最后一个
        boolean countersignToXof =
                        BaseApproveNode.ApproveType.COUNTERSIGN.equals(oldApproveNodeConfig.getType()) && BaseApproveNode.ApproveType.COUNTERSIGN.equals(
                                        nodeConfig.getType());

        if (countersignToXof && listAssignee(nodeConfig.getId()).size() > 2) {
            throw new ResourceConflictException(nodeConfig.getName() + "节点 审批类型从会签转为异或, 审批人不能多于2个， 请选择保留最多2位审批人, 删除其他审批人 ");
        }

        // 会签转直签
        boolean countersignToDirect =
                        BaseApproveNode.ApproveType.COUNTERSIGN.equals(oldApproveNodeConfig.getType()) && BaseApproveNode.ApproveType.DIRECT.equals(
                                        nodeConfig.getType());

        if (countersignToDirect && listAssignee(nodeConfig.getId()).size() > 1) {
            throw new ResourceConflictException(nodeConfig.getName() + "节点 审批类型从会签转为直签, 审批人不能多于2个， 请选择保留最多2位审批人, 删除其他审批人 ");
        }

        approveNodeConfigMapper.updateById(nodeConfig);

    }

    /**
     * TODO @me 考虑节点顺序是否可以任意调整
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteNode(long nodeId) {

        // TODO 检查当前审批模板是否只有一级节点 如果已有一级节点 不允许删除，至少需要一级审批人

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
    public void preview(long modelId) {
        // TODO 预览返回值待定
    }

    /**
     * TODO 将检查逻辑 放到validator 业务模块 将检查与事务分离
     * 检查审批节点下的审批人员， 所有审批节点下至少要由一位审批人员
     */
    private void checkAssignee(long modelId) {
        List<ApproveNodeConfig> approveNodeConfigs = listNodeConfig(modelId);
        approveNodeConfigs.forEach(nodeConfig -> {
            List<ApproveAssigneeConfig> assignees = listAssignee(nodeConfig.getId());
            if (assignees.size() < 1) {
                throw new ResourceNotFoundException(nodeConfig.getName() + "审批节点, 需要指定审批人员");
            }
        });
    }
}
