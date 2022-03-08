package com.example.boot.approve.entity;

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
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApproveModel extends BaseEntity {

    /**
     * ID
     */
    private long id;

    /**
     * 模板名称【模板名称最初由代码自己生成出来维护到数据库】
     */
    private String name;

    /**
     * 描述
     */
    private String description;

    /**
     * 版本号【版本号不断递增，但是数据永远只有一条】
     */
    private int version;

    /**
     * 默认禁用
     * 启用禁用 true : 禁用，false : 启用
     */
    private boolean disabled = true;

    /**
     * 审批模板基础路径 【结合实例中的参数Id，以及下面的详情Id（detailId）
     * 保证页面能够正常跳转，并正确获取页面详情】
     */
    private String uri;

    /**
     * 详情组件ID【基于前端vue组件】
     */
    private String detailId;

    //    /**
    //     * 服务ID 【保留字段,微服务的时候用来与模板名称确定唯一，也是微服务的一个标识】
    //     */
    //    private String serviceId;
}
