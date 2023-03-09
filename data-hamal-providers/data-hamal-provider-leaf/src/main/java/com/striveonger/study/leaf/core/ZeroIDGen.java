package com.striveonger.study.leaf.core;


import com.striveonger.study.api.leaf.core.ID;
import com.striveonger.study.api.leaf.core.Status;

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
