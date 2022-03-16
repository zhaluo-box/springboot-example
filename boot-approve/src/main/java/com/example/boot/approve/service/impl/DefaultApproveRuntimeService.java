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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 默认的审批运行时服务实现
 * Created  on 2022/3/8 15:15:53
 *
 * @author wmz
 */
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
     * TODO 是否提高方法的单一职责 将其中的一些校验，提前的validator 去做，因为默认采用Feign 调用，所以逻辑的入口在于Controller，
     *
     * @param instanceId 实例Id
     * @param remark     备注消息
     */
    @Override
    @Transactional
    public void approve(long instanceId, String remark) {

        ApproveInstance approveInstance = approveRunningHelper.findInstanceAndCheck(instanceId, ApproveResult.APPROVED);

        // 按照审批节点等级 查找首个审批中的节点 ,通常审批结束才会发生没有审批节点的情况
        List<ApproveNodeRecord> pendingApprovedNodes = approveRunningHelper.findPendingApprovedNode(instanceId);
        ApproveNodeRecord currNode = pendingApprovedNodes.get(0);

        // TODO 登录人员信息最终需要替换
        // 通过登录人员的ID以及节点ID查询对应审批记录
        User currentLogin = SecurityUtil.getCurrentLogin();
        ApproveRunningRecord runningRecord = approveRunningRecordView.findByAssigneeIdAndNodeRecordId(currentLogin.getId(), currNode.getId());
        // 有异或或者会签的情况，其他人将节点处理了 比如驳回与拒绝 就会找不到记录，因为真正的记录其实在上一节点
        if (runningRecord == null) {
            throw new MesException("已不存在您需要处理的节点记录，如有需要请查看审批详情！");
        }

        // 记录审批意见 更新当前记录审批结果
        runningRecord.setResult(ApproveResult.APPROVED).setRemarks(remark).setLastModifyTime(new Date());
        approveRunningRecordView.updateById(runningRecord);

        // 节点变化
        nodeApproveHandler(currNode, runningRecord, ApproveResult.APPROVED);
        if (BaseApproveNode.ApproveType.XOF.equals(currNode.getType())) {
            approveRunningHelper.otherPendingApprovedRunningRecordHandler(currNode, runningRecord);
        }
        nodeApprovedPostHandler(approveInstance, pendingApprovedNodes);
        approveNodeRecordView.updateById(currNode);

        final String approvedMessage = currentLogin.getName() + "通过了您的【" + approveInstance.getName() + "】审批！";
        // 通知发起人【如果当前审批人就是发起人 则不做通知】
        if (!currentLogin.getId().equals(approveInstance.getInitiator())) {
            approveRunningHelper.notifiedAssignee(approveInstance.getInitiator(), approvedMessage);
        }
    }

    @Override
    public void refuse(long instanceId, String remark) {

        ApproveInstance approveInstance = approveRunningHelper.findInstanceAndCheck(instanceId, ApproveResult.REFUSE);
        // 按照审批节点等级 查找首个审批中的节点 ,通常审批结束才会发生没有审批节点的情况
        List<ApproveNodeRecord> pendingApprovedNodes = approveRunningHelper.findPendingApprovedNode(instanceId);
        ApproveNodeRecord currNode = pendingApprovedNodes.get(0);

        // TODO 登录人员信息最终需要替换
        // 通过登录人员的ID以及节点ID查询对应审批记录
        User currentLogin = SecurityUtil.getCurrentLogin();
        ApproveRunningRecord runningRecord = approveRunningRecordView.findByAssigneeIdAndNodeRecordId(currentLogin.getId(), currNode.getId());
        // 有异或或者会签的情况，其他人将节点处理了 比如驳回与拒绝 就会找不到记录，因为真正的记录其实在上一节点
        if (runningRecord == null) {
            throw new MesException("已不存在您需要处理的节点记录，如有需要请查看审批详情！");
        }

        runningRecord.setResult(ApproveResult.REFUSE).setRemarks(remark);
        approveRunningRecordView.updateById(runningRecord);

        nodeApproveHandler(currNode, runningRecord, ApproveResult.REFUSE);
        approveNodeRecordView.updateById(currNode);

        if (!BaseApproveNode.ApproveType.DIRECT.equals(currNode.getType())) {
            approveRunningHelper.otherPendingApprovedRunningRecordHandler(currNode, runningRecord);
        }

        final String approvedMessage = currentLogin.getName() + "拒绝了您的【" + approveInstance.getName() + "】审批！";
        // 通知发起人【如果当前审批人就是发起人 则不做通知】
        if (!currentLogin.getId().equals(approveInstance.getInitiator())) {
            approveRunningHelper.notifiedAssignee(approveInstance.getInitiator(), approvedMessage);
        }

    }

    @Override
    public void cancel(long instanceId, String remark) {

        ApproveInstance approveInstance = approveRunningHelper.findInstanceAndCheck(instanceId, ApproveResult.REFUSE);

        // 按照审批节点等级 查找首个审批中的节点 ,通常审批结束才会发生没有审批节点的情况
        List<ApproveNodeRecord> pendingApprovedNodes = approveRunningHelper.findPendingApprovedNode(instanceId);
        ApproveNodeRecord currNode = pendingApprovedNodes.get(0);

        // TODO 无法定位到底是哪一个审批被撤销了
        // 通知第一级节点审批人 审批已经被取消
        approveRunningHelper.notifiedNextNodeAssignee(currNode, " 【" + approveInstance.getName() + "】审批已经被撤销！", false);
    }

    @Override
    public void reject(long instanceId, String remark, long nodeId) {
        ApproveInstance approveInstance = approveRunningHelper.findInstanceAndCheck(instanceId, ApproveResult.REFUSE);

        // 按照审批节点等级 查找首个审批中的节点 ,通常审批结束才会发生没有审批节点的情况
        List<ApproveNodeRecord> pendingApprovedNodes = approveRunningHelper.findPendingApprovedNode(instanceId);
        ApproveNodeRecord currNode = pendingApprovedNodes.get(0);

        ApproveNodeRecord rejectNode = approveNodeRecordView.getById(nodeId);

        // 查找节点  当前节点>= [需要重新生成的审批记录与审批节点]>= 驳回节点ID 生成节点记录并通知审批人
        List<ApproveNodeRecord> midNodeRecord = approveRunningHelper.findMidNodeRecord(rejectNode.getLevel(), currNode.getLevel(), instanceId);

        // 记录审批意见

        // 通知发起人【如果当前审批人就是发起人 则不做通知】

    }

    @Override
    public void transform(long instanceId, String remark, long transfer) {

    }

    /**
     * 节点审批通过之后的处理
     *
     * @param approveInstance      审批实例
     * @param pendingApprovedNodes 待审批的节点
     */
    private void nodeApprovedPostHandler(ApproveInstance approveInstance, List<ApproveNodeRecord> pendingApprovedNodes) {
        final String message = "有一则【" + approveInstance.getName() + "】审批待您处理!";
        final String endMessage = "【" + approveInstance.getName() + "】审批已结束";
        // 待审批节点数量大于1 代表还有审批节点
        if (pendingApprovedNodes.size() > 1) {
            ApproveNodeRecord nextNode = pendingApprovedNodes.get(1);
            approveRunningHelper.notifiedNextNodeAssignee(nextNode, message, true);
        } else { // 如果没有待审批节点 直接审批结束，更新审批实例记录，通知发起人
            approveInstance.setResult(ApproveResult.APPROVED);
            approveInstanceView.updateById(approveInstance);
            approveRunningHelper.notifiedAssignee(approveInstance.getInitiator(), endMessage);
        }
    }

    /**
     * 节点审批处理
     * 转办不走节点处理，所以下面的逻辑不需要考虑转办的情况
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
            currNode.setResult(result);
            // 会签 ： 判定是否所有人审批通过了，如果所有人审批通过了 则节点审批结果为通过，否则不做处理
            boolean allApproved = runningRecordByCurrNode.stream()
                                                         .filter(record -> !record.equals(runningRecord))
                                                         .allMatch(record -> record.getResult().equals(ApproveResult.APPROVED));
            // 全部通过节点发生变化
            if (ApproveResult.APPROVED.equals(result) && allApproved) {
                currNode.setResult(ApproveResult.APPROVED);
            }
        }
    }

}
