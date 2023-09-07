package com.example.boot.base.common.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Set;

/**
 * 是一些元信息，demo 中没什么用处，在 nest 中用于识别 分发到哪里，顺带 带上生产者信息与租户信息，以及目的服务
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
@Accessors(chain = true)
public class MetaData implements Serializable {

    public enum ReceiverRole {
        CONTROL_CENTER, CENTRAL_RUNTIME, REMOTE_RUNTIME
    }

    /**
     * 时间发起者的ID(当前节点的ID)
     */
    private String producer;

    private Set<ReceiverRole> targetRoles;

    /**
     * 租户ID
     */
    private String tenantId;

}
