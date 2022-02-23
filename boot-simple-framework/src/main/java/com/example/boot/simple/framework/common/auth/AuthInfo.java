package com.example.boot.simple.framework.common.auth;

import com.example.boot.simple.framework.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Set;

/**
 * Created  on 2022/2/21 21:21:55
 *
 * @author zl
 */
@Getter
@AllArgsConstructor
public final class AuthInfo {

    /**
     * 登录用户
     */
    private final String username;

    /**
     * 拥有的角色
     */
    private final Set<Role> roles;

}
