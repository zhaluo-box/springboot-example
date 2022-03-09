package com.example.boot.approve.enums;

/**
 * Created  on 2022/3/8 16:16:03
 *
 * @author zl
 */
public enum ApproveResult {

    IN_APPROVED, // 审批中
    APPROVED, // 审批通过
    REJECTED, // 拒绝的
    CANCELED,  // 审批撤销
    TRANSFER, // 转办的
    REFUSE, // 拒绝的
    INVALID, //作废【整个流程审批通过之后才会出现作废按钮， 逻辑归档】
    PENDING_APPROVED; // 待审批的

}
