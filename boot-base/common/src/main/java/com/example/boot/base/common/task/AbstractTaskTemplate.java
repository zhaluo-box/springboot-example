package com.example.boot.base.common.task;

/**
 * 模板类
 */
public abstract class AbstractTaskTemplate implements Runnable {

    protected String taskId;

    @Override
    public void run() {
        this.before();
        this.execute();
        this.after();
    }

    /**
     * 任务执行前需要执行的方法
     */
    protected abstract void before();

    /**
     * 任务执行后需要执行的方法
     */
    protected abstract void after();

    /**
     * 任务取消需要执行的方法
     */
    protected abstract void cancel();

    /**
     * 任务真正执行的方法体
     */
    protected abstract void execute();

}
