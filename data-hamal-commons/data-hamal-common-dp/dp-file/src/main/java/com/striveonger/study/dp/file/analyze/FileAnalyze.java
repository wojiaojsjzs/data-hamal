package com.striveonger.study.dp.file.analyze;

import com.striveonger.study.core.constant.ResultStatus;
import com.striveonger.study.core.exception.CustomException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.*;

/**
 * @author Mr.Lee
 * @description: 文本解析类
 * @date 2023-06-09 14:03
 */
public abstract class FileAnalyze {

    private final Logger log = LoggerFactory.getLogger(FileAnalyze.class);

    protected final AnalyzeParams params;

    protected final File file;

    protected FileAnalyze(AnalyzeParams params) {
        this.params = params;
        this.file = new File(params.getPath());
        if (!file.exists() || file.isDirectory()) {
            throw new CustomException(ResultStatus.NOT_FOUND, String.format("[%s] path not find file...", params.getPath()));
        }
    }


    public abstract List<String> head();

    public abstract Map<String, List<String>> analyze();

    protected Map<String, List<String>> initResultData(List<String> head) {
        Map<String, List<String>> result = new HashMap<>(head.size());
        for (String field : head) result.put(field, new ArrayList<>());
        return result;
    }

    protected void fillRowData(Map<String, List<String>> data, List<String> head, List<Object> row) {
        // 处理行数据
        cut(row, head.size());
        // 加入到结果中
        for (int i = 0; i < head.size(); i++) {
            String key = head.get(i);
            List<String> column = data.get(key);
            column.add(Objects.toString(row.get(i)));
        }
    }

    private void cut(List<Object> row, int size) {
        int diff = size - row.size();
        for (int i = 0; i < Math.abs(diff); i++) {
            if (diff > 0) {
                row.add("");
            } else {
                row.remove(row.size() - 1);
            }
        }
    }


    public String fileName() {
        return file.getName();
    }

    public static class AnalyzeParams {
        /**
         * 文件绝对路径
         */
        private String path;

        /**
         * 数据的起始行
         */
        private int startLineNum = 0;

        /**
         * 分割符(Csv专用)
         */
        private String separator;

        /**
         * sheet名(Excel专用)
         */
        private String sheetName;

        /**
         * 选择路径(Json专用)
         */
        private String choosePath;

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public int getStartLineNum() {
            return startLineNum;
        }

        public void setStartLineNum(int startLineNum) {
            this.startLineNum = startLineNum;
        }

        public String getSeparator() {
            return separator;
        }

        public void setSeparator(String separator) {
            this.separator = separator;
        }

        public String getSheetName() {
            return sheetName;
        }

        public void setSheetName(String sheetName) {
            this.sheetName = sheetName;
        }

        public String getChoosePath() {
            return choosePath;
        }

        public void setChoosePath(String choosePath) {
            this.choosePath = choosePath;
        }
    }
}
