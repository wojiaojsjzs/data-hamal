package com.striveonger.study.leaf.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.striveonger.study.leaf.entity.LeafAlloc;

import java.util.List;

/**
 * <p>
 * 号段表 服务类
 * </p>
 *
 * @author Mr.Lee
 * @since 2022-11-23
 */
public interface ILeafAllocService extends IService<LeafAlloc> {

    List<LeafAlloc> getAllLeafAllocs();

    LeafAlloc updateMaxIdAndGetLeafAlloc(String tag);

    LeafAlloc updateMaxIdByCustomStepAndGetLeafAlloc(LeafAlloc leafAlloc);

    List<String> getAllTags();

    int count(String tag);

    @Override
    boolean save(LeafAlloc leafAlloc);
}
