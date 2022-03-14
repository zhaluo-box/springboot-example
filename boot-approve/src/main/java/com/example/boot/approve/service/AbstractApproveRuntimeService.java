package com.example.boot.approve.service;

/**
 * Created  on 2022/3/9 11:11:48
 *
 * @author zl
 */
public abstract class AbstractApproveRuntimeService implements ApproveRuntimeService {

    protected abstract ApproveRuntimeService getApproveRuntimeService();

    protected void approvePre(long instanceId, String remark) {
    }

    protected void approvePost(long instanceId, String remark) {
    }

    @Override
    public void approve(long instanceId, String remark) {
        approvePre(instanceId, remark);
        getApproveRuntimeService().approve(instanceId, remark);
        approvePost(instanceId, remark);
    }

    protected void refusePre(long instanceId, String remark) {
    }

    protected void refusePost(long instanceId, String remark) {

    }

    @Override
    public void refuse(long instanceId, String remark) {
        refusePre(instanceId, remark);
        getApproveRuntimeService().refuse(instanceId, remark);
        refusePost(instanceId, remark);
    }

    protected void cancelPre(long instanceId, String remark) {
        // 子类需要直接重写;
    }

    protected void cancelPost(long instanceId, String remark) {

    }

    @Override
    public void cancel(long instanceId, String remark) {
        cancelPre(instanceId, remark);
        getApproveRuntimeService().cancel(instanceId, remark);
        cancelPost(instanceId, remark);
    }

    protected void rejectPre(long instanceId, String remark, long nodeId) {
    }

    protected void rejectPost(long instanceId, String remark, long nodeId) {

    }

    @Override
    public void reject(long instanceId, String remark, long nodeId) {
        rejectPre(instanceId, remark, nodeId);
        getApproveRuntimeService().reject(instanceId, remark, nodeId);
        rejectPost(instanceId, remark, nodeId);
    }

    protected void transformPre(long instanceId, String remark, long transfer) {

    }

    protected void transformPost(long instanceId, String remark, long transfer) {

    }

    @Override
    public void transform(long instanceId, String remark, long transfer) {
        transformPre(instanceId, remark, transfer);
        getApproveRuntimeService().transform(instanceId, remark, transfer);
        transformPost(instanceId, remark, transfer);
    }
}
