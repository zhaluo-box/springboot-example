package com.example.boot.approve.controller.vo;

import lombok.Data;

/**
 * Created  on 2022/3/9 16:16:06
 *
 * @author zl
 */
@Data
public class ApproveVO {

    /**
     * 审批实例Id
     */
    private long instanceId;

    /**
     * 审批意见
     */
    private String remark;

}
