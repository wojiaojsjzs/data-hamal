package com.striveonger.study.leaf.entity;


import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;

/**
 * <p>
 * 号段表
 * </p>
 *
 * @author Mr.Lee
 * @since 2022-11-23
 */
@Table(value ="alloc")
public class LeafAlloc {

    @Id
    @Column(value = "biz_tag")
    private String key;
    private long maxId;
    private int step;
    private String description;
    private String updateTime;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public long getMaxId() {
        return maxId;
    }

    public void setMaxId(long maxId) {
        this.maxId = maxId;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}
