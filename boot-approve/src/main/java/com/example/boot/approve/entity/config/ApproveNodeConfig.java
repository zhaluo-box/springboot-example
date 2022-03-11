package com.example.boot.approve.entity.config;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
@TableName(value = "approve_node_config")
public class ApproveNodeConfig extends BaseApproveNode {

    /**
     * 配置ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 审批模板Id
     */
    private long approveModelId;

}
