package com.striveonger.study.files.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.striveonger.study.files.entity.Files;
import com.striveonger.study.files.mapper.FilesMapper;
import com.striveonger.study.files.service.IFilesService;
import org.springframework.stereotype.Service;

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

}
