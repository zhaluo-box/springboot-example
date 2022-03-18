package com.example.boot.approve.common.utils;

import com.example.boot.approve.entity.User;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO 登录人员信息替换 测试 后期删除
 * 方便测试使用随机获取一个用户作为登录用户
 * Created  on 2022/3/16 11:11:05
 *
 * @author zl
 */
public final class SecurityUtil {

    private static final List<User> USERS = new ArrayList<>(1);

    private SecurityUtil() {

    }

    public static User getCurrentLogin() {
        return USERS.get(0);
    }

    public static void reset(long id, String name) {
        USERS.clear();
        USERS.add(new User(id, name));
    }

}
