package com.example.boot.approve.entity;

import com.example.boot.approve.enums.ApproveResult;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 审批实例
 * Created  on 2022/2/24 17:17:19
 *
 * @author zl
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApproveInstance extends BaseEntity {

    private long id;

    /**
     * 最终的审批结果
     */
    private ApproveResult approveResult;

    /**
     * 审批模板Id
     */
    private long approveModelId;

    /**
     * 审批模板版本号
     */
    private int approveModelVersion;

    /**
     * 参数【通常不建议传递复杂JSON由具体业务决定，目前建议传输ID 即可，自己做类型转换】
     */
    private String param;

    // TODO  目前实例不保存 URI与 detailId ,但是如果业务发生了变化，URI与DetailId 会导致历史审批界面打不开

    // 审批实例发起人

}
