package com.example.boot.approve.validator;

import com.example.boot.approve.common.exception.ResourceConflictException;
import com.example.boot.approve.common.exception.ResourceNotFoundException;
import com.example.boot.approve.entity.config.ApproveModel;
import com.example.boot.approve.entity.runtime.ApproveNodeRecord;
import com.example.boot.approve.enums.ApproveResult;
import com.example.boot.approve.service.ApproveConfigManager;
import com.example.boot.approve.view.ApproveNodeRecordView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Objects;

/**
 * Created  on 2022/3/14 17:17:24
 *
 * @author zl
 */
@Service
public class ApproveRecordValidator {

    @Autowired
    private ApproveConfigManager approveConfigManager;

    @Autowired
    private ApproveNodeRecordView approveNodeRecordView;

    /**
     * 验证是否有启用的模板配置信息
     *
     * @param modelName 模板名称
     */
    public ApproveModel verifyApproveModelIsAvailable(String modelName) {
        Assert.hasLength(modelName, "模板名称不能为空！");
        ApproveModel model = approveConfigManager.findOfficialEditionModel(modelName);
        if (Objects.isNull(model)) {
            throw new ResourceNotFoundException("当前审批缺少启用的的审批模板信息， 请联系管理员检查审批模板配置信息！");
        }
        return model;
    }

    /**
     * 验证是否存在驳回节点
     */
    public List<ApproveNodeRecord> verifyExistRejectAbleNode(long instanceId, long currNodeId) {
        Assert.isTrue(instanceId > 0, "实例ID不能为空");
        List<ApproveNodeRecord> nodes = approveNodeRecordView.list(instanceId, ApproveResult.APPROVED, null);
        if (nodes.size() == 0) {
            throw new ResourceConflictException("当前节点之前没有已经被审批的节点，请查看当前节点等级！");
        }
        return nodes;
    }

    /**
     * 验证当前节点是否存在
     *
     * @param nodeId 当前节点ID
     */
    public ApproveNodeRecord verifyNodeIsExist(long nodeId) {
        Assert.isTrue(nodeId > 0, "当前节点ID不能为空");
        ApproveNodeRecord currNode = approveNodeRecordView.getById(nodeId);
        if (Objects.isNull(currNode)) {
            throw new ResourceConflictException("当前节点不存在或许已被删除，节点ID ：" + nodeId);
        }
        return currNode;
    }
}
