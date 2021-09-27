package com.zl.box.bootmybaits.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "user_role")
@Data
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

}
