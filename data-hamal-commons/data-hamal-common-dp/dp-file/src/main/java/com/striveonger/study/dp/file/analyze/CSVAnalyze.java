package com.striveonger.study.dp.file.analyze;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Tuple;
import cn.hutool.core.util.StrUtil;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.striveonger.study.core.constant.ResultStatus;
import com.striveonger.study.core.exception.CustomException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.striveonger.study.dp.file.utils.EncodingDetect.getCharset;

/**
 * @author Mr.Lee
 * @description:
 * @date 2023-06-09 15:07
 */
public class CSVAnalyze extends FileAnalyze {
    private final Logger log = LoggerFactory.getLogger(CSVAnalyze.class);

    private final List<String> lines;

    private final CSVParser parser;

    public CSVAnalyze(AnalyzeParams params) {
        super(params);
        char separator = awaySeparator(params.getSeparator());
        this.parser = new CSVParserBuilder().withSeparator(separator).build();
        try {
            String path = params.getPath();
            this.lines = Files.lines(Paths.get(path), getCharset(path)).filter(StrUtil::isNotEmpty).collect(Collectors.toList());
        } catch (IOException e) {
            log.error("{} 文件读取失败...", params.getPath(), e);
            throw new CustomException(ResultStatus.ACCIDENT, "文件读取失败");
        }
    }

    @Override
    public List<String> head() {
        int start = params.getStartLineNum();
        List<String> result = new ArrayList<>();
        Tuple tuple = null;
        do {
            String line = lines.get(start);
            tuple = parseLine(line);
            String[] arr = tuple.get(1);
            result.addAll(Arrays.asList(arr));
        } while (Convert.toBool(tuple.get(0)) && !"tab".equals(params.getSeparator()) && !"space".equals(params.getSeparator()) && ++start < lines.size());

        return result;
    }

    @Override
    public Map<String, List<String>> analyze() {
        List<String> head = head();
        // 1. 初始化容器 (防止Map扩容)
        Map<String, List<String>> result = initResultData(head);

        // 2. 开始填充数据 O(M*N)
        for (int i = params.getStartLineNum() + 1; i < lines.size(); i++) {
            List<Object> row = new ArrayList<>();
            Tuple tuple = null;
            do {
                String line = lines.get(i);
                tuple = parseLine(line);
                String[] arr = tuple.get(1);
                row.addAll(Arrays.asList(arr));
            } while (Convert.toBool(tuple.get(0)) && !"tab".equals(params.getSeparator()) && !"space".equals(params.getSeparator()) && ++i < lines.size());

            fillRowData(result, head, row);
        }
        return result;
    }

    private Tuple parseLine(String line) {
        try {
            String[] row = parser.parseLineMulti(line);
            return new Tuple(parser.isPending(), row);
        } catch (IOException e) {
            log.error(line + ":" + e);
            throw new CustomException(ResultStatus.ACCIDENT, "文件内容解析失败");
        }
    }


    private Character awaySeparator(String separator) {
        if ("tab".equals(separator)) {
            separator = "\t";
        } else if ("space".equals(separator)) {
            separator = " ";
        }
        return Convert.toChar(separator);
    }

}
