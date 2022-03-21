package com.example.boot.approve.entity.runtime;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
@TableName(value = "approve_node_record", autoResultMap = true)
public class ApproveNodeRecord extends BaseApproveNode {

    /**
     * ID
     */
    @TableId(type = IdType.AUTO)
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

    /**
     * 审批配置节点ID
     */
    private long nodeConfigId;

    /**
     * 驳回重审标识， 默认为false, 在驳回操作时， 置为true 代表新生成的节点是驳回重审的节点
     */
    private boolean rejectMark;

    /**
     * * 这里存储的是发起驳回的那个节点的ID
     * * 只有具有驳回重审标识的这个字段的ID 才有值>0
     */
    private long rejectNodeId;

}
