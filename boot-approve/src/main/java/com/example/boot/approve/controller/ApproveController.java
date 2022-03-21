package com.example.boot.approve.controller;

import com.example.boot.approve.service.ApproveRuntimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 审批流程控制器【不包含审批模板与配置相关的代码】
 * Created  on 2022/3/8 15:15:54
 * TODO  参数， 校验，逻辑需要优化
 *
 * @author zl
 */
@RequestMapping("/approves/")
@RestController
public class ApproveController {

    @Autowired
    private ApproveRuntimeService approveRuntimeService;

    /**
     * 审批
     */
    @PostMapping("actions/approve/")
    public ResponseEntity<Void> approve(@RequestParam long instanceId, @RequestParam String remark) {
        approveRuntimeService.approve(instanceId, remark);
        return ResponseEntity.ok().build();
    }

    /**
     * 拒绝
     *
     * @param instanceId 实例Id
     * @param remark     备注消息
     */
    @PostMapping("actions/refuse/")
    public ResponseEntity<Void> refuse(@RequestParam long instanceId, @RequestParam String remark) {
        approveRuntimeService.refuse(instanceId, remark);
        return ResponseEntity.ok().build();
    }

    /**
     * 撤销（检查所有节点都未被审批的时候才允许撤销）
     *
     * @param instanceId 审批实例ID
     * @param remark     备注消息
     */
    @PostMapping("actions/cancel/")
    public ResponseEntity<Void> cancel(@RequestParam long instanceId, @RequestParam String remark) {
        approveRuntimeService.cancel(instanceId, remark);
        return ResponseEntity.ok().build();
    }

    /**
     * 驳回
     * 从当前节点到驳回节点，新增历史记录
     *
     * @param instanceId 实例Id
     * @param remark     备注消息
     * @param nodeId     驳回节点
     */
    @PostMapping("actions/reject/")
    public ResponseEntity<Void> reject(@RequestParam long instanceId, @RequestParam String remark, @RequestParam long nodeId) {
        approveRuntimeService.reject(instanceId, remark, nodeId);
        return ResponseEntity.ok().build();
    }

    /**
     * 转办
     *
     * @param instanceId 实例Id
     * @param remark     备注消息
     * @param transfer   转办人Id
     */
    @PostMapping("actions/transform/")
    public ResponseEntity<Void> transform(long instanceId, String remark, long transfer) {
        approveRuntimeService.transform(instanceId, remark, transfer);
        return ResponseEntity.ok().build();
    }

    /**
     * TODO 工作移交 由超级管理员 才能进行管理
     */

}

