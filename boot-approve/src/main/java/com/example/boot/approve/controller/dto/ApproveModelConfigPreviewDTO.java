package com.example.boot.approve.controller.dto;

import com.example.boot.approve.entity.common.BaseApproveNode;
import com.example.boot.approve.entity.config.ApproveAssigneeConfig;
import com.example.boot.approve.entity.config.ApproveModel;
import lombok.Data;
import lombok.experimental.Accessors;

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
public class ApproveModelConfigPreviewDTO {

    /**
     * 配置模板信息
     */
    private ApproveModel approveModel;

    /**
     * 采用List 保证数据有序， 方便前端渲染
     */
    private List<ApproveNodePreviewDTO> nodeInfo;

    @Data
    @Accessors(chain = true)
    public static class ApproveNodePreviewDTO {

        /**
         * 审批类型
         */
        private BaseApproveNode.ApproveType type;

        /**
         * 审批节点描述
         */
        private String description;

        /**
         * 审批节点名称
         */
        private String name;

        /**
         * 审批等级【数据库允许重复， 并不一定是挨着的】
         */
        private int leave;

        /**
         * 审批人【预览时，发起人审批节点做特殊处理】
         */
        List<ApproveAssigneeConfig> assigneeConfigs;

    }

}
