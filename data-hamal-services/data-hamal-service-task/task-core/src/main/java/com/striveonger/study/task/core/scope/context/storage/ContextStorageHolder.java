package com.striveonger.study.task.core.scope.context.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mr.Lee
 * @description:
 * @date 2023-07-20 18:17
 */
public class ContextStorageHolder {
    private final Logger log = LoggerFactory.getLogger(ContextStorageHolder.class);

    /*
    public static class Holder {
        private static final Holder instance = new Holder();

        private final StatusControls controls;

        private Holder() {
            ServiceLoader<StatusControls> loader = ServiceLoader.load(StatusControls.class);
            // 尝试加载外部实现, 如果没有加载到外部实现类, 则使用默认的
            this.controls  = loader.findFirst().orElseGet(Default::new);
        }

    public static StatusControls getControls() {
        return instance.controls;
    }
}
     */


}