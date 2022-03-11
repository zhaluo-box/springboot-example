package com.example.boot.approve.common.annotation.approve;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 审批服务标记
 * Created  on 2022/3/11 13:13:22
 * TODO: 2022/3/11  审批注解待完善
 *
 * @author zl
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ApproveService {
    // TODO 记录支持哪些功能
    /**
     * 审批模板名称【前端不支持修改】
     */

    /**
     * 审批URI 【前端支持修改】
     */

    /**
     * 详情detailId 【前端支持修改】
     */

    /**
     * 审批标识 【使用英文字符】
     */

    /**
     * 审批描述 【前端支持修改】
     */

    /**
     * 当前审批支持了哪些功能 【取决于后台实现了哪些】
     */
}
