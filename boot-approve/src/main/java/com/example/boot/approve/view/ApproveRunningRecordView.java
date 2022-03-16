package com.example.boot.approve.view;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.boot.approve.entity.runtime.ApproveRunningRecord;
import com.example.boot.approve.enums.ApproveResult;
import com.example.boot.approve.mapper.ApproveRunningRecordMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created  on 2022/3/15 11:11:35
 *
 * @author zl
 */
@Service
public class ApproveRunningRecordView extends ServiceImpl<ApproveRunningRecordMapper, ApproveRunningRecord> {

    /**
     * 查找登录人员在当前审批节点下的的待审批记录
     *
     * @param assigneeId   登录ID == 审批人ID
     * @param nodeRecordId 待审批节点ID
     */
    public ApproveRunningRecord findByAssigneeIdAndNodeRecordId(long assigneeId, long nodeRecordId) {
        return getBaseMapper().selectOne(new QueryWrapper<ApproveRunningRecord>().lambda()
                                                                                 .eq(ApproveRunningRecord::getNodeRecordId, nodeRecordId)
                                                                                 .eq(ApproveRunningRecord::getAssignee, assigneeId));
    }

    /**
     * 当前节点下的所有审批记录
     *
     * @param nodeRecordId 审批节点记录ID
     */
    public List<ApproveRunningRecord> findByNodeRecordId(long nodeRecordId) {
        return getBaseMapper().selectList(new QueryWrapper<ApproveRunningRecord>().lambda().eq(ApproveRunningRecord::getNodeRecordId, nodeRecordId));
    }

    /**
     * 查找当前节点下为待审批的记录
     *
     * @param id           审批记录Id
     * @param nodeRecordId 节点记录Id
     */
    public List<ApproveRunningRecord> findAllByNodeIdAndNotEqualIdAndResultEqPendingApproved(long nodeRecordId, long id) {
        return getBaseMapper().selectList(new QueryWrapper<ApproveRunningRecord>().lambda()
                                                                                  .eq(ApproveRunningRecord::getNodeRecordId, nodeRecordId)
                                                                                  .eq(ApproveRunningRecord::getResult, ApproveResult.PENDING_APPROVED)
                                                                                  .ne(id > 0, ApproveRunningRecord::getId, id));
    }
}
