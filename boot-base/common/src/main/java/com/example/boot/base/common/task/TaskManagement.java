package com.example.boot.base.common.task;

import com.example.boot.base.task.AbstractTaskTemplate;

/**
 * 　定时任务管理器
 */
public interface TaskManagement {

    void run(String taskId);

    void start(String taskId);

    void stop(String taskId);

    void disable(String taskId);

    TaskStatus getStatus(String taskId);

    AbstractTaskTemplate getTask(String taskId);

    /**
     * 任务状态
     */
    enum TaskStatus {
        START, //启动状态状态
        DISABLED,//禁用
        STOPPED, // 停止
        RUNNING //运行中
    }

}
