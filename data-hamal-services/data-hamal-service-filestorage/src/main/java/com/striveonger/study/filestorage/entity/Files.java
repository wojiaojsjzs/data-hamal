package com.striveonger.study.filestorage.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.striveonger.study.mybatis.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>
 * 文件表
 * </p>
 *
 * @author Mr.Lee
 * @since 2022-11-07
 */
@ApiModel(value = "Files对象", description = "文件表")
public class Files extends BaseEntity {

    private static final long serialVersionUID = 1L;

    public Files() { }

    public Files(String id, String filename, String filepath, String filetype, String hashcode) {
        this.id = id;
        this.filename = filename;
        this.filepath = filepath;
        this.filetype = filetype;
        this.hashcode = hashcode;
    }

    @ApiModelProperty("文件ID")
    @Id
    private String id;

    @ApiModelProperty("文件名")
    private String filename;

    @ApiModelProperty("文件路径")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String filepath;

    @ApiModelProperty("文件类型")
    private String filetype;

    @ApiModelProperty("文件Hash")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String hashcode;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public String getFiletype() {
        return filetype;
    }

    public void setFiletype(String filetype) {
        this.filetype = filetype;
    }

    public String getHashcode() {
        return hashcode;
    }

    public void setHashcode(String hashcode) {
        this.hashcode = hashcode;
    }
}
