package com.example.boot.approve.view;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.boot.approve.entity.common.BaseApproveNode;
import com.example.boot.approve.entity.runtime.ApproveNodeRecord;
import com.example.boot.approve.enums.ApproveResult;
import com.example.boot.approve.mapper.ApproveNodeRecordMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

/**
 * Created  on 2022/3/15 11:11:32
 *
 * @author zl
 */
@Repository
public class ApproveNodeRecordView extends ServiceImpl<ApproveNodeRecordMapper, ApproveNodeRecord> {

    /**
     * 查找 待审批的节点
     *
     * @param instanceId    实例Id
     * @param approveResult 审批结果
     * @param rejectMark    驳回重审标识
     * @return 审批节点
     */
    public List<ApproveNodeRecord> findPendingApprovedNode(long instanceId, ApproveResult approveResult, Boolean rejectMark) {
        return getBaseMapper().selectList(new QueryWrapper<ApproveNodeRecord>().lambda()
                                                                               .eq(instanceId > 0, ApproveNodeRecord::getInstanceId, instanceId)
                                                                               .eq(Objects.nonNull(approveResult), ApproveNodeRecord::getResult, approveResult)
                                                                               .eq(Objects.nonNull(rejectMark), ApproveNodeRecord::isRejectMark, rejectMark)
                                                                               .orderByAsc(BaseApproveNode::getLevel));
    }

    /**
     * 查询当前与驳回节点的中间节点【标准节点。需要对驳回节点过滤】
     *
     * @param instanceId      实例ID
     * @param rejectNodeLevel 驳回节点ID
     * @param currNodeLevel   当前节点ID
     */
    public List<ApproveNodeRecord> findByInstanceIdAndLevelBetween(long instanceId, int rejectNodeLevel, int currNodeLevel) {
        return getBaseMapper().selectList(new QueryWrapper<ApproveNodeRecord>().lambda()
                                                                               .eq(ApproveNodeRecord::getInstanceId, instanceId)
                                                                               .eq(ApproveNodeRecord::isRejectMark, false) // 查询非驳回节点，也就是标准节点
                                                                               .between(BaseApproveNode::getLevel, rejectNodeLevel, currNodeLevel));
    }

    /**
     * 实例ID
     *
     * @param instanceId    实例ID
     * @param approveResult 审批结果
     */
    public List<ApproveNodeRecord> list(long instanceId, ApproveResult approveResult, Boolean rejectMark) {
        return list(new QueryWrapper<ApproveNodeRecord>().lambda()
                                                         .eq(instanceId > 0, ApproveNodeRecord::getInstanceId, instanceId)
                                                         .eq(Objects.nonNull(approveResult), ApproveNodeRecord::getResult, approveResult)
                                                         .eq(Objects.nonNull(rejectMark), ApproveNodeRecord::isRejectMark, rejectMark));
    }

}
