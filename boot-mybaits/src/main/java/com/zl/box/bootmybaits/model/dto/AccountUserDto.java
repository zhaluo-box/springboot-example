package com.zl.box.bootmybaits.model.dto;

import com.zl.box.bootmybaits.model.Account;
import lombok.Data;

import java.io.Serializable;

@Data
public class AccountUserDto extends Account implements Serializable {
    private static final long serialVersionUID = -4201831738034341383L;
    /**
     * 用户名称
     */
    private String username;
    /**
     * 地址
     */
    private String address;
}
