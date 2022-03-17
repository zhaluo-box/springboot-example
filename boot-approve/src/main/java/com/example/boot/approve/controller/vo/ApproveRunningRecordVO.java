package com.example.boot.approve.controller.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 审批记录筛选
 * Created  on 2022/3/17 11:11:22
 *
 * @author zl
 */
@Data
@Accessors(chain = true)
public class ApproveRunningRecordVO {

    /**
     * 审批名称==>审批模板的名称
     */
    private String name;

    /**
     * 发起人
     */
    private long assignee;

    /**
     * 起始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    /**
     * 结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;
}
