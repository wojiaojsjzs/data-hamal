package com.striveonger.study.task.core.exec.flow;

import com.striveonger.study.task.core.exec.Executable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Mr.Lee
 * @description:
 * @date 2022-11-28 21:23
 */
public abstract class BasicExecutable implements Executable {
    private final Logger log = LoggerFactory.getLogger(BasicExecutable.class);

    protected List<Executable> executables;

    public void addExecutable(Executable executable) {
        if (executables == null) {
            executables = new ArrayList<>();
        }
        executables.add(executable);
    }

    public void addExecutable(Collection<Executable> executables) {
        if (this.executables == null) {
            this.executables = new ArrayList<>(executables);
        } else {
            this.executables.addAll(executables);
        }

    }





}