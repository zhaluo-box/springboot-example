package com.example.boot.approve.enums;

/**
 * Created  on 2022/3/8 16:16:03
 *
 * @author zl
 */
public enum ApproveResult {

    IN_APPROVED, // 审批中
    APPROVED, // 已审批的
    REJECTED, // 拒绝的
    CANCELED,  // 取消的
    TRANSFER, // 转办的
    REFUSE, // 拒绝的
    PENDING_APPROVED; // 待审批的

}
