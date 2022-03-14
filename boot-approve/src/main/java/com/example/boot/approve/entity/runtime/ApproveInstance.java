package com.example.boot.approve.entity.runtime;

import com.example.boot.approve.enums.ApproveResult;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 审批实例
 * Created  on 2022/2/24 17:17:19
 *
 * @author zl
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(of = "id")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApproveInstance {

    private Long id;

    /**
     * 最终的审批结果,默认待审批
     * 开始： 默认是待审批
     * 过程： 审批中 【有一个节点审批就转为审批中】
     * 结束： 审批通过， 拒绝， 撤销【只能在没有任何一级节点审批的时候才允许撤销】
     */
    private ApproveResult result = ApproveResult.PENDING_APPROVED;

    /**
     * 审批模板Id
     */
    private long approveModelId;


    /**
     * 发起原因
     */
    private String reason;

    /**
     * 审批发起人
     */
    private String initiator;

    /**
     * 审批发起时间
     */
    private Date initiateTime;

    /**
     * 参数ID [通常发起审批的节点会绑定审批实例ID， 而审批实例也会反向绑定发起审批的ID，
     * 用于审批历史的回溯， 但是发起审批哪里通常只会绑定最近一次的审批实例ID]
     */
    private String paramId;

    /**
     * 下一级待审批节点ID或者审批中的当前节点ID【会签】
     */
    private long nextNodeId;

    /**
     * 下一级审批节点名称或者审批中的当前节点【会签】
     */
    private String nextNodeName;

}
