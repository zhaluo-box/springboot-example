package com.example.boot.approve.entity.config;

import com.example.boot.approve.entity.common.BaseApproveNode;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 审批配置与审批模板关联
 * Created  on 2022/2/24 17:17:09
 *
 * @author zl
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApproveNodeConfig extends BaseApproveNode {

    /**
     * 配置ID
     */
    private long id;

    /**
     * 审批模板Id
     */
    private long approveModelId;

    /**
     * 审批模板版本号
     */
    private int approveModelVersion;

}
