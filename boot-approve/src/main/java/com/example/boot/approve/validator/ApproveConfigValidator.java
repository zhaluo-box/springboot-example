package com.example.boot.approve.validator;

import com.example.boot.approve.entity.config.ApproveAssigneeConfig;
import org.springframework.stereotype.Component;

/**
 * 审批配置校验服务
 * Created  on 2022/3/10 16:16:30
 *
 * @author zl
 */
@Component
public class ApproveConfigValidator {

    //    ==========================审批模板配置==============================
    //    ==========================审批节点配置==============================
    //    ==========================审批人配置================================
    public void verifyAssigneeIsRequired(ApproveAssigneeConfig assigneeConfig) {
        // TODO: 2022/3/10 验证审批节点类型， 当前审批类型是否支持添加审批人
        // 规则:  异或只能添加两个  会签可以添加多个  直签只能添加一个
    }
    //    ==========================审批配置通用功能===========================
}
