package com.striveonger.study.leaf.web.controller;


import com.striveonger.study.api.leaf.core.ID;
import com.striveonger.study.api.leaf.core.Status;
import com.striveonger.study.core.constant.ResultStatus;
import com.striveonger.study.core.exception.CustomException;
import com.striveonger.study.core.result.Result;
import com.striveonger.study.leaf.core.IDGen;
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

    public LeafController(IDGen gen) {
        this.gen = gen;
    }

    /**
     * 生成ID
     * @param key tag
     * @return Long
     */
    @GetMapping("/gen/{key}")
    public Result<Object> generateIdByKey(@PathVariable("key") String key) {
        ID id = gen.get(key);
        if (Status.SUCCESS.equals(id.getStatus())) {
            return Result.success(id);
        }
        throw new CustomException(ResultStatus.FAIL);
    }

}