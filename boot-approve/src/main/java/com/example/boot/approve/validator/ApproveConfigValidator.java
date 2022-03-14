package com.example.boot.approve.validator;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.boot.approve.common.exception.ResourceConflictException;
import com.example.boot.approve.common.exception.ResourceNotFoundException;
import com.example.boot.approve.entity.common.BaseApproveNode;
import com.example.boot.approve.entity.config.ApproveAssigneeConfig;
import com.example.boot.approve.entity.config.ApproveModel;
import com.example.boot.approve.entity.config.ApproveNodeConfig;
import com.example.boot.approve.mapper.ApproveAssigneeConfigMapper;
import com.example.boot.approve.mapper.ApproveNodeConfigMapper;
import com.example.boot.approve.service.ApproveConfigManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.List;

/**
 * 审批配置校验服务
 * Created  on 2022/3/10 16:16:30
 *
 * @author zl
 */
@Component
public class ApproveConfigValidator {

    @Autowired
    private ApproveNodeConfigMapper approveNodeConfigMapper;

    @Autowired
    private ApproveAssigneeConfigMapper approveAssigneeConfigMapper;

    @Autowired
    private ApproveConfigManager approveConfigManager;

    //    ==========================审批模板配置==============================

    /**
     * 验证审批模板配置的合法性
     *
     * @param model 审批模板
     */
    public void verifyApproveModelIsValid(ApproveModel model) {

        Assert.notNull(model, "审批模板配置不能为空");
        Assert.hasLength(model.getUri(), "审批模板跳转路径不能为空,模板名称 ： " + model.getName());
        Assert.hasLength(model.getDescription(), "审批模板的详情配置不能为空 ,模板名称 ： " + model.getName());

    }

    //    ==========================审批节点配置==============================

    /**
     * 【更新】校验节点配置是否合法
     * 主要针对节点类型发生变化时进行校验
     *
     * @param nodeConfig 新的节点配置信息
     */
    public void verifyNodeConfigIsValidForUpdate(ApproveNodeConfig nodeConfig) {

        ApproveNodeConfig oldApproveNodeConfig = approveNodeConfigMapper.selectById(nodeConfig.getId());

        // 名称发生变化，需要进行名称判定
        if (!nodeConfig.getName().equalsIgnoreCase(oldApproveNodeConfig.getName())) {
            verifyNodeConfigNameIsValid(nodeConfig);
        }

        // 节点类型没有发生变化
        if (oldApproveNodeConfig.getType().equals(nodeConfig.getType())) {
            return;
        }

        // 【直签转为异或，会签】
        boolean direct = BaseApproveNode.ApproveType.DIRECT.equals(oldApproveNodeConfig.getType());
        // 【异或转会签】
        boolean xofToCountersign = BaseApproveNode.ApproveType.XOF.equals(oldApproveNodeConfig.getType()) && BaseApproveNode.ApproveType.COUNTERSIGN.equals(
                        nodeConfig.getType());
        if (direct || xofToCountersign) {
            return;
        }

        int size = approveAssigneeConfigMapper.selectList(
                        new QueryWrapper<ApproveAssigneeConfig>().lambda().eq(ApproveAssigneeConfig::getApproveNodeId, nodeConfig.getId())).size();

        // 【会签转异或】 审批人数量大于2
        boolean countersignToXof =
                        BaseApproveNode.ApproveType.COUNTERSIGN.equals(oldApproveNodeConfig.getType()) && BaseApproveNode.ApproveType.COUNTERSIGN.equals(
                                        nodeConfig.getType());
        if (countersignToXof && size > 2) {
            throw new ResourceConflictException(nodeConfig.getName() + "【节点修改】 审批类型从会签转为异或, 审批人不能多于2个， 请选择保留最多2位审批人, 删除其他审批人 ");
        }

        // 【会签转直签】
        boolean countersignToDirect =
                        BaseApproveNode.ApproveType.COUNTERSIGN.equals(oldApproveNodeConfig.getType()) && BaseApproveNode.ApproveType.DIRECT.equals(
                                        nodeConfig.getType());
        if (countersignToDirect && size > 1) {
            throw new ResourceConflictException(nodeConfig.getName() + "【节点修改】 审批类型从会签转为直签, 审批人不能多于1个， 请选择保1位审批人, 删除其他审批人 ");
        }

        // 【异或转直签】
        boolean xofToDirect = BaseApproveNode.ApproveType.XOF.equals(oldApproveNodeConfig.getType()) && BaseApproveNode.ApproveType.DIRECT.equals(
                        nodeConfig.getType());
        if (xofToDirect && size > 1) {
            throw new ResourceConflictException(nodeConfig.getName() + "【节点修改】 审批类型从异或转为直签, 审批人不能多于1个， 请选择保留1位审批人, 删除其他审批人 ");
        }

    }

    /**
     * 校验审批模板 审批人是否符合规格
     *
     * @param modelId 模板ID
     */
    public void verifyModelAssigneeIsValid(long modelId) {

        List<ApproveNodeConfig> approveNodeConfigs = approveConfigManager.listNodeConfig(modelId);
        Assert.isTrue(approveNodeConfigs.size() != 0, "【审批模板发布】 : 模板至少需要有一级审批节点，模板ID : " + modelId);
        approveNodeConfigs.forEach(nodeConfig -> {
            List<ApproveAssigneeConfig> assignees = approveConfigManager.listAssignee(nodeConfig.getId());
            if (assignees.size() < 1) {
                throw new ResourceNotFoundException(nodeConfig.getName() + "【审批模板发布】 ： 审批节点, 需要指定审批人员，节点名称 ：" + nodeConfig.getName());
            }
        });

    }

    /**
     * 校验审批节点添加的合法性
     * 校验项：
     * 审批节点名称是否冲突
     *
     * @param nodeConfig 审批节点配置信息
     */
    public void verifyNodeConfigNameIsValid(ApproveNodeConfig nodeConfig) {

        ApproveNodeConfig approveNodeConfig = approveNodeConfigMapper.selectOne(new QueryWrapper<ApproveNodeConfig>().lambda()
                                                                                                                     .eq(BaseApproveNode::getName,
                                                                                                                         nodeConfig.getName())
                                                                                                                     .eq(ApproveNodeConfig::getApproveModelId,
                                                                                                                         nodeConfig.getApproveModelId()));
        Assert.isNull(approveNodeConfig, "同一版本的审批模板下，审批节点名称不可以重复，当前审批节点名称 ： " + nodeConfig.getName());

    }

    //    ==========================审批人配置================================

    /**
     * 审批人添加校验
     * 审批人规则：
     * 直签 ： 有且只有一位审批人
     * 异或 ： 至多有两位审批人
     * 会签 ： 至少一位审批人，人数不做限制
     *
     * @param assigneeConfig 审批人配置信息
     */
    public void verifyAssigneeIsValid(ApproveAssigneeConfig assigneeConfig) {
        ApproveNodeConfig nodeConfig = approveNodeConfigMapper.selectById(assigneeConfig.getApproveNodeId());

        Long assigneeSize = approveAssigneeConfigMapper.selectCount(
                        new QueryWrapper<ApproveAssigneeConfig>().lambda().eq(ApproveAssigneeConfig::getApproveNodeId, assigneeConfig.getApproveNodeId()));

        BaseApproveNode.ApproveType type = nodeConfig.getType();

        // 直签
        if (BaseApproveNode.ApproveType.DIRECT.equals(type) && assigneeSize == 1) {
            throw new ResourceConflictException("当前审批节点类型为【直签】有且只能有1位审批人");
        }

        // 异或
        if (BaseApproveNode.ApproveType.DIRECT.equals(type) && assigneeSize == 2) {
            throw new ResourceConflictException("当前审批节点类型为【异或】最多只能有2位审批人");
        }
        // 会签 不做任何限制，可以添加多个，但是至少一个，但是当前操作为添加所以不做任何判断

    }

    //    ==========================审批配置通用功能===========================

}
