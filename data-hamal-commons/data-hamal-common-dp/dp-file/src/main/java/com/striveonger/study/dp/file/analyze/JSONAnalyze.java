package com.striveonger.study.dp.file.analyze;

import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.parser.JSONReaderScanner;
import com.alibaba.fastjson.parser.JSONToken;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.striveonger.study.core.constant.ResultStatus;
import com.striveonger.study.core.exception.CustomException;
import com.striveonger.study.dp.file.utils.EncodingDetect;
import org.apache.commons.compress.utils.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author Mr.Lee
 * @description:
 * @date 2023-06-15 15:46
 */
public class JSONAnalyze extends FileAnalyze {
    private final Logger log = LoggerFactory.getLogger(JSONAnalyze.class);

    private final JSONSchema schema;

    private Function<Object, String> formatItem = o -> {
        if (o instanceof JSONArray) {
            return ((JSONArray) o).stream().map(Object::toString).collect(Collectors.joining(","));
        }
        return o.toString();
    };

    public JSONAnalyze(AnalyzeParams params) {
        super(params);
        this.schema = inferSchema();
    }

    @Override
    public List<String> head() {
        String evalPath = params.getChoosePath().substring(params.getChoosePath().indexOf(".") + 1);
        boolean isRoot = "root".equals(evalPath);
        LevelElement element = isRoot ? schema.root : schema.getByFullKey(params.getChoosePath());
        if (Objects.isNull(element)) {
            throw new NullPointerException(StrUtil.format("LevelElement 为 null. choosePath: {}", params.getChoosePath()));
        }
        return Optional.of(element).map(LevelElement::getChildren).orElse(Lists.newArrayList()).stream().filter(LevelElement::isLeaf).map(LevelElement::getLabel).collect(Collectors.toList());
    }

    @Override
    public Map<String, List<String>> analyze() {

        FileReader reader = new FileReader(file, EncodingDetect.getCharset(file));

        Object object = JSON.parse(reader.readString());
        String evalPath = params.getChoosePath().substring(params.getChoosePath().indexOf(".") + 1);
        boolean isRoot = "root".equals(evalPath);
        Object eval = isRoot ? JSONPath.eval(object, "$") : JSONPath.eval(object, "$." + evalPath);

        List<String> head = head();
        Map<String, List<String>> result = initResultData(head);

        Function<JSONObject, List<Object>> convertRowData = item -> head.stream().map(item::get).map(formatItem).collect(Collectors.toList());

        if (eval instanceof JSONObject) {
            JSONObject item = (JSONObject) eval;
            List<Object> row = convertRowData.apply(item);
            fillRowData(result, head, row);
        } else if (eval instanceof JSONArray) {
            JSONArray array = (JSONArray) eval;
            List<List<Object>> rows = array.stream().filter(JSONObject.class::isInstance).map(JSONObject.class::cast).map(convertRowData).collect(Collectors.toList());
            for (List<Object> row : rows) {
                fillRowData(result, head, row);
            }
        }
        return result;
    }

    /**
     * Json 文件结构推导<br/>
     * 本方法，需要对json 文件 进行基本规定
     * <ul>
     * <li> 遇到 { 层级 + 1</li>
     * <li>嵌套层级不要超过100</li>
     * <li>数组只能包含值 或{}对象,{}中可以继续包含数组,不允许得[[ </li>
     * <li>数组元素，只推到第一组元素的结构，其它忽略,建议数组中的对象结构保持统一</li>
     * </ul>
     * @return 推导出来的结构
     * @throws Exception
     */
    private JSONSchema inferSchema() {
        try {
            boolean isFirst = true;
            JSONSchema schema = new JSONSchema();
            JSONReaderScanner scanner = new JSONReaderScanner(new java.io.FileReader(file));
            boolean skip = false;
            int level = 0;

            // 临时变量，主要用于去除一些非对象级的key的收集
            int tmpLevel = 0;
            String tmpKey = "";

            Predicate<Integer> expectToken = null;

            // 默认处理100层级，不做扩容了
            int[] skipArray = new int[100];

            while (!scanner.isEOF()) {
                scanner.nextToken();
                int token = scanner.token();
                String name = JSONToken.name(token);
                if (expectToken != null) {
                    Assert.isTrue(expectToken.test(token), "JSON结构不满足要求");
                    expectToken = null;
                }
                if (tmpLevel > 0 && token == JSONToken.COLON) {
                    // 本次是：，表示上期是对象的key，刚记录
                    schema.add(tmpLevel, tmpKey);
                    tmpLevel = 0;
                }
                if (token == JSONToken.LBRACE) {// ("{"),
                    if (isFirst) {
                        schema.root.setIsObjectOrArray(0);
                        isFirst = false;
                    }
                    level++;
                }
                if (token == JSONToken.RBRACE) {// ("}"),
                    // 指定对应层级，对象数据解析完毕，增加指示变量为1
                    level--;
                    schema.back(level);
                    if(schema.getCurrent().isAarray()){
                        skipArray[level] = 1;
                    }
                }
                if (token == JSONToken.LBRACKET) {// ("["),
                    if (isFirst) {
                        schema.root.setIsObjectOrArray(1);
                        isFirst = false;
                    }
                    // 设置当前对象为array
                    schema.setArrayType();
                    expectToken = (t) -> t == JSONToken.LBRACE || (t >= JSONToken.LITERAL_INT && t <= JSONToken.NULL) || t == JSONToken.RBRACKET;
                    // 优化的小手段(如果父结点已经处理完数组的第一组数据，则对应数组其余不再做处理）
                    // 为0的判断是为兼容不规范的json 以[ 开头的json文件
                    skipArray[level] = level == 0 ? 0 : skipArray[level - 1];
                }
                if (token == JSONToken.RBRACKET) {// ("]"),
                    // 当解析到 ] ,表示当前数组已经处理完成,迁移相关对象
                    skipArray[level] = 0;
                    schema.endArray();
                    schema.back(level-1);
                }
                // skip the rest array element 和 不必要的token(:,{等）
                if (!skip && name.equals("string") && IntStream.range(0, level).noneMatch(i -> skipArray[i] > 0)) {
                    // 获取Key值
                    tmpKey = scanner.stringVal();
                    tmpLevel = level;
                }
                skip = token == JSONToken.COLON;//token为:
            }
            return schema;
        } catch (FileNotFoundException e) {
            throw new CustomException(ResultStatus.NOT_FOUND, String.format("文件[%s]读取失败...", params.getPath()));
        }
    }

    private static class JSONSchema {

        @JSONField(name = "root")
        public LevelElement root = new LevelElement("root", 0);

        private LevelElement current = root;

        /**
         * 用于辅助查询的节点的集合
         */
        private final Map<String, LevelElement> indexKeys = new HashMap<>();

        /**
         * 设置当前节点为 Array类型
         */
        public void setArrayType() {
            current.setAarray(true);
        }


        public void back(int level){
            if(current.getLevel()>level) {
                current = Optional.ofNullable(current.getParent()).orElse(root);
            }
        }

        /**
         * 增加一个节点
         *
         * @param level 层级
         * @param key 字段值
         */
        public void add(int level, String key) {

            // 层级增加
            if (current.getLevel() < level) {
                String fullKey = current.genChildFullKey(key);
                if (indexKeys.containsKey(fullKey)) {
                    current = indexKeys.get(fullKey);
                    return;
                }
                LevelElement addLE = new LevelElement(key, level);
                addLE.setParent(current);
                current = addLE;
                indexKeys.put(fullKey, current);

            } else {
                int level1 = current.getLevel();
                // 可能存在，一个嵌套很深的目录，结束后，回退层级会比较多
                while (level1-- >= level) {
                    current = current.getParent();
                }
                String fullKey = current.genChildFullKey(key);
                if (!indexKeys.containsKey(fullKey)) {
                    LevelElement addLE = new LevelElement(key, level);
                    addLE.setParent(current);
                    current = addLE;
                    indexKeys.put(fullKey, addLE);
                } else {
                    current = indexKeys.get(fullKey);
                }
            }

        }

        /**
         * 用于检测空数组的情况，这种场景下不计算结构
         */
        public void endArray() {
            if (current.isAarray() && current.getChildren().isEmpty()) {
                current.getParent().getChildren().remove(current);
            }
        }
        /**
         * 获取节点
         *
         * @param fullKey
         * @return
         */
        public LevelElement getByFullKey(String fullKey) {
            return indexKeys.get(fullKey);
        }

        /**
         * 判断当前节点的父节点是不是数据类型
         * @return
         */
        public boolean prentIsArray() {
            return Optional.ofNullable(current).map(LevelElement::getParent).map(LevelElement::isAarray).orElse(false);
        }

        public LevelElement getCurrent() {
            return current;
        }
    }

    private static class LevelElement {
        /**
         * 当前节点的 key 值
         */
        private String label;
        /**
         *  当前节点的key 全路径值
         */
        private String fullKey;
        /**
         * 所属层级，遇 { 表示一个层级的开始
         */
        private int level;

        @JsonIgnore
        private LevelElement parent;

        private boolean isAarray;
        /**
         * 当前节点的所有子结点
         */
        @JSONField(name = "children")
        private List<LevelElement> children = new ArrayList<>();

        /**
         * 是否是叶子节点，用来表示当前节点是否一个值类型 <br/>
         * 如果 当前节点是 array 但，array 中的值 都为 值（int,string,date),仍为一个值类型
         * @return
         */
        public boolean isLeaf() {
            return children.isEmpty();
        }

        /**
         * disabled:当非叶子节点
         * @return
         */
        public boolean isdisabled()  {
            return children.isEmpty();
        }


        /**
         * Object:0,Array:1
         */
        public int isObjectOrArray;

        public LevelElement(String key, int level) {
            this.label  = key;
            this.level = level;
            if (level == 0) {
                fullKey = "root";
            }
        }

        public void setParent(LevelElement parent) {
            this.parent = parent;
            parent.getChildren().add(this);
            this.fullKey = String.format("%s.%s", parent.getFullKey(), label );
        }

        @Override
        public String toString() {
            return "LevelElement{" + "fullKey='" + fullKey + '\'' + ", level=" + level + ", isAarray=" + isAarray + '}';
        }

        /**
         * 生成下一节点key
         * @param childKey
         * @return
         */
        public  String genChildFullKey(String childKey){
            return String.format("%s.%s", getFullKey(), childKey);
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public String getFullKey() {
            return fullKey;
        }

        public void setFullKey(String fullKey) {
            this.fullKey = fullKey;
        }

        public int getLevel() {
            return level;
        }

        public void setLevel(int level) {
            this.level = level;
        }

        public LevelElement getParent() {
            return parent;
        }

        public boolean isAarray() {
            return isAarray;
        }

        public void setAarray(boolean aarray) {
            isAarray = aarray;
        }

        public List<LevelElement> getChildren() {
            return children;
        }

        public void setChildren(List<LevelElement> children) {
            this.children = children;
        }

        public int getIsObjectOrArray() {
            return isObjectOrArray;
        }

        public void setIsObjectOrArray(int isObjectOrArray) {
            this.isObjectOrArray = isObjectOrArray;
        }
    }
}
