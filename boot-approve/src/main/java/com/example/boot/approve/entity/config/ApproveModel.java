package com.example.boot.approve.entity.config;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 审批模板
 * Created  on 2022/2/24 17:17:06
 *
 * @author zl
 */

@Data
@Accessors(chain = true)
@TableName(value = "approve_model", autoResultMap = true)
public class ApproveModel {

    /**
     * ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 模板名称【模板名称最初由代码自己生成出来维护到数据库】
     * 不允许修改， 只能服务自动发现
     * 模板名称规则： 模块名称 - 审批名称 例如 设备管理-设备申领审批
     */
    private String name;

    /**
     * 描述
     */
    private String description;

    /**
     * 版本号【版本号不断递增，但是数据永远只有一条】
     * 自动新增版本号
     */
    private int version;

    /**
     * 默认禁用状态
     * 与status 区分 ： 当前字段用于控制处于正式审批状态的审批停用， status控制审批模板的类型状态历史 草稿 正式
     * 启用禁用 true : 禁用，false : 启用
     */
    private boolean disabled;

    /**
     * 审批模板基础路径 【结合实例中的参数Id，以及下面的详情Id（detailId）
     * 保证页面能够正常跳转，并正确获取页面详情】
     */
    private String uri;

    /**
     * 详情组件ID【基于前端vue组件】
     */
    private String detailId;

    /**
     * 服务ID 【保留字段,微服务的时候用来与模板名称确定唯一，也是微服务的一个标识】
     * 取值： spring.application.name
     * 前端显示 可以维护系统字典 进行映射显示中文，到时候可以在某种程度上替代分类，作为分类使用
     */
    private String serviceName;

    /**
     * 默认为草稿版
     */
    private ApproveModelStatus status = ApproveModelStatus.DRAFT;

    /**
     * 审批模板状态
     */
    public enum ApproveModelStatus {
        HISTORY,// 历史版
        DRAFT,// 草稿
        OFFICIAL_EDITION // 官方版，这里指代正式版【启用的版本】
    }

}
