package com.example.boot.simple.framework.config.intercetpor;

import com.example.boot.simple.framework.common.auth.AuthInfo;
import com.example.boot.simple.framework.common.auth.AuthInfoHolder;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.HashSet;

/**
 * 认证拦截器，主要解析token 获取其中的用户名与角色相关信息
 * Created  on 2022/2/23 17:17:09
 *
 * @author zl
 */
public class AuthInterceptor implements HandlerInterceptor {

    private static final String[] IGNORED_PATH = new String[] {  };

    private static final String OPTIONS = "OPTIONS";

    protected static final String AUTHORIZATION = "Authorization";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        var path = request.getRequestURI().toLowerCase();

        if (isPublicAddress(path)) {
            return true;
        }

        if (OPTIONS.equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        var tokenHeaderValue = getTokenHeaderValue(request);

        // TODO 验证 token

        // TODO 验证用户是否被禁用或者归档

        // TODO 从token中解析用户名，角色存放到authInfoHolder

        AuthInfo authInfo = new AuthInfo();
        authInfo.setUsername("XXX");
        authInfo.setRoles(new HashSet<>());

        AuthInfoHolder.set(authInfo);

        return true;
    }

    /**
     * 过滤不需要权限验证的Path
     *
     * @param path 请求地址
     * @return 如果是登录或者登出，则返回true
     */
    private boolean isPublicAddress(String path) {
        return Arrays.asList(IGNORED_PATH).contains(path);
    }

    /**
     * 获取token认证信息
     */
    private String getTokenHeaderValue(Object request) {
        return ((HttpServletRequest) request).getHeader(AUTHORIZATION);
    }
}
