package com.example.boot.approve.entity.runtime;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.boot.approve.common.mybaitsplus.ListToStringTypeHandler;
import com.example.boot.approve.enums.ApproveResult;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

/**
 * 审批历史 与审批配置基本上相同 多了转办人员， 转办Id
 * Created  on 2022/2/24 17:17:24
 *
 * @author zl
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(of = "id")
@JsonIgnoreProperties(ignoreUnknown = true)
@TableName(value = "approve_running_record", autoResultMap = true)
public class ApproveRunningRecord {

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 审批节点记录ID
     */
    private long nodeRecordId;

    /**
     * 审批实例ID 方便查询 减少表关联
     */
    private long instanceId;

    /**
     * 审批指定人
     */
    private long assignee;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 审批结果
     */
    private ApproveResult result;

    /**
     * 权限标识符【具体能够做什么操作由具体业务限定】
     */
    private String permission;

    /**
     * 排序号
     */
    private int orderNum;

    /**
     * 转办标记
     */
    private boolean transferMark;

    /**
     * 转办人 [可以为空] 只能转办给一个人
     */
    private long transfer;

    /**
     * 是否支持转办，默认不支持
     */
    private boolean supportTransfer;

    /**
     * 转办人员候选人员 方便前端渲染,
     */
    @TableField(typeHandler = ListToStringTypeHandler.class)
    private List<Long> transferCandidates = new ArrayList<>();

    /**
     * 转办记录ID 自关联
     */
    private long transferRecordId;

    /**
     * 驳回节点ID TODO 后期验证是否冗余
     */
    private long rejectNodeId;

    /**
     * 驳回节点等级
     */
    private long rejectNodeLevel;

}
