package com.striveonger.study.task.core.listener;

import com.striveonger.study.task.core.scope.context.RuntimeContext;
import com.sun.source.tree.IfTree;

/**
 * @author Mr.Lee
 * @description:
 * @date 2023-04-23 16:11
 */
public abstract class Listener {

    /*
     * TODO: 现在呢想到的
     *  执行前:
     *    1. 初始化执行环境
     *    2. 检查Task执行状态
     *  执行后:
     *    1. 更新Step状态
     *    2. 回收资源
     *  执行出错:
     *    1. 更新Step状态
     *    2. 终止Task运行
     */

    /**
     * 执行前
     */
    public final void before(RuntimeContext context) {
        this.execBefore(context);
    }

    protected abstract void execBefore(RuntimeContext context);

    /**
     * 执行后
     */
    public final void after(RuntimeContext context) {
        this.execAfter(context);
    }

    protected abstract void execAfter(RuntimeContext context);

    /**
     * 执行出错
     */
    public final void error(RuntimeContext context) {
        this.execError(context);
    }

    protected abstract void execError(RuntimeContext context);

    public enum Type { ALL, TASK, STEP; }
}
