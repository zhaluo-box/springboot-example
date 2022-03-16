package com.example.boot.approve.view;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.boot.approve.entity.common.BaseApproveNode;
import com.example.boot.approve.entity.runtime.ApproveNodeRecord;
import com.example.boot.approve.enums.ApproveResult;
import com.example.boot.approve.mapper.ApproveNodeRecordMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created  on 2022/3/15 11:11:32
 *
 * @author zl
 */
@Repository
public class ApproveNodeRecordView extends ServiceImpl<ApproveNodeRecordMapper, ApproveNodeRecord> {

    /**
     * 查找下一个待审批的节点
     *
     * @param instanceId 实例Id
     * @return 下一个审批节点
     */
    public List<ApproveNodeRecord> findNextPendingApprovedNode(long instanceId, ApproveResult approveResult) {
        return getBaseMapper().selectList(new QueryWrapper<ApproveNodeRecord>().lambda()
                                                                               .eq(instanceId > 0, ApproveNodeRecord::getInstanceId, instanceId)
                                                                               .eq(approveResult != null, ApproveNodeRecord::getResult,
                                                                                   ApproveResult.PENDING_APPROVED)
                                                                               .orderByAsc(BaseApproveNode::getLevel));
    }

    /**
     * 查询当前与驳回节点的中间节点
     *
     * @param instanceId      实例ID
     * @param rejectNodeLevel 驳回节点ID
     * @param currNodeLevel   当前节点ID
     */
    public List<ApproveNodeRecord> findByInstanceIdAndLevelBetween(long instanceId, int rejectNodeLevel, int currNodeLevel) {
        return getBaseMapper().selectList(new QueryWrapper<ApproveNodeRecord>().lambda()
                                                                               .eq(ApproveNodeRecord::getInstanceId, instanceId)
                                                                               .between(BaseApproveNode::getLevel, rejectNodeLevel, currNodeLevel));
    }
}
