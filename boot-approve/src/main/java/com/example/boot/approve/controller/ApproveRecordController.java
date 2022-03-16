package com.example.boot.approve.controller;

import com.example.boot.approve.entity.config.ApproveModel;
import com.example.boot.approve.service.ApproveRecordManager;
import com.example.boot.approve.validator.ApproveRecordValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
