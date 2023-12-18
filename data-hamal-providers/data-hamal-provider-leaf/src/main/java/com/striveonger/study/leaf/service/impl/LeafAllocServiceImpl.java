package com.striveonger.study.leaf.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.striveonger.study.leaf.entity.LeafAlloc;
import com.striveonger.study.leaf.mapper.LeafAllocMapper;
import com.striveonger.study.leaf.service.ILeafAllocService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private final Logger log = LoggerFactory.getLogger(LeafAllocServiceImpl.class);

    @Override
    public List<LeafAlloc> getAllLeafAllocs() {
        return this.mapper.getAllLeafAllocs();
    }

    @Override
    @Transactional
    public LeafAlloc updateMaxIdAndGetLeafAlloc(String tag) {
        this.mapper.updateMaxId(tag);
        return this.mapper.getLeafAlloc(tag);
    }

    @Override
    @Transactional
    public LeafAlloc updateMaxIdByCustomStepAndGetLeafAlloc(LeafAlloc leafAlloc) {
        log.info("update leafAlloc {}", leafAlloc);
        this.mapper.updateMaxIdByCustomStep(leafAlloc);
        return this.mapper.getLeafAlloc(leafAlloc.getKey());
    }

    @Override
    public List<String> getAllTags() {
        return this.mapper.getAllTags();
    }

    @Override
    public int count(String tag) {
        return this.mapper.count(tag);
    }

    @Override
    public boolean save(LeafAlloc leafAlloc) {
        return this.mapper.save(leafAlloc);
    }


}
