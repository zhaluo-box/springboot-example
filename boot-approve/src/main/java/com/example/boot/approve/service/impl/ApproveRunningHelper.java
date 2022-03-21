package com.example.boot.approve.service.impl;

import com.example.boot.approve.common.exception.MesException;
import com.example.boot.approve.common.exception.ResourceNotFoundException;
import com.example.boot.approve.common.utils.SecurityUtil;
import com.example.boot.approve.entity.User;
import com.example.boot.approve.entity.runtime.ApproveInstance;
import com.example.boot.approve.entity.runtime.ApproveNodeRecord;
import com.example.boot.approve.entity.runtime.ApproveRunningRecord;
import com.example.boot.approve.enums.ApproveResult;
import com.example.boot.approve.service.MessageService;
import com.example.boot.approve.view.ApproveInstanceView;
import com.example.boot.approve.view.ApproveNodeRecordView;
import com.example.boot.approve.view.ApproveRunningRecordView;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 审批运行协助服务， 主要做一些通用的检查 通知等业务
 * TODO  可以优化为抽象类 让审批实现类继承
 * Created  on 2022/3/16 13:13:42
 *
 * @author zl
 */
@Slf4j
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
     * 对审批进行包装并检查
     *
     * @param instanceId    审批实例ID
     * @param approveHandle 这里的审批枚举指代的是审批的行为， 主要用来识别【撤销】操作
     */
    public ApproveWrapper findAndCheck(long instanceId, ApproveResult approveHandle) {

        ApproveInstance approveInstance = this.findInstanceAndCheck(instanceId, approveHandle);

        // 按照审批节点等级 查找首个审批中的节点 ,通常审批结束才会发生没有审批节点的情况
        List<ApproveNodeRecord> pendingApprovedNodes = this.findPendingApprovedNode(instanceId);
        ApproveWrapper approveWrapper = new ApproveWrapper(approveInstance, pendingApprovedNodes);

        // TODO 登录人员信息最终需要替换
        User currentLogin = SecurityUtil.getCurrentLogin();
        log.debug("当前登录人员信息{}", currentLogin);

        List<ApproveRunningRecord> runningRecords = approveRunningRecordView.findByNodeRecordId(approveWrapper.getCurrNode().getId());

        // 有异或或者会签的情况，其他人将节点处理了 比如驳回与拒绝 就会找不到记录，因为真正的记录其实在上一节点
        ApproveRunningRecord runningRecord = runningRecords.stream()
                                                           .filter(rd -> rd.getAssignee() == currentLogin.getId())
                                                           .findFirst()
                                                           .orElseThrow(() -> new MesException("已不存在您需要处理的节点记录，如有需要请查看审批详情！"));

        approveWrapper.setApproveRunningRecord(runningRecord).setCurrNodeApproveRunningRecords(runningRecords);
        return approveWrapper;
    }

    /**
     * 查看并检查审批实例是已经结束
     * 检查主要是针对异或， 会签的其他审批人员做了审批结束相关的处理【拒绝】或者发起人取消了审批
     *
     * @param instanceId    审批实例ID
     * @param approveHandle 这里的审批枚举指代的是审批的行为， 主要用来识别【撤销】操作
     */
    public ApproveInstance findInstanceAndCheck(long instanceId, ApproveResult approveHandle) {
        // 对于审批实例而言，有三个过程 待审批 审批中 结束
        ApproveInstance approveInstance = approveInstanceView.getById(instanceId);
        Assert.notNull(approveInstance, "审批实例不存在！实例ID： " + instanceId);
        ApproveResult result = approveInstance.getResult();

        if (ApproveResult.CANCELED.equals(approveHandle) && !ApproveResult.PENDING_APPROVED.equals(result)) {
            throw new MesException(approveInstance.getName() + "审批已经开始，无法取消！");
        }

        // 如果当前审批不是处于待审批与审批中 则审批就代表结束了
        boolean status = ApproveResult.PENDING_APPROVED.equals(result) || ApproveResult.IN_APPROVED.equals(result);
        if (!status) {
            throw new MesException(approveInstance.getName() + "审批已经被处理，如有需要请自行查看审批详情！当前审批状态" + result);
        }
        return approveInstance;
    }

    /**
     * 查找待审批的节点
     *
     * @param instanceId 实例ID
     */
    public List<ApproveNodeRecord> findPendingApprovedNode(long instanceId) {
        return approveNodeRecordView.findPendingApprovedNode(instanceId, ApproveResult.PENDING_APPROVED, null);
    }

    /**
     * 通知审批人
     *
     * @param nextNodeRunningRecords 下一级审批节点记录
     * @param message                消息
     * @param cancel                 true 待审批 false 无需审批
     */
    @Transactional(rollbackFor = Exception.class)
    public void notifiedNextNodeAssignee(List<ApproveRunningRecord> nextNodeRunningRecords, String message, boolean cancel) {
        List<Long> assignees = nextNodeRunningRecords.stream().peek(runningRecord -> {
            ApproveResult result = cancel ? ApproveResult.PENDING_APPROVED : ApproveResult.NO_APPROVAL_REQUIRED;
            runningRecord.setResult(result).setLastModifyTime(new Date());
        }).map(ApproveRunningRecord::getAssignee).collect(Collectors.toList());
        approveRunningRecordView.updateBatchById(nextNodeRunningRecords);
        messageService.notifyMessage(assignees, message);
    }

    @Transactional(rollbackFor = Exception.class)
    public void notifiedAssignee(long initiator, String endMessage) {
        messageService.notifyMessage(initiator, endMessage);
    }

    /**
     * 将其他待审批记录处理为无需审批，主要是针对异或或者会签
     *
     * @param runningRecord                 当前审批运行记录
     * @param currNodeApproveRunningRecords 当前节点下的审批记录
     */
    @Transactional(rollbackFor = Exception.class)
    public void otherPendingApprovedRunningRecordHandler(ApproveRunningRecord runningRecord, List<ApproveRunningRecord> currNodeApproveRunningRecords) {

        // 处理其他审批记录【处于待审批的记录】
        List<ApproveRunningRecord> otherPendingApprovedRunningRecord = currNodeApproveRunningRecords.stream()
                                                                                                    .filter(rd -> !rd.equals(runningRecord))
                                                                                                    .filter(rd -> rd.getResult()
                                                                                                                    .equals(ApproveResult.PENDING_APPROVED))
                                                                                                    .peek(rd -> rd.setResult(
                                                                                                                    ApproveResult.NO_APPROVAL_REQUIRED))
                                                                                                    .collect(Collectors.toList());

        if (otherPendingApprovedRunningRecord.size() == 0) {
            return;
        }

        // TODO 是否有必要通知其他审批人【无需审批了】

        approveRunningRecordView.updateBatchById(otherPendingApprovedRunningRecord);
    }

    /**
     * 重新生成当前节点与驳回节点之间的审批记录
     *
     * @param rejectNodeLevel 驳回节点等级
     * @param currNodeLevel   结束节点等级
     * @param instanceId      实例Id
     */
    @Transactional(rollbackFor = Exception.class)
    public void generateMidRecord(int rejectNodeLevel, int currNodeLevel, long currNodeId, long instanceId) {

        // TODO 是否优化为通过模板生成节点
        List<ApproveNodeRecord> midRecord = approveNodeRecordView.findByInstanceIdAndLevelBetween(instanceId, rejectNodeLevel, currNodeLevel);

        // 复制运行记录
        List<ApproveRunningRecord> runningRecords = midRecord.stream()
                                                             .map(ApproveNodeRecord::getId)
                                                             .flatMap(nodeId -> approveRunningRecordView.findByNodeRecordId(nodeId).stream())
                                                             .collect(Collectors.toList());

        // 初始化审批节点的结果 并标记这些节点为驳回重审
        midRecord.forEach(rd -> rd.setId(null).setResult(ApproveResult.PENDING_APPROVED).setRejectMark(true).setRejectNodeId(currNodeId));
        approveNodeRecordView.saveBatch(midRecord);

        // 复制节点
        runningRecords.forEach(runningRecord -> {
            long newRunningRecordId = findNewRunningRecordId(runningRecord.getNodeRecordName(), midRecord);
            runningRecord.setId(null).setNodeRecordId(newRunningRecordId).setResult(ApproveResult.NONE).setRemarks("").setLastModifyTime(new Date());
        });

        approveRunningRecordView.saveBatch(runningRecords);
    }

    private long findNewRunningRecordId(String name, List<ApproveNodeRecord> newMidRecords) {
        return newMidRecords.stream()
                            .filter(record -> record.getName().equals(name))
                            .mapToLong(ApproveNodeRecord::getId)
                            .findFirst()
                            .orElseThrow(() -> new ResourceNotFoundException("为找到名称为" + name + "的节点"));
    }

    @Getter
    @Accessors(chain = true)
    public static class ApproveWrapper {

        /**
         * 审批实例
         */
        private final ApproveInstance approveInstance;

        /**
         * 待审批节点
         */
        private final List<ApproveNodeRecord> pendingApproveNodeRecords;

        /**
         * 当前节点
         */
        private final ApproveNodeRecord currNode;

        /**
         * 最后一个节点的标识
         */
        private final boolean lastNodeStatus;

        /**
         * 当前节点下的所有审批记录
         */
        @Setter
        private List<ApproveRunningRecord> currNodeApproveRunningRecords;

        /**
         * 当前审批记录
         */
        @Setter
        private ApproveRunningRecord approveRunningRecord;

        ApproveWrapper(ApproveInstance approveInstance, List<ApproveNodeRecord> pendingApproveNodeRecords) {
            this.approveInstance = approveInstance;
            this.pendingApproveNodeRecords = pendingApproveNodeRecords;
            this.lastNodeStatus = computeLastNode();
            this.currNode = getCurrNode();
        }

        public boolean computeLastNode() {
            return this.pendingApproveNodeRecords.size() <= 1;
        }

        public ApproveNodeRecord getCurrNode() {
            if (this.pendingApproveNodeRecords.size() < 1) {
                throw new MesException("审批异常： 审批未结束，但是未找到当前审批节点。请联系管理员查看！当前审批实例 " + approveInstance.toString());
            }
            return this.pendingApproveNodeRecords.get(0);
        }
    }

}

