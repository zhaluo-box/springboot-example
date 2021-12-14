package com.example.boot.base.task;

import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

/**
 *
 */
@Service
public class DefaultTaskManagement implements TaskManagement {
    private static final Map<String, ScheduledFuture<?>> TASK_THREAD_POOL_CACHE = new ConcurrentHashMap<>();

    @Override
    public void run(String taskId) {

    }

    @Override
    public void start(String taskId) {

        var taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.initialize();
        ScheduledFuture<?> schedule = taskScheduler.schedule(getTask(taskId), new CronTrigger("0 0 3 1 * ?"));
        TASK_THREAD_POOL_CACHE.put(taskId, schedule);
        //  List<TransactionStatus> transactionStatuses = Collections.synchronizedList(new ArrayList<TransactionStatus>());

    }

    @Override
    public void stop(String taskId) {
        TASK_THREAD_POOL_CACHE.get(taskId).cancel(true);
    }

    @Override
    public void disable(String taskId) {

    }

    @Override
    public TaskStatus getStatus(String taskId) {
        return null;
    }

    @Override
    public AbstractTaskTemplate getTask(String taskId) {
        return new DefaultTaskService();
    }
}
