package com.striveonger.study.leaf.core;


import com.striveonger.study.leaf.core.common.Result;

public interface IDGen {
    Result get(String key);

    boolean init();
}
