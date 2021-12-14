package com.example.boot.base.service;

import com.example.boot.base.common.view.UserView;
import com.example.boot.base.entity.jpa.User;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

/**
 * Created by MingZhe on 2021/11/21 09:9:32
 *
 * @author wmz
 */
@Slf4j
@AllArgsConstructor
class AsyncExecutor implements Runnable {

    protected UserView userView;

    @SneakyThrows
    @Override
    public void run() {
        log.info("耗时任务模拟-事务开始!");
        var user = new User().setId(UUID.randomUUID().toString()).setAddress("北京").setAge(18).setSex(User.SexType.MAN);
        userView.save(user);

        log.info("线程睡眠20S! ");
        Thread.sleep(20 * 1000);

        user.setUsername("张三");
        userView.save(user);
        log.info("耗时任务模拟-事务结束!");
    }

    private User initUser() {
        return new User().setId(UUID.randomUUID().toString()).setAddress("北京").setAge(18).setSex(User.SexType.MAN);
    }
}
