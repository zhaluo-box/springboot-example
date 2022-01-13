package com.example.boot.base.common.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Set;

/**
 *
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
