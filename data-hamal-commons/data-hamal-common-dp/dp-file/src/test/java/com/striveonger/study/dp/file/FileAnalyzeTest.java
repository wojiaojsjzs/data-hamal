package com.striveonger.study.dp.file;

import cn.hutool.core.io.FileUtil;
import com.alibaba.fastjson.JSONObject;
import com.striveonger.study.dp.file.analyze.FileAnalyze;
import com.striveonger.study.dp.file.utils.ExcelSheetUtils;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.striveonger.study.dp.file.ReadFileHandler.FileType.*;

/**
 * @author Mr.Lee
 * @description:
 * @date 2023-06-09 15:29
 */
public class FileAnalyzeTest {

    @Test
    public void testCSVReader() {
        String path = "/Users/striveonger/development/workspace/favor/data-hamal/data-hamal-commons/data-hamal-common-dp/dp-file/src/test/resources/airline.csv";
        FileAnalyze analyze = ReadFileHandler.Builder.builder().fileType(CSV).path(path).startLineNum(0).separator(",").build();
        System.out.println(analyze.fileName());
        System.out.println(analyze.head());
        Map<String, List<String>> map = analyze.analyze();
        System.out.println(map.size());
        System.out.println(map.entrySet().stream().findAny().get().getValue().size());

    }

    @Test
    public void testExcelReader() {

        String path = "/Users/striveonger/development/workspace/favor/data-hamal/data-hamal-commons/data-hamal-common-dp/dp-file/src/test/resources/airline.xlsx";

        List<String> sheets = ExcelSheetUtils.getSheetNames(path);
        System.out.println(sheets);
        FileAnalyze analyze = ReadFileHandler.Builder.builder().fileType(EXCEL).path(path).startLineNum(0).sheetName(sheets.get(0)).build();
        System.out.println(analyze.fileName());
        List<String> head = analyze.head();
        System.out.println(head);
        Map<String, List<String>> map = analyze.analyze();
        int size = map.entrySet().stream().findAny().get().getValue().size();
        System.out.println(size);

        // 将数据以JSON的格式写到磁盘上
        List<Map<String, String>> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            Map<String, String> row = new HashMap<>();
            for (String field : head) {
                row.put(field, map.get(field).get(i));
            }
            list.add(row);
        }
        String s = JSONObject.toJSONString(list);
        FileUtil.writeBytes(s.getBytes(StandardCharsets.UTF_8), "/Users/striveonger/development/workspace/favor/data-hamal/data-hamal-commons/data-hamal-common-dp/dp-file/src/test/resources/airline.json");
    }

    @Test
    public void testJSONReader() {
        String path = "/Users/striveonger/development/workspace/favor/data-hamal/data-hamal-commons/data-hamal-common-dp/dp-file/src/test/resources/airline.json";

        String choosePath = "root";
        // String choosePath = "root.Species";
        FileAnalyze analyze = ReadFileHandler.Builder.builder().fileType(JSON).path(path).startLineNum(0).choosePath(choosePath).build();
        System.out.println(analyze.fileName());
        List<String> head = analyze.head();
        System.out.println(head);
        Map<String, List<String>> map = analyze.analyze();
        int size = map.entrySet().stream().findAny().get().getValue().size();
        System.out.println(size);
        System.out.println(JSONObject.toJSONString(map));

    }

}