package com.example.boot.approve.controller.dto;

import com.example.boot.approve.entity.config.ApproveNodeConfig;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

/**
 * Created  on 2022/3/14 16:16:09
 *
 * @author zl
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class ApproveNodeConfigDTO extends ApproveNodeConfig {

    /**
     * 审批节点关联的审批人信息
     */
    List<ApproveAssigneeConfigDTO> approveAssigneeConfigs = new ArrayList<>();
}