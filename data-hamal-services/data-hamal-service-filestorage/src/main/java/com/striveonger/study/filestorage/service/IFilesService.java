package com.striveonger.study.filestorage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.striveonger.study.filestorage.entity.Files;

/**
 * <p>
 * 文件表 服务类
 * </p>
 *
 * @author Mr.Lee
 * @since 2022-11-07
 */
public interface IFilesService extends IService<Files> {

    Files getByHashCode(String hashcode);

    Files getById(String id);
}
