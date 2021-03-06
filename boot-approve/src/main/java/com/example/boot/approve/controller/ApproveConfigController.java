package com.example.boot.approve.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.boot.approve.common.mvc.BasePageQuery;
import com.example.boot.approve.controller.dto.ApproveModelConfigPreviewDTO;
import com.example.boot.approve.entity.config.ApproveAssigneeConfig;
import com.example.boot.approve.entity.config.ApproveModel;
import com.example.boot.approve.entity.config.ApproveNodeConfig;
import com.example.boot.approve.service.ApproveConfigManager;
import com.example.boot.approve.validator.ApproveConfigValidator;
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

    @Autowired
    private ApproveConfigValidator approveConfigValidator;

    //    ==========================审批模板配置================================

    /**
     * TODO: 2022/3/14  冗余接口后期删除
     * 查询所有审批模板不分页
     */
    @GetMapping("models/")
    public ResponseEntity<List<ApproveModel>> listModel(@RequestParam(required = false) String modelName, @RequestParam(required = false) String serviceName) {
        return ResponseEntity.ok(approveConfigManager.listModel(modelName, serviceName));
    }

    /**
     * 分页返回
     *
     * @param modelName   模板名称
     * @param serviceName 服务名称 application.name
     */
    @GetMapping("models/actions/pages/")
    public ResponseEntity<IPage<ApproveModel>> pageModel(@RequestParam(required = false) String modelName, @RequestParam(required = false) String serviceName,
                                                         BasePageQuery<String, ApproveModel> pageQuery) {
        return ResponseEntity.ok(approveConfigManager.pageModel(modelName, serviceName, pageQuery));
    }

    /**
     * TODO: 2022/3/10  只是用于测试
     */
    @PostMapping("models/")
    public ResponseEntity<Void> saveModel(@RequestBody ApproveModel model) {
        approveConfigManager.saveModel(model);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 修改审批模板 【审批模板不允许新增，只能修改】
     * 目前哪些可以编辑哪些不可以编辑由前端控制，目前服务ID和名称是不允许修改的
     *
     * @param model 审批模板
     * @return httpHeader 204 no-content
     */
    @PutMapping("models/")
    public ResponseEntity<Void> updateModel(@RequestBody ApproveModel model) {
        approveConfigValidator.verifyApproveModelIsValid(model);
        approveConfigManager.updateModel(model);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * 复制   复制审批模板的所有相关数据【节点配置，指定人配置】
     */
    @PostMapping("models/{modelId}/actions/copy-models/")
    public ResponseEntity<Void> copyModel(@PathVariable long modelId) {
        approveConfigManager.copyModel(modelId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 发布
     */
    @PostMapping("models/{modelId}/actions/publish-models/")
    public ResponseEntity<Void> publishModel(@PathVariable long modelId) {
        approveConfigValidator.verifyModelAssigneeIsValid(modelId);
        approveConfigManager.publishModel(modelId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    // 审批模板不支持删除

    //    ==========================审批节点配置================================

    /**
     * 展示所有节点
     */
    @GetMapping("nodes/{modelId}/")
    public ResponseEntity<List<ApproveNodeConfig>> listNodeConfig(@PathVariable long modelId) {
        return ResponseEntity.ok(approveConfigManager.listNodeConfig(modelId));
    }

    /**
     * 添加节点
     */
    @PostMapping("nodes/")
    public ResponseEntity<Void> saveNode(@RequestBody ApproveNodeConfig nodeConfig) throws Exception {
        approveConfigValidator.verifyNodeConfigNameIsValid(nodeConfig);
        approveConfigManager.saveNode(nodeConfig);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 更新节点
     */
    @PutMapping("nodes/")
    public ResponseEntity<Void> updateNode(@RequestBody ApproveNodeConfig nodeConfig) {
        approveConfigValidator.verifyNodeConfigIsValidForUpdate(nodeConfig);
        approveConfigManager.updateNode(nodeConfig);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * 删除节点
     * 节点删除不做任何限制，因为有前置条件，审批模板相关的所有东西，都需要在草稿状态下才能修改【需要前端做一定的限制】
     */
    @DeleteMapping("nodes/{nodeId}/")
    public ResponseEntity<Void> deleteNode(@PathVariable long nodeId) {
        approveConfigManager.deleteNode(nodeId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    //    ==========================审批人配置================================

    /**
     * 展示当前节点的所有审批人
     */
    @GetMapping("assignees/{nodeId}/")
    public ResponseEntity<List<ApproveAssigneeConfig>> listAssignee(@PathVariable long nodeId) {
        return ResponseEntity.ok(approveConfigManager.listAssignee(nodeId));
    }

    /**
     * 添加审批人配置
     */
    @PostMapping("assignees/")
    public ResponseEntity<Void> saveAssignee(@RequestBody ApproveAssigneeConfig assigneeConfig) {
        approveConfigValidator.verifyAssigneeIsValid(assigneeConfig);
        approveConfigManager.saveAssignee(assigneeConfig);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 删除审批人配置
     */
    @DeleteMapping("assignees/{assigneeId}/")
    public ResponseEntity<Void> deleteAssignee(@PathVariable long assigneeId) {
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
    public ResponseEntity<ApproveModelConfigPreviewDTO> preview(@PathVariable long modelId) {
        return ResponseEntity.ok(approveConfigManager.preview(modelId));
    }

}
