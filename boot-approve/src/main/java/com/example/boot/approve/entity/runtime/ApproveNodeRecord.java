package com.example.boot.approve.entity.runtime;

import com.example.boot.approve.entity.common.BaseApproveNode;
import com.example.boot.approve.enums.ApproveResult;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 审批节点记录，与审批实例相关联
 * Created  on 2022/3/9 10:10:49
 *
 * @author zl
 * @see ApproveInstance
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApproveNodeRecord extends BaseApproveNode {

    /**
     * ID
     */
    private Long id;

    /**
     * 当前节点的审批结果, 默认待审批
     * 特殊情况： 会签有一个在审批就会变为审批中
     * 会签： 全部通过为通过，一个不通过则整个节点不通过
     * 异或： 一个通过则通过，一个不通过则整个节点不通过
     */
    private ApproveResult result = ApproveResult.PENDING_APPROVED;

    /**
     * 审批实例ID
     */
    private long instanceId;

}
