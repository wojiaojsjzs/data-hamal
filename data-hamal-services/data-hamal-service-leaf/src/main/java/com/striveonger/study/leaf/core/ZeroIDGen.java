package com.striveonger.study.leaf.core;

import com.striveonger.study.leaf.core.common.ID;
import com.striveonger.study.leaf.core.common.Status;


public class ZeroIDGen implements IDGen {

    @Override
    public ID get(String key) {
        return new ID(0, Status.SUCCESS);
    }

    @Override
    public boolean init() {
        return true;
    }
}
