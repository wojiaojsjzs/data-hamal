package com.striveonger.study.filestorage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.striveonger.study.filestorage.entity.Files;
import com.striveonger.study.filestorage.mapper.FilesMapper;
import com.striveonger.study.filestorage.service.IFilesService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 文件表 服务实现类
 * </p>
 *
 * @author Mr.Lee
 * @since 2022-11-07
 */
@Service
public class FilesServiceImpl extends ServiceImpl<FilesMapper, Files> implements IFilesService {

    @Override
    public Files getByHashCode(String hashcode) {
        LambdaQueryWrapper<Files> wrapper = new QueryWrapper<Files>().lambda().select(Files.class, x -> true).eq(Files::getHashcode, hashcode);
        List<Files> list = list(wrapper);
        if (list == null || list.isEmpty()) return null;
        return list.get(0);
    }

    @Override
    public Files getById(String id) {
        LambdaQueryWrapper<Files> wrapper = new QueryWrapper<Files>().lambda().select(Files.class, x -> true).eq(Files::getId, id);
        return getOne(wrapper);
    }
}
