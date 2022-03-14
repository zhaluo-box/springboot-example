package com.example.boot.approve.controller.dto;

import com.example.boot.approve.entity.config.ApproveAssigneeConfig;
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
public class ApproveAssigneeConfigDTO extends ApproveAssigneeConfig {

    /**
     * 审批人姓名
     * TODO: 2022/3/14   后期修改为审批人完整信息，包括性别 工号等
     */
    private String assigneeName;

    /**
     * 转办候选人的姓名列表，
     * TODO: 2022/3/14  后期修改为转办人完整信息，包括性别 工号等
     */
    private List<String> transferCandidateNames = new ArrayList<>();

}