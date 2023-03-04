package com.striveonger.study.filestorage.web.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.striveonger.study.core.constant.ResultStatus;
import com.striveonger.study.core.exception.CustomException;
import com.striveonger.study.core.result.Result;
import com.striveonger.study.core.utils.FileHash;
import com.striveonger.study.core.vo.BasicQueryVo;
import com.striveonger.study.filestorage.entity.Files;
import com.striveonger.study.filestorage.service.IFilesService;
import com.striveonger.study.filestorage.web.utils.FileStreamUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Objects;


/**
 * @author Mr.Lee
 * @description: 文件存储服务
 * @date 2022-11-07 21:45
 */
@Controller
public class FileStorageController {
    private final Logger log = LoggerFactory.getLogger(FileStorageController.class);

    @Resource
    private IFilesService service;

    @Value("${data.hamal.file.storage}")
    private String storage;

    /**
     * 上传文件
     *
     * @param files
     * @return
     */
    @PostMapping("/upload")
    @ResponseBody
    public Result<Object> upload(MultipartFile[] files) {
        if (files == null || files.length == 0) {
            throw new CustomException(ResultStatus.NOT_FOUND);
        }
        for (MultipartFile file : files) {
            String filename = file.getOriginalFilename();
            if (StrUtil.isBlank(filename)) continue;
            log.info("upload filename: {}", filename);
            try {
                // TODO: 生成id的方案，后面可以换成 "美团的叶子"
                String id = IdUtil.simpleUUID();
                // 1. generate file info
                String filetype = StrUtil.subAfter(filename, '.', true);
                String filepath = String.format("%s%s%s%s.%s", File.separator, DateUtil.today(), File.separator, id, filetype);
                File target = new File(storage + filepath);
                String hashcode = FileHash.SHA512.code(file.getInputStream());
                log.info("file hashcode: {}", hashcode);
                // 2. check hashcode is exist
                if (StrUtil.isBlank(hashcode)) {
                    throw new CustomException("generate file hashcode error...");
                }
                Files hold = service.getByHashCode(hashcode);
                if (Objects.nonNull(hold)) {
                    filepath = hold.getFilepath();
                } else {
                    target.getParentFile().mkdirs();
                    // save file to disk
                    file.transferTo(target);
                }
                Files entity = new Files(id, filename, filepath, filetype, hashcode);
                service.save(entity);
            } catch (IOException e) {
                log.error("save file error", e);
                throw new CustomException("save file error");
            }
        }
        return Result.success().message("文件上传成功");
    }

    /**
     * 文件列表
     *
     * @param vo
     * @return
     */
    @GetMapping("/list")
    @ResponseBody
    public Result<IPage<Files>> list(BasicQueryVo vo) {
        IPage<Files> page = new Page<>(vo.getPageNum(), vo.getPageSize());
        LambdaQueryWrapper<Files> wrapper = new LambdaQueryWrapper<>();
        if (StrUtil.isNotBlank(vo.getSearch())) {
            wrapper.like(Files::getFilename, vo.getSearch());
        }
        wrapper.orderByDesc(Files::getCreateTime);
        return Result.success(service.page(page, wrapper));
    }

    @GetMapping("/download")
    public void download(HttpServletRequest request, HttpServletResponse response, String id) {
        Files entity = service.getById(id);
        if (Objects.isNull(entity)) {
            throw new CustomException(ResultStatus.NOT_FOUND, "未找到文件");
        }
        String filepath = storage + entity.getFilepath();
        File file = new File(filepath);
        if (!file.exists()) {
            throw new CustomException(ResultStatus.NOT_FOUND, "文件不存在");
        }
        FileStreamUtils.export(entity.getFilename(), request, response, file);
    }

    @GetMapping("/preview")
    public void preview(HttpServletRequest request, HttpServletResponse response, String id) {
        Files entity = service.getById(id);
        if (Objects.isNull(entity)) {
            throw new CustomException(ResultStatus.NOT_FOUND, "未找到文件");
        }
        String filepath = storage + entity.getFilepath();
        File file = new File(filepath);
        if (!file.exists()) {
            throw new CustomException(ResultStatus.NOT_FOUND, "文件不存在");
        }
        FileStreamUtils.preview(entity.getFilename(), request, response, file);
    }
}