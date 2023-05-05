package com.striveonger.study.task.core.constant;

/**
 * @author Mr.Lee
 * @description:
 * @date 2023-05-05 16:46
 */
public class TaskKeyFactory {
    private static final String TASK_MASTER_THREAD_NAME = "task-exec-%d-master";
    public static String masterThreadName(long taskID) {
        return String.format(TASK_MASTER_THREAD_NAME, taskID);
    }


}
