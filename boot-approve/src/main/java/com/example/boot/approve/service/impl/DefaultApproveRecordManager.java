package com.example.boot.approve.service.impl;

import com.example.boot.approve.common.exception.ResourceNotFoundException;
import com.example.boot.approve.entity.common.BaseApproveNode;
import com.example.boot.approve.entity.config.ApproveAssigneeConfig;
import com.example.boot.approve.entity.config.ApproveModel;
import com.example.boot.approve.entity.config.ApproveNodeConfig;
import com.example.boot.approve.entity.runtime.ApproveInstance;
import com.example.boot.approve.entity.runtime.ApproveNodeRecord;
import com.example.boot.approve.entity.runtime.ApproveRunningRecord;
import com.example.boot.approve.enums.ApproveResult;
import com.example.boot.approve.service.ApproveConfigManager;
import com.example.boot.approve.service.ApproveRecordManager;
import com.example.boot.approve.view.ApproveInstanceView;
import com.example.boot.approve.view.ApproveNodeRecordView;
import com.example.boot.approve.view.ApproveRunningRecordView;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 审批记录管理
 * Created  on 2022/3/9 13:13:17
 *
 * @author zl
 */
@Service
public class DefaultApproveRecordManager implements ApproveRecordManager {

    @Autowired
    private ApproveConfigManager approveConfigManager;

    @Autowired
    private ApproveInstanceView approveInstanceView;

    @Autowired
    private ApproveNodeRecordView approveNodeRecordView;

    @Autowired
    private ApproveRunningRecordView approveRunningRecordView;

    @Autowired
    private ApproveRunningHelper approveRunningHelper;

    @Override
    @Transactional
    public void createInstance(long paramId, ApproveModel model, String reason) {

        Long modelId = model.getId();
        ApproveInstance approveInstance = new ApproveInstance();
        // TODO: 2022/3/15 发起者改为系统当前会话登录人员，目前采用写死的方式
        approveInstance.setInitiator(100L).setCreateTime(new Date()).setApproveModelId(modelId).setParamId(paramId).setReason(reason).setName(model.getName());
        approveInstanceView.save(approveInstance);

        // 创建节点
        Long instanceId = approveInstance.getId();
        List<ApproveNodeConfig> nodeConfigs = approveConfigManager.listNodeConfig(modelId);
        List<ApproveNodeRecord> nodeRecords = nodeConfigs.stream().map(nodeConfig -> {
            ApproveNodeRecord approveNodeRecord = new ApproveNodeRecord();
            BeanUtils.copyProperties(nodeConfig, approveNodeRecord);
            approveNodeRecord.setInstanceId(instanceId).setNodeConfigId(nodeConfig.getId()).setId(null);
            return approveNodeRecord;
        }).collect(Collectors.toList());
        approveNodeRecordView.saveBatch(nodeRecords);

        //@formatter:off 创建审批人记录【对审批人是发起者做特殊处理】
        List<ApproveRunningRecord> runningRecordList = nodeRecords.stream()
                                                                  .flatMap(this::getApproveRunningRecordStream)
                                                                  .peek(approveRunningRecord -> approveRunningRecord.setId(null)
                                                                                                                    .setInstanceId(instanceId)
                                                                                                                    .setCreateTime(new Date())
                                                                                                                    .setLastModifyTime(new Date()))
                                                                  .collect(Collectors.toList());
        approveRunningRecordView.saveBatch(runningRecordList);

        //@formatter:on 下一级节点与审批人
        ApproveNodeRecord nextNodeRecord = nodeRecords.stream()
                                                      .min(Comparator.comparing(BaseApproveNode::getLevel))
                                                      .orElseThrow(() -> new ResourceNotFoundException("不存在下一级审批节点！审批实例Id" + instanceId));

        List<ApproveRunningRecord> nextNodeRunningRecords = runningRecordList.stream()
                                                                             .filter(runningRecord -> runningRecord.getNodeRecordId() == nextNodeRecord.getId())
                                                                             .collect(Collectors.toList());

        List<Long> nextAssignees = nextNodeRunningRecords.stream().map(ApproveRunningRecord::getAssignee).collect(Collectors.toList());

        approveInstance.setNextNodeId(nextNodeRecord.getId()).setNextNodeName(nextNodeRecord.getName()).setNextAssignees(nextAssignees);
        approveInstanceView.updateById(approveInstance);

        String message = "有一则【" + model.getName() + "】审批需要您处理!";
        approveRunningHelper.notifiedNextNodeAssignee(nextNodeRunningRecords, message, true);

    }

    @Override
    public List<ApproveNodeRecord> listApprovedNode(long instanceId) {
        return approveNodeRecordView.list(instanceId, ApproveResult.APPROVED, false);
    }

    @Override
    public List<ApproveNodeRecord> listRejectedAbleNode(long instanceId, long currNodeId, List<ApproveNodeRecord> nodeRecords) {
        ApproveNodeRecord currNode = approveNodeRecordView.getById(currNodeId);
        Assert.notNull(currNode, "节点ID 异常，当前节点可能已被删除！");
        return nodeRecords.stream().filter(nodeRecord -> nodeRecord.getLevel() < currNode.getLevel()).collect(Collectors.toList());
    }

    private Stream<ApproveRunningRecord> getApproveRunningRecordStream(ApproveNodeRecord nodeRecord) {
        List<ApproveAssigneeConfig> assigneeConfigs = approveConfigManager.listAssignee(nodeRecord.getNodeConfigId());
        return assigneeConfigs.stream().map(assigneeConfig -> {
            ApproveRunningRecord runningRecord = new ApproveRunningRecord();
            BeanUtils.copyProperties(assigneeConfig, runningRecord);
            runningRecord.setNodeRecordId(nodeRecord.getId()).setNodeRecordName(nodeRecord.getName()); // 设置审批节点记录的ID
            // 是否是发起人
            if (assigneeConfig.isInitiator()) {
                runningRecord.setAssignee(100L); // TODO 发起人 替换为从取值系统登录人员信息
            }
            return runningRecord;
        }).collect(Collectors.toList()).stream();
    }

}
