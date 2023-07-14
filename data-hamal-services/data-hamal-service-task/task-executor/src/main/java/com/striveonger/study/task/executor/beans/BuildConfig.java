package com.striveonger.study.task.executor.beans;

import java.util.List;

/**
 * @author Mr.Lee
 * @description: 组件基础配置
 * @date 2023-07-14 14:35
 */
public abstract class BuildConfig {

    private List<Column> columns;

    public static class Column {

        /**
         * [昨天] 上一个组件传过来的名
         */
        private String originalName;

        /**
         * [今天] 当前组件生成的列
         */
        private String showName;

        /**
         * [明天] 向后传递的名
         */
        private String columnName;

        /**
         * 字段长度
         */
        private Integer columnLength;

        /**
         * <p>字段类型 </p>
         * 11: 整型<br/>12: 浮点型<br/>21: 字符型<br/>22: 大字段<br/>31: 日期<br/>32: 时间<br/>33: 日期时间
         */
        private int columnType;

        /**
         * 列的基础类型
         */
        private int baseType;

        /**
         * 是否为主键
         */
        private boolean isPrimaryKey;

        /**
         * <p>字段状态</p>
         * 0: 本次产生列<br/>1: 上一步传递而来, 需要向下传递<br/>2: 终止传递
         */
        private int status;

        public String getOriginalName() {
            return originalName;
        }

        public void setOriginalName(String originalName) {
            this.originalName = originalName;
        }

        public String getShowName() {
            return showName;
        }

        public void setShowName(String showName) {
            this.showName = showName;
        }

        public String getColumnName() {
            return columnName;
        }

        public void setColumnName(String columnName) {
            this.columnName = columnName;
        }

        public Integer getColumnLength() {
            return columnLength;
        }

        public void setColumnLength(Integer columnLength) {
            this.columnLength = columnLength;
        }

        public int getColumnType() {
            return columnType;
        }

        public void setColumnType(int columnType) {
            this.columnType = columnType;
        }

        public int getBaseType() {
            return baseType;
        }

        public void setBaseType(int baseType) {
            this.baseType = baseType;
        }

        public boolean getIsPrimaryKey() {
            return isPrimaryKey;
        }

        public void setIsPrimaryKey(boolean primaryKey) {
            isPrimaryKey = primaryKey;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }
}
