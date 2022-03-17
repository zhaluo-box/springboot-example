package com.example.boot.approve.service.impl;

import com.example.boot.approve.common.exception.MesException;
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
        if (BaseApproveNode.ApproveType.XOF.equals(currNode.getType())) {
            log.info("异或审批当前节点直接通过！");
            approveRunningHelper.otherPendingApprovedRunningRecordHandler(runningRecord, currNodeApproveRunningRecords);
        }

        // 节点通过后的处理 决定是通知下一级审批人 还是审批结束了
        approveInstance.setResult(ApproveResult.IN_APPROVED);
        nodeApprovedPostHandler(approveInstance, pendingApprovedNodes, currNodeApproveRunningRecords, currNode.getType());

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

        ApproveRunningHelper.ApproveWrapper approveWrapper = approveRunningHelper.findAndCheck(instanceId, ApproveResult.APPROVED);
        ApproveInstance approveInstance = approveWrapper.getApproveInstance();
        ApproveNodeRecord currNode = approveWrapper.getCurrNode();
        ApproveRunningRecord runningRecord = approveWrapper.getApproveRunningRecord();
        List<ApproveRunningRecord> currNodeApproveRunningRecords = approveWrapper.getCurrNodeApproveRunningRecords();

        runningRecord.setResult(ApproveResult.REFUSE).setRemarks(remark);
        approveRunningRecordView.updateById(runningRecord);

        // 更新节点变化
        nodeApproveHandler(currNode, runningRecord, ApproveResult.REFUSE);
        approveNodeRecordView.updateById(currNode);

        if (!BaseApproveNode.ApproveType.DIRECT.equals(currNode.getType())) {
            approveRunningHelper.otherPendingApprovedRunningRecordHandler(runningRecord, currNodeApproveRunningRecords);
        }

        // 通知发起人【如果当前审批人就是发起人 则不做通知】
        final String approvedMessage = currentLogin.getName() + "拒绝了您的【" + approveInstance.getName() + "】审批！";
        if (!currentLogin.getId().equals(approveInstance.getInitiator())) {
            approveRunningHelper.notifiedAssignee(approveInstance.getInitiator(), approvedMessage);
        }

        // 审批终止

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancel(long instanceId, String remark) {

        ApproveInstance approveInstance = approveRunningHelper.findInstanceAndCheck(instanceId, ApproveResult.REFUSE);

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

        // 有异或或者会签的情况，其他人将节点处理了 比如驳回与拒绝 就会找不到记录，因为真正的记录其实在上一节点
        if (runningRecord == null) {
            throw new MesException("已不存在您需要处理的节点记录，如有需要请查看审批详情！");
        }

        ApproveNodeRecord rejectNode = approveNodeRecordView.getById(rejectNodeId);

        // 查找节点  当前节点>= [需要重新生成的审批记录与审批节点]>= 驳回节点ID 生成节点记录并通知审批人
        approveRunningHelper.generateMidRecord(rejectNode.getLevel(), currNode.getLevel(), instanceId);

        // 记录审批意见
        currNode.setResult(ApproveResult.REJECTED);
        approveNodeRecordView.updateById(currNode);

        runningRecord.setRemarks(remark)
                     .setLastModifyTime(new Date())
                     .setResult(ApproveResult.REJECTED)
                     .setRejectNodeId(rejectNodeId)
                     .setRejectNodeLevel(rejectNode.getLevel());
        approveRunningRecordView.updateById(runningRecord);

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

        newRunningRecord.setResult(ApproveResult.PENDING_APPROVED).setRemarks("").setId(null);
        approveRunningRecordView.save(newRunningRecord);

        // 原有记录绑定转办审批记录
        runningRecord.setTransfer(transfer)
                     .setTransferMark(true)
                     .setRemarks(remark)
                     .setLastModifyTime(new Date())
                     .setResult(ApproveResult.TRANSFER)
                     .setTransferRecordId(newRunningRecord.getId());
        approveRunningRecordView.updateById(runningRecord);

        // 下一级审批人添加当前转办人员
        approveInstance.getNextAssignees().add(currentLogin.getId());
        approveInstanceView.updateById(approveInstance);

        // 通知审批人
        // 通知发起人【如果当前审批人就是发起人 则不做通知】
        if (!currentLogin.getId().equals(approveInstance.getInitiator())) {
            approveRunningHelper.notifiedAssignee(approveInstance.getInitiator(), "审批已被驳回！审批名称 ：" + approveInstance.getName());
        }
    }

    /**
     * TODO bug 审批实例的最终审批结果有问题 针对与会签和异或需要做处理 不能单独依靠是否有下一个节点做处理
     * 节点审批通过之后的处理
     *
     * @param approveInstance               审批实例
     * @param pendingApprovedNodes          待审批的节点
     * @param currNodeApproveRunningRecords 当前节点下的所有审批记录
     * @param type                          审批节点类型
     */
    private void nodeApprovedPostHandler(ApproveInstance approveInstance, List<ApproveNodeRecord> pendingApprovedNodes,
                                         List<ApproveRunningRecord> currNodeApproveRunningRecords, BaseApproveNode.ApproveType type) {
        // TODO 设置为消息模板常量
        final String message = "有一则【" + approveInstance.getName() + "】审批待您处理!";
        final String endMessage = "【" + approveInstance.getName() + "】审批已结束";

        // 待审批节点数量大于1 代表还有审批节点
        if (pendingApprovedNodes.size() > 1) {
            ApproveNodeRecord nextNode = pendingApprovedNodes.get(1);
            List<ApproveRunningRecord> nextRunningRecords = approveRunningRecordView.findByNodeRecordId(nextNode.getId());
            List<Long> assignees = nextRunningRecords.stream()
                                                     .peek(runningRecord -> runningRecord.setResult(ApproveResult.PENDING_APPROVED)
                                                                                         .setLastModifyTime(new Date()))
                                                     .map(ApproveRunningRecord::getAssignee)
                                                     .collect(Collectors.toList());
            approveInstance.setNextNodeId(nextNode.getId()).setNextNodeName(nextNode.getName()).setNextAssignees(assignees);
            approveRunningHelper.notifiedNextNodeAssignee(nextRunningRecords, message, true);
        } else { // 如果没有待审批节点 直接审批结束，更新审批实例记录，通知发起人
            approveInstance.setResult(ApproveResult.APPROVED);
            approveRunningHelper.notifiedAssignee(approveInstance.getInitiator(), endMessage);
        }
    }

    /**
     * 转办不走节点处理，所以下面的逻辑不需要考虑转办的情况
     * 主要涉及到节点的审批结果 【通过 拒绝 驳回】
     *
     * @param currNode 当前节点
     * @param result   审批通过 拒绝 驳回
     */
    private void nodeApproveHandler(ApproveNodeRecord currNode, ApproveRunningRecord runningRecord, ApproveResult result) {
        // 直签与异或 更新节点审批结果为通过，
        if (BaseApproveNode.ApproveType.DIRECT.equals(currNode.getType()) || BaseApproveNode.ApproveType.XOF.equals(currNode.getType())) {
            currNode.setResult(result);
        } else {
            List<ApproveRunningRecord> runningRecordByCurrNode = approveRunningRecordView.findByNodeRecordId(currNode.getId());
            // @formatter:off 会签 ： 判定是否所有人审批通过了，如果所有人审批通过了 则节点审批结果为通过，否则不做处理
            boolean allApproved = runningRecordByCurrNode.stream()
                                                         .filter(record -> !record.equals(runningRecord))
                                                         .peek(System.out::println)
                                                         .allMatch(record -> record.getResult().equals(ApproveResult.APPROVED));
            // @formatter:on 全部通过节点发生变化
            if (ApproveResult.APPROVED.equals(result) && allApproved) {
                currNode.setResult(ApproveResult.APPROVED);
            }
        }
    }

}
