package com.example.boot.approve.service.impl;

import com.example.boot.approve.common.exception.ResourceNotFoundException;
import com.example.boot.approve.entity.common.BaseApproveNode;
import com.example.boot.approve.entity.config.ApproveAssigneeConfig;
import com.example.boot.approve.entity.config.ApproveModel;
import com.example.boot.approve.entity.config.ApproveNodeConfig;
import com.example.boot.approve.entity.runtime.ApproveInstance;
import com.example.boot.approve.entity.runtime.ApproveNodeRecord;
import com.example.boot.approve.entity.runtime.ApproveRunningRecord;
import com.example.boot.approve.service.ApproveConfigManager;
import com.example.boot.approve.service.ApproveRecordManager;
import com.example.boot.approve.view.ApproveInstanceView;
import com.example.boot.approve.view.ApproveNodeRecordView;
import com.example.boot.approve.view.ApproveRunningRecordView;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        approveInstance.setInitiator(100L).setInitiateTime(new Date()).setApproveModelId(modelId).setParamId(paramId).setReason(reason);
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

        // 创建审批人记录【对审批人是发起者做特殊处理】
        List<ApproveRunningRecord> runningRecordList = nodeRecords.stream()
                                                                  .flatMap(this::getApproveRunningRecordStream)
                                                                  .peek(approveRunningRecord -> approveRunningRecord.setId(null)
                                                                                                                    .setCreateTime(new Date())
                                                                                                                    .setLastModifyTime(new Date()))
                                                                  .collect(Collectors.toList());
        approveRunningRecordView.saveBatch(runningRecordList);

        // 下一级节点与审批人
        ApproveNodeRecord nextNodeRecord = nodeRecords.stream()
                                                      .min(Comparator.comparing(BaseApproveNode::getLevel))
                                                      .orElseThrow(() -> new ResourceNotFoundException("不存在下一级审批节点！审批实例Id"));

        List<Long> nextAssignees = runningRecordList.stream()
                                                    .filter(runningRecord -> runningRecord.getNodeRecordId() == nextNodeRecord.getId())
                                                    .map(ApproveRunningRecord::getAssignee)
                                                    .collect(Collectors.toList());

        approveInstance.setNextNodeId(nextNodeRecord.getId()).setNextNodeName(nextNodeRecord.getName()).setNextAssignees(nextAssignees);
        approveInstanceView.updateById(approveInstance);

        String message = "有一则【" + model.getName() + "】审批带您处理!";
        approveRunningHelper.notifiedNextNodeAssignee(nextNodeRecord, message, true);

    }

    private Stream<ApproveRunningRecord> getApproveRunningRecordStream(ApproveNodeRecord nodeRecord) {
        List<ApproveAssigneeConfig> assigneeConfigs = approveConfigManager.listAssignee(nodeRecord.getNodeConfigId());
        return assigneeConfigs.stream().map(assigneeConfig -> {
            ApproveRunningRecord runningRecord = new ApproveRunningRecord();
            BeanUtils.copyProperties(assigneeConfig, runningRecord);
            runningRecord.setNodeRecordId(nodeRecord.getId()); // 设置审批节点记录的ID
            // 是否是发起人
            if (assigneeConfig.isInitiator()) {
                runningRecord.setAssignee(100L); // TODO 发起人 替换为从取值系统登录人员信息
            }
            return runningRecord;
        }).collect(Collectors.toList()).stream();
    }

}
