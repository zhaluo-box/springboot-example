package com.example.boot.approve.controller.dto;

import com.example.boot.approve.entity.config.ApproveModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

/**
 * 审批预览DTO
 * Created  on 2022/3/9 17:17:08
 * TODO 待优化
 *
 * @author zl
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class ApproveModelConfigPreviewDTO extends ApproveModel {

    /**
     * 关联的审批节点信息
     */
    private List<ApproveNodeConfigDTO> approveNodeConfigs = new ArrayList<>();

}
