package com.example.boot.approve.enums;

/**
 * Created  on 2022/3/8 16:16:03
 *
 * @author zl
 */
public enum ApproveResult {

    NONE, // 审批意见初始的状态

    PENDING_APPROVED, // 待审批的
    IN_APPROVED, // 审批中

    APPROVED, // 审批通过
    REJECTED, // 驳回的
    REFUSE, // 拒绝的

    TRANSFER, // 转办的

    CANCELED,  // 审批撤销

    NO_APPROVAL_REQUIRED, //无需审批的 异或 会签 其他人做出了决定了，就无需做审批了，主要用于审批管理中 待我审批，我已审批的 我发起的做筛选
    INVALID; //作废【整个流程审批通过之后才会出现作废按钮， 逻辑归档】
}
