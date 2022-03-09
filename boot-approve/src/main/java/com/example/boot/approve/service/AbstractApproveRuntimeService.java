package com.example.boot.approve.service;

/**
 * Created  on 2022/3/9 11:11:48
 *
 * @author zl
 */
public abstract class AbstractApproveRuntimeService implements ApproveRuntimeService {

    protected ApproveRuntimeService approveRuntimeService;

    /**
     * 子类实现赋予父类
     *
     * @param approveRuntimeService 审批运行时服务
     */
    protected abstract void setApproveRuntimeService(ApproveRuntimeService approveRuntimeService);

    protected void approvePre(long instanceId, String remark) {
    }

    protected void approvePost(long instanceId, String remark) {
    }

    @Override
    public void approve(long instanceId, String remark) {
        approvePre(instanceId, remark);
        approveRuntimeService.approve(instanceId, remark);
        approvePost(instanceId, remark);
    }

    protected void refusePre(long instanceId, String remark) {
    }

    protected void refusePost(long instanceId, String remark) {

    }

    @Override
    public void refuse(long instanceId, String remark) {
        refusePre(instanceId, remark);
        approveRuntimeService.refuse(instanceId, remark);
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
        approveRuntimeService.cancel(instanceId, remark);
        cancelPost(instanceId, remark);
    }

    protected void rejectPre(long instanceId, String remark, long nodeId) {
    }

    protected void rejectPost(long instanceId, String remark, long nodeId) {

    }

    @Override
    public void reject(long instanceId, String remark, long nodeId) {
        rejectPre(instanceId, remark, nodeId);
        approveRuntimeService.reject(instanceId, remark, nodeId);
        rejectPost(instanceId, remark, nodeId);
    }

    protected void transformPre(long instanceId, String remark, long transfer) {

    }

    protected void transformPost(long instanceId, String remark, long transfer) {

    }

    @Override
    public void transform(long instanceId, String remark, long transfer) {
        transformPre(instanceId, remark, transfer);
        approveRuntimeService.transform(instanceId, remark, transfer);
        transformPost(instanceId, remark, transfer);
    }
}
