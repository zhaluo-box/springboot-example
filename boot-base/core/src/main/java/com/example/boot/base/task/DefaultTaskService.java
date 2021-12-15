package com.example.boot.base.task;

import com.example.boot.base.common.task.AbstractTaskTemplate;
import org.springframework.stereotype.Service;

/**
 * Created by MingZhe on 2021/11/17 17:17:21
 *
 * @author wmz
 */
@Service
public class DefaultTaskService extends AbstractTaskTemplate {

    @Override
    protected void before() {

    }

    @Override
    protected void after() {

    }

    @Override
    protected void cancel() {

    }

    @Override
    protected void execute() {
        System.out.println("执行任务");
    }
}
