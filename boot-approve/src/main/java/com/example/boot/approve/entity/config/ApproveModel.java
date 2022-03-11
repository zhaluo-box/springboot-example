package com.example.boot.approve.entity.config;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 审批模板
 * Created  on 2022/2/24 17:17:06
 *
 * @author zl
 */

@Data
@Accessors(chain = true)
@EqualsAndHashCode(of = "id")
@JsonIgnoreProperties(ignoreUnknown = true)
@TableName(value = "approve_model")
public class ApproveModel {

    /**
     * ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 模板名称【模板名称最初由代码自己生成出来维护到数据库】
     * 不允许修改， 只能服务自动发现
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

    // TODO: 2022/3/10  对审批模板进行管理 分为草稿【disabled = true】， 正式版【disabled =false】 ： 历史版本【disabled = true】 发布功能会影响这些状态， 必须有一版本为正式版，否则程序无法正常运行
    // 复制的时候所有配置为草稿状态 还需要添加审批移交 方便审批人员职位变动进行工作交接
    // TODO: 2022/3/10  记录支持的功能按钮，审批 拒绝 驳回 转办 撤销 作废 等 
}
