package com.striveonger.study.dp.file;

import com.striveonger.study.core.constant.ResultStatus;
import com.striveonger.study.core.exception.CustomException;
import com.striveonger.study.dp.file.analyze.CSVAnalyze;
import com.striveonger.study.dp.file.analyze.ExcelAnalyze;
import com.striveonger.study.dp.file.analyze.FileAnalyze;
import com.striveonger.study.dp.file.analyze.JSONAnalyze;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mr.Lee
 * @description:
 * @date 2023-06-09 11:10
 */
public class ReadFileHandler {
    private final Logger log = LoggerFactory.getLogger(ReadFileHandler.class);


    public static class Builder {
        private FileType fileType;

        private FileAnalyze.AnalyzeParams params;

        public static Builder builder() {
            Builder builder = new Builder();
            builder.params = new FileAnalyze.AnalyzeParams();
            return builder;
        }

        public Builder fileType(FileType type) {
            this.fileType = type;
            return this;
        }

        public Builder path(String path) {
            params.setPath(path);
            return this;
        }

        public Builder startLineNum(int startLineNum) {
            params.setStartLineNum(startLineNum);
            return this;
        }

        public Builder separator(String separator) {
            params.setSeparator(separator);
            return this;
        }

        public Builder sheetName(String sheetName) {
            params.setSheetName(sheetName);
            return this;
        }

        public Builder choosePath(String choosePath) {
            params.setChoosePath(choosePath);
            return this;
        }

        public FileAnalyze build() {
            if (this.fileType == FileType.CSV) {
                return new CSVAnalyze(params);
            } else if (this.fileType == FileType.EXCEL) {
                return new ExcelAnalyze(params);
            } else if (this.fileType == FileType.JSON) {
                return new JSONAnalyze(params);
            }
            throw new CustomException(ResultStatus.NON_SUPPORT, "不支持的fileType, 导致无法初始化文件解析器");
        }
    }

    public enum FileType {
        CSV, EXCEL, JSON
    }

}
