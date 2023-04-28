package com.striveonger.study.task.core.executor;

/**
 * @author Mr.Lee
 * @description: 可执行的
 * @date 2022-11-28 20:33
 */
public interface Executable extends Runnable {
    void execute() throws Exception;
}