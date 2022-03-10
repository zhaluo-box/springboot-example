package com.example.boot.approve.entity.common;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.type.EnumTypeHandler;

/**
 * Created  on 2022/3/9 10:10:04
 *
 * @author zl
 */
@Setter
@Getter
public class BaseApproveNode {

    /**
     * 审批类型
     */
    public enum ApproveType {
        DIRECT, // 直签，配置项对应只有一项
        COUNTERSIGN, // 会签 ，配置项可以多项
        XOF; // 异或审批 ， 配置项可以多项，通常为2项
    }

    /**
     * 审批类型
     */
    @TableField(typeHandler = EnumTypeHandler.class)
    private ApproveType type;

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
    private int level;
}
