package com.striveonger.study.files.web.controller;

import com.striveonger.study.core.result.Result;
import com.striveonger.study.tools.FileHash;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;


/**
 * @author Mr.Lee
 * @description: 文件存储服务
 * @date 2022-11-07 21:45
 */
@Controller
public class FilesController {
    private final Logger log = LoggerFactory.getLogger(FilesController.class);


    @PostMapping("/upload")
    @ResponseBody
    public Result<Object> upload(MultipartFile file) {
        String filename = file.getOriginalFilename();
        log.info("upload filename: {}", filename);
        return Result.success().message("文件上传成功");
    }



}