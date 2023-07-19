package com.striveonger.study.task.plugin;

import cn.hutool.core.lang.Dict;
import com.striveonger.study.task.common.executor.step.item.ItemReader;
import com.striveonger.study.task.plugin.entity.SQLInputBuildConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Mr.Lee
 * @description:
 * @date 2023-07-17 14:21
 */
public class SQLInputReader implements ItemReader<Map<String, Object>> {

    private final Logger log = LoggerFactory.getLogger(SQLInputReader.class);

    private SQLInputBuildConfig buildConfig;
    private List<Map<String, Object>> source = null;
    private int start = 0, skip = 5;


    /**
     * 从数据源读取数据
     * TODO: 暂时数据结构用Map代替, 后期再考虑数据传输结构的问题(暂时, 先把流程跑通了, 才是重点哦)
     */
    private List<Map<String, Object>> readSource() {
        // TODO: 临时写法哦~
        if (start >= data.size()) {
            return new ArrayList<>();
        }
        List<Map<String, Object>> result = data.stream().skip(start).limit(skip).collect(Collectors.toList());
        start += skip;
        return result;
    }


    @Override
    public Map<String, Object> read() throws Exception {
        if (Objects.isNull(source) || source.isEmpty()) {
            source = readSource();
        }
        if (source.isEmpty()) {
            return null;
        }
        return source.remove(0);
    }

    // todo: temp 模拟数据
    private final List<Map<String, Object>> data = Arrays.asList(
            Dict.create().set("id", "1").set("name", "1"),
            Dict.create().set("id", "2").set("name", "2"),
            Dict.create().set("id", "3").set("name", "3"),
            Dict.create().set("id", "4").set("name", "4"),
            Dict.create().set("id", "5").set("name", "5"),
            Dict.create().set("id", "6").set("name", "6"),
            Dict.create().set("id", "7").set("name", "7"),
            Dict.create().set("id", "8").set("name", "8"),
            Dict.create().set("id", "9").set("name", "9"),
            Dict.create().set("id", "10").set("name", "10"),
            Dict.create().set("id", "11").set("name", "11"),
            Dict.create().set("id", "12").set("name", "12"),
            Dict.create().set("id", "13").set("name", "13"),
            Dict.create().set("id", "14").set("name", "14"),
            Dict.create().set("id", "15").set("name", "15"),
            Dict.create().set("id", "16").set("name", "16"),
            Dict.create().set("id", "17").set("name", "17"),
            Dict.create().set("id", "18").set("name", "18"),
            Dict.create().set("id", "19").set("name", "19"),
            Dict.create().set("id", "20").set("name", "20"),
            Dict.create().set("id", "21").set("name", "21"),
            Dict.create().set("id", "22").set("name", "22"),
            Dict.create().set("id", "23").set("name", "23"),
            Dict.create().set("id", "24").set("name", "24"),
            Dict.create().set("id", "25").set("name", "25")
    );

}


