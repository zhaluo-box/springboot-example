package com.example.boot.simple.framework.common.utils;

import com.example.boot.simple.framework.common.auth.AuthInfo;
import com.example.boot.simple.framework.entity.Role;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * TODO 功能待完善！
 * Created  on 2022/2/23 17:17:15
 *
 * @author zl
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class JwtUtils {

    /**
     * 生成Token
     * token 内部存放账户名称与角色列表
     *
     * @return token
     */
    public static String generateToken(String accountName, Set<Role> roles) {

        return "";
    }

    /**
     * 校验Token
     */
    public static boolean verifyToken(String token) {

        return true;
    }

    /**
     * 解析token
     *
     * @return 认证信息
     */
    public static AuthInfo parseToken() {

        return null;
    }
}
