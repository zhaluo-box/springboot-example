package com.example.boot.simple.framework.service;

/**
 * Created  on 2022/2/24 16:16:39
 *
 * @author zl
 */
public interface ApproveService {

    /**
     * 审批
     *
     * @param approveInstanceId 审批实例Id
     */
    void approve(long approveInstanceId);

    /**
     * 取消审批
     *
     * @param approveInstanceId 审批实例Id
     */
    boolean cancel(long approveInstanceId);

    /**
     * 转办
     *
     * @param approveInstanceId 审批实例Id
     * @param transfer          转办人员编号
     * @param message           备注消息
     */
    void transfer(long approveInstanceId, long transfer, String message);

    /**
     * 驳回
     *
     * @param approveInstanceId 审批实例Id
     * @param approveNodeId     驳回节点Id
     * @param message           备注消息
     */
    void reject(long approveInstanceId, long approveNodeId, String message);

}
