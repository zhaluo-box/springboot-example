package com.example.boot.simple.framework.common.auth;

import org.springframework.util.Assert;

/**
 * Created  on 2022/2/21 22:22:08
 *
 * @author zl
 */
public class AuthInfoHolder {

    private static final ThreadLocal<AuthInfo> AUTH_INFO_HOLDER = new ThreadLocal<>();

    public static void clear() {
        AUTH_INFO_HOLDER.remove();
    }

    public static AuthInfo get() {
        return AUTH_INFO_HOLDER.get();
    }

    public static void set(AuthInfo authInfo) {
        Assert.notNull(authInfo, "认证信息为空。");
        AUTH_INFO_HOLDER.set(authInfo);
    }
}
