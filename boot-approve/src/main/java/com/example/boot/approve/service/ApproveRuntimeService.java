package com.example.boot.approve.service;

/**
 * 审批流程运行接口
 * Created  on 2022/3/8 20:20:47
 *
 * @author zl
 */
public interface ApproveRuntimeService {

    /**
     * 审批
     *
     * @param instanceId 实例Id
     * @param remark     备注消息
     */
    void approve(long instanceId, String remark);

    /**
     * 拒绝
     *
     * @param instanceId 实例Id
     * @param remark     备注消息
     */
    void refuse(long instanceId, String remark);

    /**
     * 撤销（检查所有节点都未被审批的时候才允许撤销）
     *
     * @param instanceId 审批实例ID
     * @param remark     备注消息
     */
    void cancel(long instanceId, String remark);

    /**
     * 驳回
     * 从当前节点到驳回节点，新增历史记录
     *
     * @param instanceId 实例Id
     * @param remark     备注消息
     * @param level      驳回节点
     */
    void reject(long instanceId, String remark, long level);

    /**
     * 转办
     *
     * @param instanceId 实例Id
     * @param remark     备注消息
     * @param transfer   转办人Id
     */
    void transform(long instanceId, String remark, long transfer);
}
