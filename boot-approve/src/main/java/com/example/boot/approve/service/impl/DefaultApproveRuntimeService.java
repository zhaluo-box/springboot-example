package com.example.boot.approve.service.impl;

import com.example.boot.approve.common.utils.SecurityUtil;
import com.example.boot.approve.entity.User;
import com.example.boot.approve.entity.common.BaseApproveNode;
import com.example.boot.approve.entity.runtime.ApproveInstance;
import com.example.boot.approve.entity.runtime.ApproveNodeRecord;
import com.example.boot.approve.entity.runtime.ApproveRunningRecord;
import com.example.boot.approve.enums.ApproveResult;
import com.example.boot.approve.service.ApproveRuntimeService;
import com.example.boot.approve.view.ApproveInstanceView;
import com.example.boot.approve.view.ApproveNodeRecordView;
import com.example.boot.approve.view.ApproveRunningRecordView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * TODO 后期的扩展优化 基于模板方式，预留钩子方法
 * 默认的审批运行时服务实现
 * Created  on 2022/3/8 15:15:53
 *
 * @author wmz
 */
@Slf4j
@Service
public class DefaultApproveRuntimeService implements ApproveRuntimeService {

    @Autowired
    private ApproveNodeRecordView approveNodeRecordView;

    @Autowired
    private ApproveInstanceView approveInstanceView;

    @Autowired
    private ApproveRunningRecordView approveRunningRecordView;

    @Autowired
    private ApproveRunningHelper approveRunningHelper;

    /**
     * 审批通过的逻辑
     * 1.查看并检查审批
     * 2.记录审批意见
     * 3.审批节点变化
     * 4.审批实例变化
     * 5.通知
     *
     * @param instanceId 实例Id
     * @param remark     备注消息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void approve(long instanceId, String remark) {

        // TODO 登录人员信息最终需要替换
        User currentLogin = SecurityUtil.getCurrentLogin();
        log.debug("当前登录人员信息{}", currentLogin);

        ApproveRunningHelper.ApproveWrapper approveWrapper = approveRunningHelper.findAndCheck(instanceId, ApproveResult.APPROVED);
        ApproveInstance approveInstance = approveWrapper.getApproveInstance();
        List<ApproveNodeRecord> pendingApprovedNodes = approveWrapper.getPendingApproveNodeRecords();
        ApproveNodeRecord currNode = approveWrapper.getCurrNode();
        ApproveRunningRecord runningRecord = approveWrapper.getApproveRunningRecord();
        List<ApproveRunningRecord> currNodeApproveRunningRecords = approveWrapper.getCurrNodeApproveRunningRecords();

        log.debug("开始审批");

        // 记录审批意见 更新当前记录审批结果
        runningRecord.setResult(ApproveResult.APPROVED).setRemarks(remark).setLastModifyTime(new Date());
        approveRunningRecordView.updateById(runningRecord);

        // 节点变化
        nodeApproveHandler(currNode, runningRecord, ApproveResult.APPROVED);
        approveNodeRecordView.updateById(currNode);

        // 通过 异或需要将另一位审批人置为无需审批
        if (BaseApproveNode.ApproveType.XOF.equals(currNode.getType())) {
            approveRunningHelper.otherPendingApprovedRunningRecordHandler(runningRecord, currNodeApproveRunningRecords);
        }

        // 节点通过后的处理 决定是通知下一级审批人 还是审批结束了
        approveInstance.setResult(ApproveResult.IN_APPROVED).setLastModifyTime(new Date());
        pendingApprovedNodes.remove(currNode);
        nodePostHandler(approveInstance, pendingApprovedNodes, currNode);
        approveInstanceView.updateById(approveInstance);

        final String approvedMessage = currentLogin.getName() + "通过了您的【" + approveInstance.getName() + "】审批！";
        // 通知发起人【如果当前审批人就是发起人 则不做通知】
        if (!currentLogin.getId().equals(approveInstance.getInitiator())) {
            approveRunningHelper.notifiedAssignee(approveInstance.getInitiator(), approvedMessage);
        }

        log.debug("结束审批");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void refuse(long instanceId, String remark) {

        // TODO 登录人员信息最终需要替换
        // 通过登录人员的ID以及节点ID查询对应审批记录
        User currentLogin = SecurityUtil.getCurrentLogin();

        ApproveRunningHelper.ApproveWrapper approveWrapper = approveRunningHelper.findAndCheck(instanceId, ApproveResult.REFUSE);
        ApproveInstance approveInstance = approveWrapper.getApproveInstance();
        ApproveNodeRecord currNode = approveWrapper.getCurrNode();
        ApproveRunningRecord runningRecord = approveWrapper.getApproveRunningRecord();
        List<ApproveRunningRecord> currNodeApproveRunningRecords = approveWrapper.getCurrNodeApproveRunningRecords();

        runningRecord.setResult(ApproveResult.REFUSE).setRemarks(remark);
        approveRunningRecordView.updateById(runningRecord);

        // 更新节点
        currNode.setResult(ApproveResult.REFUSE);
        approveNodeRecordView.updateById(currNode);

        // 拒绝  有多个审批的都需要将其他审批人置为无需处理
        if (!BaseApproveNode.ApproveType.DIRECT.equals(currNode.getType())) {
            approveRunningHelper.otherPendingApprovedRunningRecordHandler(runningRecord, currNodeApproveRunningRecords);
        }

        // 通知发起人【如果当前审批人就是发起人 则不做通知】
        final String approvedMessage = currentLogin.getName() + "拒绝了您的【" + approveInstance.getName() + "】审批！";
        if (!currentLogin.getId().equals(approveInstance.getInitiator())) {
            approveRunningHelper.notifiedAssignee(approveInstance.getInitiator(), approvedMessage);
        }

        // 审批终止
        approveInstance.setResult(ApproveResult.REFUSE);
        approveInstanceView.updateById(approveInstance);

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancel(long instanceId, String remark) {

        ApproveInstance approveInstance = approveRunningHelper.findInstanceAndCheck(instanceId, ApproveResult.CANCELED);

        // 按照审批节点等级 查找首个审批中的节点 ,通常审批结束才会发生没有审批节点的情况
        List<ApproveNodeRecord> pendingApprovedNodes = approveRunningHelper.findPendingApprovedNode(instanceId);
        ApproveNodeRecord currNode = pendingApprovedNodes.get(0);

        List<ApproveRunningRecord> nextNodeRunningRecords = approveRunningRecordView.findByNodeRecordId(currNode.getId());

        // TODO 无法定位到底是哪一个审批被撤销了
        // 通知第一级节点审批人 审批已经被取消
        approveRunningHelper.notifiedNextNodeAssignee(nextNodeRunningRecords, " 【" + approveInstance.getName() + "】审批已经被撤销！取消的原因" + remark, false);

        // 填写取消的原因
        approveInstance.setCancelReason(remark).setLastModifyTime(new Date()).setResult(ApproveResult.CANCELED);
        approveInstanceView.updateById(approveInstance);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void reject(long instanceId, String remark, long rejectNodeId) {

        ApproveRunningHelper.ApproveWrapper approveWrapper = approveRunningHelper.findAndCheck(instanceId, ApproveResult.APPROVED);
        ApproveInstance approveInstance = approveWrapper.getApproveInstance();
        ApproveNodeRecord currNode = approveWrapper.getCurrNode();
        ApproveRunningRecord runningRecord = approveWrapper.getApproveRunningRecord();
        List<ApproveRunningRecord> currNodeApproveRunningRecords = approveWrapper.getCurrNodeApproveRunningRecords();

        ApproveNodeRecord rejectNode = approveNodeRecordView.getById(rejectNodeId);

        // 记录审批意见
        runningRecord.setRemarks(remark).setLastModifyTime(new Date()).setResult(ApproveResult.REJECTED);
        //                     .setRejectNodeId(rejectNodeId)
        //                     .setRejectNodeLevel(rejectNode.getLevel());
        approveRunningRecordView.updateById(runningRecord);

        // 驳回  有多个审批的都需要将其他审批人置为无需处理
        if (!BaseApproveNode.ApproveType.DIRECT.equals(currNode.getType())) {
            approveRunningHelper.otherPendingApprovedRunningRecordHandler(runningRecord, currNodeApproveRunningRecords);
        }

        // 记录节点结果
        currNode.setResult(ApproveResult.REJECTED);
        approveNodeRecordView.updateById(currNode);

        // 生成节点节点  当前节点>= [需要重新生成的审批记录与审批节点]>= 驳回节点ID 生成节点记录并通知审批人
        approveRunningHelper.generateMidRecord(rejectNode.getLevel(), currNode.getLevel(), currNode.getId(), instanceId);

        // 通知下一级（驳回节点的审批人）审批人进行审批
        List<ApproveNodeRecord> pendingApprovedNode = approveRunningHelper.findPendingApprovedNode(instanceId);
        nodePostHandler(approveInstance, pendingApprovedNode, currNode);
        approveInstanceView.updateById(approveInstance);

        // TODO 登录人员信息最终需要替换
        // 通过登录人员的ID以及节点ID查询对应审批记录
        User currentLogin = SecurityUtil.getCurrentLogin();

        // 通知发起人【如果当前审批人就是发起人 则不做通知】
        if (!currentLogin.getId().equals(approveInstance.getInitiator())) {
            approveRunningHelper.notifiedAssignee(approveInstance.getInitiator(), "审批已被驳回！审批名称 ：" + approveInstance.getName());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void transform(long instanceId, String remark, long transfer) {

        // TODO 登录人员信息最终需要替换
        User currentLogin = SecurityUtil.getCurrentLogin();
        log.debug("当前登录人员信息{}", currentLogin);

        ApproveRunningHelper.ApproveWrapper approveWrapper = approveRunningHelper.findAndCheck(instanceId, ApproveResult.APPROVED);
        ApproveInstance approveInstance = approveWrapper.getApproveInstance();
        ApproveRunningRecord runningRecord = approveWrapper.getApproveRunningRecord();

        // 生成新的审批运行记录
        ApproveRunningRecord newRunningRecord = new ApproveRunningRecord();
        BeanUtils.copyProperties(runningRecord, newRunningRecord);

        newRunningRecord.setResult(ApproveResult.PENDING_APPROVED).setRemarks("").setId(null).setAssignee(transfer);
        approveRunningRecordView.save(newRunningRecord);

        // 原有记录绑定转办审批记录
        runningRecord.setTransfer(transfer)
                     .setTransferMark(true)
                     .setRemarks(remark).setLastModifyTime(new Date()).setResult(ApproveResult.TRANSFER).setTransferRecordId(newRunningRecord.getId());
        approveRunningRecordView.updateById(runningRecord);

        // 下一级审批人添加当前转办人员
        approveInstance.getNextAssignees().add(transfer);
        approveInstanceView.updateById(approveInstance);

        // 通知审批人
        approveRunningHelper.notifiedAssignee(transfer, currentLogin.getName() + "转办了一则审批给您！审批名称 ：" + approveInstance.getName());

        // 通知发起人【如果当前审批人就是发起人 则不做通知】
        if (!currentLogin.getId().equals(approveInstance.getInitiator())) {
            approveRunningHelper.notifiedAssignee(approveInstance.getInitiator(), "审批已被转办！审批名称 ：" + approveInstance.getName());
        }
    }

    /**
     * 节点后置处理
     * 情景： 当前节点审批结果不是通过也不是驳回， 不做任何处理
     * 当前节点审批通过：检查是否还有待审批节点： 有 通知并更新实例下一级审批节点信息，没有则代表审批结束
     *
     * @param approveInstance      审批实例
     * @param pendingApprovedNodes 待审批的节点
     * @param currNode             当前节点
     */
    private void nodePostHandler(ApproveInstance approveInstance, List<ApproveNodeRecord> pendingApprovedNodes, ApproveNodeRecord currNode) {

        ApproveResult currNodeResult = currNode.getResult();

        // 审批通过与驳回之外的结果直接返回
        if (!currNodeResult.equals(ApproveResult.APPROVED) && !currNodeResult.equals(ApproveResult.REJECTED)) {
            return;
        }

        // TODO 设置为消息模板常量
        final String message = "有一则【" + approveInstance.getName() + "】审批待您处理!";
        final String endMessage = "【" + approveInstance.getName() + "】审批已结束";

        if (pendingApprovedNodes.size() == 0) {
            approveInstance.setResult(ApproveResult.APPROVED);
            approveRunningHelper.notifiedAssignee(approveInstance.getInitiator(), endMessage);
            return;
        }

        //  待审批节点数量大于0 代表还有审批节点 且当前节点是通过的
        ApproveNodeRecord nextNode = pendingApprovedNodes.get(0);
        List<ApproveRunningRecord> nextRunningRecords = approveRunningRecordView.findByNodeRecordId(nextNode.getId());
        List<Long> assignees = nextRunningRecords.stream()
                                                 .peek(runningRecord -> runningRecord.setResult(ApproveResult.PENDING_APPROVED).setLastModifyTime(new Date()))
                                                 .map(ApproveRunningRecord::getAssignee)
                                                 .collect(Collectors.toList());

        //  通知下一节点 并更新实例下一节点的审批人信息
        approveInstance.setNextNodeId(nextNode.getId()).setNextNodeName(nextNode.getName()).setNextAssignees(assignees);
        approveRunningHelper.notifiedNextNodeAssignee(nextRunningRecords, message, true);

    }

    /**
     * 转办不走节点处理，所以下面的逻辑不需要考虑转办的情况
     * 主要涉及到节点的审批结果 【通过 拒绝 驳回】
     *
     * @param currNode 当前节点
     * @param result   审批结果
     */
    private void nodeApproveHandler(ApproveNodeRecord currNode, ApproveRunningRecord runningRecord, ApproveResult result) {
        // 直签与异或 更新节点审批结果为通过，
        if (BaseApproveNode.ApproveType.DIRECT.equals(currNode.getType()) || BaseApproveNode.ApproveType.XOF.equals(currNode.getType())) {
            currNode.setResult(result);
            return;
        }
        List<ApproveRunningRecord> runningRecordByCurrNode = approveRunningRecordView.findByNodeRecordId(currNode.getId());
        //   会签 ： 判定是否所有人审批通过了，如果所有人审批通过了 则节点审批结果为通过，否则不做处理
        boolean allApproved = runningRecordByCurrNode.stream()
                                                     .filter(record -> !record.equals(runningRecord))
                                                     .peek(System.out::println)
                                                     .allMatch(record -> record.getResult().equals(ApproveResult.APPROVED));
        //   会签所有人同意 节点才算通过
        if (ApproveResult.APPROVED.equals(result) && allApproved) {
            currNode.setResult(ApproveResult.APPROVED);
        }

    }

}
