package com.example.boot.approve.common.utils;

import com.example.boot.approve.entity.User;

import java.util.ArrayList;
import java.util.List;

/**
 * todo 测试 后期删除
 * 方便测试使用随机获取一个用户作为登录用户
 * Created  on 2022/3/16 11:11:05
 *
 * @author zl
 */
public final class SecurityUtil {

    private static final ThreadLocal<User> USER_THREAD_LOCAL = new ThreadLocal<>();

    private static final List<User> USERS = new ArrayList<>(4);

    static {
        for (long i = 1; i < 5; i++) {
            USERS.add(new User(i, "name" + i));
        }
    }

    private SecurityUtil() {
       
    }

    public static User getCurrentLogin() {
        int index = (int) (Math.random() * USERS.size());
        return USERS.get(index);
    }

}
