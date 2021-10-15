package com.zl.box.bootmybaits.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Table
@Entity
public class Account implements Serializable {
    private static final long serialVersionUID = -4000575235873058976L;

    /**
     * 编号
     */
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 用户编号
     */
    @Column(name = "UID")
    private Integer uid;

    /**
     * 金额
     */
    @Column(name = "MONEY")
    private Double money;

    //   一对一测试;
    //    private User user;
    //
    //    public User getUser() {
    //        return user;
    //    }
    //    public void setUser(User user) {
    //        this.user = user;
    //    }

}
