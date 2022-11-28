package com.striveonger.study.filestorage.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
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
public class Files implements Serializable {

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
    @TableId
    private String id;

    @ApiModelProperty("文件名")
    private String filename;

    @ApiModelProperty("文件路径")
    @TableField(select = false)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String filepath;

    @ApiModelProperty("文件类型")
    private String filetype;

    @ApiModelProperty("文件Hash")
    @TableField(select = false)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String hashcode;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS", timezone = "GMT+8")
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS", timezone = "GMT+8")
    @TableField(select = false)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDateTime updateTime;

    @ApiModelProperty("删除标志（0否 1是）")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @TableField(select = false)
    private Boolean deleted;

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

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }
}
