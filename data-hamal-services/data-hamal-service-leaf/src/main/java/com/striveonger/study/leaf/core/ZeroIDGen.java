package com.striveonger.study.leaf.core;

import com.striveonger.study.leaf.core.common.Result;
import com.striveonger.study.leaf.core.common.Status;


public class ZeroIDGen implements IDGen {

    @Override
    public Result get(String key) {
        return new Result(0, Status.SUCCESS);
    }

    @Override
    public boolean init() {
        return true;
    }
}
