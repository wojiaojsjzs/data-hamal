package com.striveonger.study.core.vo;

/**
 * @author Mr.Lee
 * @description:
 * @date 2022-11-12 14:58
 */
public class BasicQueryVo {

    private Integer pageSize;

    private Integer pageNum;

    private String search;

    public Integer getPageSize() {
        return pageSize == null ? 15 : pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPageNum() {
        return pageNum == null ? 1 : pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }
}