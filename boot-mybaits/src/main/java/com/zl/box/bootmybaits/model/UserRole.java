package com.zl.box.bootmybaits.model;

import javax.persistence.*;
import java.io.Serializable;

@Table(name = "user_role")
public class UserRole implements Serializable {
    private static final long serialVersionUID = 1161752681870211175L;
    
    /**
     * 用户编号
     */
    @Id
    @Column(name = "UID")
    private Integer uid;

    /**
     * 角色编号
     */
    @Id
    @Column(name = "RID")
    private Integer rid;

    /**
     * 获取用户编号
     *
     * @return UID - 用户编号
     */
    public Integer getUid() {
        return uid;
    }

    /**
     * 设置用户编号
     *
     * @param uid 用户编号
     */
    public void setUid(Integer uid) {
        this.uid = uid;
    }

    /**
     * 获取角色编号
     *
     * @return RID - 角色编号
     */
    public Integer getRid() {
        return rid;
    }

    /**
     * 设置角色编号
     *
     * @param rid 角色编号
     */
    public void setRid(Integer rid) {
        this.rid = rid;
    }
}
