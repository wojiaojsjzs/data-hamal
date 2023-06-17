package com.striveonger.study.dp.file.analyze;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import org.apache.poi.ss.usermodel.Sheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Mr.Lee
 * @description:
 * @date 2023-06-14 15:19
 */
public class ExcelAnalyze extends FileAnalyze {
    private final Logger log = LoggerFactory.getLogger(ExcelAnalyze.class);

    private final ExcelReader reader;

    public ExcelAnalyze(AnalyzeParams params) {
        super(params);
        this.reader = ExcelUtil.getReader(file, params.getSheetName());
    }

    @Override
    public List<String> head() {
        List<Object> result = reader.readRow(params.getStartLineNum());
        if (result == null) {
            return Collections.emptyList();
        }
        return result.stream().map(Object::toString).collect(Collectors.toList());
    }

    @Override
    public Map<String, List<String>> analyze() {
        List<String> head = head();
        Map<String, List<String>> result = initResultData(head);

        Sheet sheet = reader.getSheet();

        for (int i = params.getStartLineNum() + 1; i <= sheet.getLastRowNum(); i++) {
            List<Object> row = reader.readRow(i);
            fillRowData(result, head, row);

        }
        return result;
    }




}
