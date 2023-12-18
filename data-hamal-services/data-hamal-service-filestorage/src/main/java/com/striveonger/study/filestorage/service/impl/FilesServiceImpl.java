package com.striveonger.study.filestorage.service.impl;


import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
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
        QueryWrapper wrapper = QueryWrapper.create().select(x -> true).eq(Files::getHashcode, hashcode);
        List<Files> list = this.list(wrapper);
        if (list == null || list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    @Override
    public Files getById(String id) {
        QueryWrapper wrapper = QueryWrapper.create().select(x -> true).eq(Files::getId, id);
        return getOne(wrapper);
    }
}
