package com.striveonger.study.leaf.web.controller;


import com.striveonger.study.core.constant.ResultStatus;
import com.striveonger.study.core.exception.CustomException;
import com.striveonger.study.core.result.Result;
import com.striveonger.study.leaf.constants.Status;
import com.striveonger.study.leaf.core.IDGen;
import com.striveonger.study.leaf.core.common.ID;
import com.striveonger.study.leaf.entity.LeafAlloc;
import com.striveonger.study.leaf.service.ILeafAllocService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author Mr.Lee
 * @description:
 * @date 2022-11-25 00:44
 */
@RestController
public class LeafController {
    private final Logger log = LoggerFactory.getLogger(LeafController.class);

    private final IDGen gen;

    private final ILeafAllocService service;

    public LeafController(IDGen gen, ILeafAllocService service) {
        this.gen = gen;
        this.service = service;
    }

    /**
     * 生成ID
     * @param key tag
     * @return Long
     */
    @GetMapping("/gen/{key}")
    public Result<Long> generateIdByKey(@PathVariable("key") String key) {
        ID id = gen.get(key);
        if (Status.SUCCESS.equals(id.getStatus())) {
            return Result.success(id.getId());
        }
        throw new CustomException(ResultStatus.FAIL);
    }

    /**
     * 注册tag
     * @param key tag
     * @return result
     */
    @PutMapping("/register/{key}")
    private Result<Object> registerKey(@PathVariable("key") String key) {
        synchronized (key.intern()) {
            int count = service.count(key);
            if (count == 0) {
                LeafAlloc alloc = new LeafAlloc();
                alloc.setKey(key);
                alloc.setStep(1);
                alloc.setMaxId(1);
                boolean flag = service.save(alloc);
                return Result.status(flag);
            } else {
                return Result.fail();
            }
        }
    }


}