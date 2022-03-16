package com.example.boot.approve.service.impl;

import com.example.boot.approve.common.exception.MesException;
import com.example.boot.approve.common.exception.ResourceNotFoundException;
import com.example.boot.approve.entity.runtime.ApproveInstance;
import com.example.boot.approve.entity.runtime.ApproveNodeRecord;
import com.example.boot.approve.entity.runtime.ApproveRunningRecord;
import com.example.boot.approve.enums.ApproveResult;
import com.example.boot.approve.service.MessageService;
import com.example.boot.approve.view.ApproveInstanceView;
import com.example.boot.approve.view.ApproveNodeRecordView;
import com.example.boot.approve.view.ApproveRunningRecordView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 审批运行协助服务， 主要做一些通用的检查 通知等业务
 * TODO 具体逻辑待实现
 * Created  on 2022/3/16 13:13:42
 *
 * @author zl
 */
@Service
public class ApproveRunningHelper {

    @Autowired
    private MessageService messageService;

    @Autowired
    private ApproveNodeRecordView approveNodeRecordView;

    @Autowired
    private ApproveRunningRecordView approveRunningRecordView;

    @Autowired
    private ApproveInstanceView approveInstanceView;

    /**
     * 通知审批人
     *
     * @param nextNodeRecord 下一级审批节点记录
     * @param message        消息
     * @param cancel         true 待审批 false 无需审批
     */
    @Transactional(rollbackFor = Exception.class)
    public void notifiedNextNodeAssignee(ApproveNodeRecord nextNodeRecord, String message, boolean cancel) {
        List<ApproveRunningRecord> runningRecordByCurrNode = approveRunningRecordView.findByNodeRecordId(nextNodeRecord.getId());
        List<Long> assignees = runningRecordByCurrNode.stream()
                                                      .peek(runningRecord -> runningRecord.setResult(ApproveResult.PENDING_APPROVED)
                                                                                          .setLastModifyTime(new Date()))
                                                      .map(ApproveRunningRecord::getAssignee)
                                                      .collect(Collectors.toList());
        approveRunningRecordView.saveBatch(runningRecordByCurrNode);
        messageService.notifyMessage(assignees, message);
    }

    /**
     * 查找待审批的节点
     *
     * @param instanceId 实例ID
     */
    public List<ApproveNodeRecord> findPendingApprovedNode(long instanceId) {
        return approveNodeRecordView.findNextPendingApprovedNode(instanceId, ApproveResult.PENDING_APPROVED);
    }

    @Transactional(rollbackFor = Exception.class)
    public void notifiedAssignee(long initiator, String endMessage) {
        messageService.notifyMessage(initiator, endMessage);
    }

    /**
     * 发现并检查审批实例是已经结束
     * 检查主要是针对异或， 会签的其他审批人员做了审批结束相关的处理【拒绝】或者发起人取消了审批
     *
     * @param instanceId    审批实例ID
     * @param approveHandle 这里的审批枚举指代的是审批的行为
     */
    public ApproveInstance findInstanceAndCheck(long instanceId, ApproveResult approveHandle) {
        // 对于审批实例而言，有三个过程 待审批 审批中 结束
        ApproveInstance approveInstance = approveInstanceView.getById(instanceId);
        ApproveResult result = approveInstance.getResult();

        if (ApproveResult.CANCELED.equals(approveHandle) && ApproveResult.IN_APPROVED.equals(result)) {
            throw new MesException(approveInstance.getName() + "审批已经开始，无法取消！");
        }

        // 如果当前审批不是处于待审批与审批中 则审批就代表结束了
        boolean status = ApproveResult.PENDING_APPROVED.equals(result) || ApproveResult.IN_APPROVED.equals(result);
        if (!status) {
            throw new MesException(approveInstance.getName() + "审批已经被处理，如有需要请自行查看审批详情！");
        }
        return approveInstance;
    }

    /**
     * 将其他待审批记录处理为无需审批，主要是针对异或或者会签
     *
     * @param currNode      当前审批节点记录
     * @param runningRecord 当前审批运行记录
     */
    @Transactional(rollbackFor = Exception.class)
    public void otherPendingApprovedRunningRecordHandler(ApproveNodeRecord currNode, ApproveRunningRecord runningRecord) {
        // 查询其他未审批的记录【处于待审批的记录】
        List<ApproveRunningRecord> pendingApprove = approveRunningRecordView.findAllByNodeIdAndNotEqualIdAndResultEqPendingApproved(currNode.getId(),
                                                                                                                                    runningRecord.getId());
        pendingApprove.forEach(rd -> rd.setResult(ApproveResult.NO_APPROVAL_REQUIRED));
        approveRunningRecordView.updateBatchById(pendingApprove);
    }

    /**
     * @param rejectNodeLevel 驳回节点等级
     * @param currNodeLevel   结束节点等级
     * @param instanceId      实例Id
     */
    public List<ApproveNodeRecord> findMidNodeRecord(int rejectNodeLevel, int currNodeLevel, long instanceId) {
        List<ApproveNodeRecord> midRecord = approveNodeRecordView.findByInstanceIdAndLevelBetween(instanceId, rejectNodeLevel, currNodeLevel);

        // 复制运行记录
        List<ApproveRunningRecord> runningRecords = midRecord.stream()
                                                             .map(ApproveNodeRecord::getId)
                                                             .flatMap(nodeId -> approveRunningRecordView.findByNodeRecordId(nodeId).stream())
                                                             .collect(Collectors.toList());

        midRecord.forEach(rd -> rd.setId(null));

        approveNodeRecordView.saveBatch(midRecord);

        // TODO  节点与审批记录初始化

        // 复制节点

        return null;
    }

    private long findNewRunningRecordId(String name, List<ApproveNodeRecord> newMidRecords) {
        return newMidRecords.stream()
                            .filter(record -> record.getName().equals(name))
                            .mapToLong(ApproveNodeRecord::getId)
                            .findFirst()
                            .orElseThrow(() -> new ResourceNotFoundException("为找到名称为" + name + "的节点"));
    }

    /**
     * 查找下一级审批节点
     */

    /**
     * 查找下一级审批人
     */

    /**
     * 通知下一级审批人
     */

    /**
     * 通知发起人
     */
}
