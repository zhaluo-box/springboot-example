package com.zl.box.bootmybaits.model;

import javax.persistence.*;
import java.io.Serializable;

@Table
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

    /**
     * 获取编号
     *
     * @return ID - 编号
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置编号
     *
     * @param id 编号
     */
    public void setId(Integer id) {
        this.id = id;
    }

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
     * 获取金额
     *
     * @return MONEY - 金额
     */
    public Double getMoney() {
        return money;
    }

    /**
     * 设置金额
     *
     * @param money 金额
     */
    public void setMoney(Double money) {
        this.money = money;
    }
}
