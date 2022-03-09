package com.example.boot.approve.controller;

import com.example.boot.approve.entity.config.ApproveAssigneeConfig;
import com.example.boot.approve.entity.config.ApproveModel;
import com.example.boot.approve.entity.config.ApproveNodeConfig;
import com.example.boot.approve.service.ApproveConfigManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 审批配置控制器【包括审批模板与配置项】
 * Created  on 2022/3/8 15:15:55
 * TODO @me 具体业务实现
 *
 * @author zl
 */
@RestController
@RequestMapping("/approve-configs/")
public class ApproveConfigController {

    @Autowired
    private ApproveConfigManager approveConfigManager;

    //    ==========================审批模板配置================================

    /**
     * 查询所有审批模板不分页
     * TODO 查询条件待定
     */
    @GetMapping("models/")
    public ResponseEntity<List<ApproveModel>> listModel() {
        return ResponseEntity.ok(null);
    }

    /**
     * 修改审批模板 【审批模板不允许新增，只能修改】
     *
     * @param model 审批模板
     * @return httpHeader 204 no-content
     */
    @PutMapping("models/")
    public ResponseEntity<Void> updateModel(@RequestBody ApproveModel model) {
        approveConfigManager.updateModel(model);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * 复制   复制审批模板的所有相关数据【节点配置，指定人配置】
     */
    @PostMapping("models/{modelId}/actions/copy-models/")
    public ResponseEntity<Void> copyModel(@PathVariable String modelId) {
        approveConfigManager.copyModel(modelId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 发布
     */
    @PostMapping("models/{modelId}/actions/publish-models/")
    public ResponseEntity<Void> publishModel(@PathVariable String modelId) {
        approveConfigManager.publishModel(modelId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    //    ==========================审批节点配置================================

    /**
     * 展示所有节点
     */
    @GetMapping("nodes/{modelId}/")
    public ResponseEntity<List<ApproveNodeConfig>> listNodeConfig(@PathVariable String modelId) {
        return ResponseEntity.ok(approveConfigManager.listNodeConfig(modelId));
    }

    /**
     * 添加节点
     */
    @PostMapping("nodes/")
    public ResponseEntity<Void> saveNode(@RequestBody ApproveNodeConfig nodeConfig) {
        approveConfigManager.saveNode(nodeConfig);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 更新节点
     */
    @PutMapping("nodes/")
    public ResponseEntity<Void> updateNode(@RequestBody ApproveNodeConfig nodeConfig) {
        approveConfigManager.updateNode(nodeConfig);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * 删除节点
     */
    @DeleteMapping("nodes/{nodeId}/")
    public ResponseEntity<Void> deleteNode(@PathVariable String nodeId) {
        approveConfigManager.deleteNode(nodeId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    //    ==========================审批人配置================================

    /**
     * 展示当前节点的所有审批人
     */
    @GetMapping("assignees/{nodeId}/")
    public ResponseEntity<List<ApproveAssigneeConfig>> listAssignee(@PathVariable String nodeId) {
        return ResponseEntity.ok(approveConfigManager.listAssignee(nodeId));
    }

    /**
     * 添加审批人配置
     */
    @PostMapping("assignees/")
    public ResponseEntity<Void> saveAssignee(@RequestBody ApproveAssigneeConfig assigneeConfig) {
        approveConfigManager.saveAssignee(assigneeConfig);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 删除审批人配置
     */
    @DeleteMapping("assignees/{assigneeId}/")
    public ResponseEntity<Void> deleteAssignee(@PathVariable String assigneeId) {
        approveConfigManager.deleteAssignee(assigneeId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * 修改审批人配置
     */
    @PutMapping("assignees/")
    public ResponseEntity<Void> updateAssignee(@RequestBody ApproveAssigneeConfig assigneeConfig) {
        approveConfigManager.updateAssignee(assigneeConfig);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    //    ==========================审批通用================================

    /**
     * 审批配置预览
     */
    @GetMapping("models/{modelId}/actions/preview/")
    public ResponseEntity<?> preview(@PathVariable String modelId) {
        // TODO 返回值类型待定
        approveConfigManager.preview(modelId);
        return ResponseEntity.ok(null);
    }

}
