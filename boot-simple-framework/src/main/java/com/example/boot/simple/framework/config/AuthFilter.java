package com.example.boot.simple.framework.config;

import com.example.boot.simple.framework.common.auth.AuthInfo;
import com.example.boot.simple.framework.common.auth.AuthInfoHolder;
import com.example.boot.simple.framework.entity.Role;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.HashSet;

/**
 * Created  on 2022/2/21 22:22:03
 *
 * @author zl
 */
@Slf4j
public class AuthFilter implements HandlerInterceptor {

    private static final String[] IGNORED_PATH = new String[] { "/systems/actions/logout/", "/systems/actions/login/" };

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
