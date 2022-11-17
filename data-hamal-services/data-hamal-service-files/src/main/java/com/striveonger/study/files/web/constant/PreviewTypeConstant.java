package com.striveonger.study.files.web.constant;

import com.striveonger.study.core.constant.ResultStatus;
import com.striveonger.study.core.exception.CustomException;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Mr.Lee
 * @description:
 * @date 2022-11-12 20:19
 */
public class PreviewTypeConstant {

    private static final Set<String> IMAGE_TYPE = new HashSet<>(Arrays.asList("BMP", "JPG", "JPEG", "PNG", "GIF"));
    private static final Set<String> AUDIO_TYPE = new HashSet<>(Arrays.asList("MP3"));
    private static final Set<String> VIDEO_TYPE = new HashSet<>(Arrays.asList("MP4"));


    public static boolean supportPreview(String type) {
        type = type.toUpperCase();
        return IMAGE_TYPE.contains(type)
            || VIDEO_TYPE.contains(type)
            || AUDIO_TYPE.contains(type);
    }

    public static String typeTag(String type) {
        type = type.toUpperCase();
        if (IMAGE_TYPE.contains(type)) {
            return "image";
        } else if (VIDEO_TYPE.contains(type)) {
            return "video";
        } else if (AUDIO_TYPE.contains(type)) {
            return "audio";
        }
        throw new CustomException(ResultStatus.FAIL, "文件格式不支持预览");
    }

}