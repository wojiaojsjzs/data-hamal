package com.striveonger.study.leaf.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.striveonger.study.leaf.entity.LeafAlloc;
import com.striveonger.study.leaf.mapper.LeafAllocMapper;
import com.striveonger.study.leaf.service.ILeafAllocService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 号段表 服务实现类
 * </p>
 * @author Mr.Lee
 * @since 2022-11-23
 */
@Service
public class LeafAllocServiceImpl extends ServiceImpl<LeafAllocMapper, LeafAlloc> implements ILeafAllocService {

    @Override
    public List<LeafAlloc> getAllLeafAllocs() {
        return this.getBaseMapper().getAllLeafAllocs();
    }

    @Override
    @Transactional
    public LeafAlloc updateMaxIdAndGetLeafAlloc(String tag) {
        this.getBaseMapper().updateMaxId(tag);
        return this.getBaseMapper().getLeafAlloc(tag);
    }

    @Override
    @Transactional
    public LeafAlloc updateMaxIdByCustomStepAndGetLeafAlloc(LeafAlloc leafAlloc) {
        this.getBaseMapper().updateMaxIdByCustomStep(leafAlloc);
        return this.getBaseMapper().getLeafAlloc(leafAlloc.getKey());
    }

    @Override
    public List<String> getAllTags() {
        return this.getBaseMapper().getAllTags();
    }
}
