package com.example.boot.simple.framework.common.auth;

import com.example.boot.simple.framework.entity.Role;
import lombok.Data;

import java.util.Set;

/**
 * Created  on 2022/2/21 21:21:55
 *
 * @author zl
 */
@Data
public class AuthInfo {

    /**
     * 登录用户
     */
    private String username;

    /**
     * 拥有的角色
     */
    private Set<Role> roles;

}
