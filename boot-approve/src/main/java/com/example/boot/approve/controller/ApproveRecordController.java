package com.example.boot.approve.controller;

import com.example.boot.approve.entity.config.ApproveModel;
import com.example.boot.approve.entity.runtime.ApproveNodeRecord;
import com.example.boot.approve.service.ApproveRecordManager;
import com.example.boot.approve.validator.ApproveRecordValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created  on 2022/3/14 17:17:12
 *
 * @author zl
 */
@RestController
@RequestMapping("/approve-records/")
public class ApproveRecordController {

    @Autowired
    private ApproveRecordManager approveRecordManager;

    @Autowired
    private ApproveRecordValidator approveRecordValidator;

    // TODO: 2022/3/14 测试使用，通常审批实例的添加分散在不同的模块下

    /**
     * 添加审批实例
     *
     * @param instanceId 业务相关的实例ID
     * @param modelName  审批模板名称
     */
    @PostMapping("instances/")
    public ResponseEntity<Void> createInstance(@RequestParam long instanceId, @RequestParam String modelName, @RequestParam(required = false) String reason) {

        ApproveModel model = approveRecordValidator.verifyApproveModelIsAvailable(modelName);
        approveRecordManager.createInstance(instanceId, model, reason);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * TODO 具体的逻辑实现
     * 当前实例，已被审批的节点， 用于驳回查看
     *
     * @param instanceId 实例ID
     * @return 已被审批的节点列表
     */
    @GetMapping("node-records/list-approve-nodes/")
    public ResponseEntity<List<ApproveNodeRecord>> listApprovedNode(@RequestParam long instanceId) {
        return ResponseEntity.ok(approveRecordManager.listApproveNode(instanceId));
    }

    /**
     * 待我审批
     */
    @GetMapping("actions/get-pending-approves/")
    public ResponseEntity<Void> getPendingApprove() {

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * 我发起的审批
     */
    @GetMapping("actions/get-initiate-approves/")
    public ResponseEntity<Void> getInitiateApprove() {
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * 我审批的【拒绝 驳回 通过】
     */
    @GetMapping("actions/get-self-approved/")
    public ResponseEntity<Void> getSelfApproved() {
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * 我转办的
     */
    @GetMapping("actions/get-transfers/")
    public ResponseEntity<Void> getTransfer() {
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * 查看审批详情
     * 通过参数ID 获取最近一次的审批详情
     */
    @GetMapping("actions/get-approve-details/")
    public ResponseEntity<Void> getApproveDetail(@RequestParam long instanceId) {
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * 根据参数ID 查询审批历史
     *
     * @param paramId 参数ID
     */
    @GetMapping("actions/get-approve-histories/")
    public ResponseEntity<Void> getApproveHistory(@RequestParam long paramId) {
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
