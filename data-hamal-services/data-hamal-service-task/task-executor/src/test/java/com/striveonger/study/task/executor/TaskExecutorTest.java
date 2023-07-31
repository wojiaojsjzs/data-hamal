package com.striveonger.study.task.executor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.striveonger.study.core.utils.JacksonUtils;
import com.striveonger.study.task.common.constant.TaskStatus;
import com.striveonger.study.task.executor.entity.TaskBody;
import com.striveonger.study.task.plugin.entity.SQLInputBuildConfig;
import com.striveonger.study.task.plugin.entity.TableOutputBuildConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

/**
 * @author Mr.Lee
 * @description:
 * @date 2023-07-14 17:11
 */
public class TaskExecutorTest {
    private final Logger log = LoggerFactory.getLogger(TaskExecutorTest.class);

    private TaskBody task;

    @BeforeEach
    public void before() {
        String body = """
                    {
                        "id": "201405271630458970030",
                        "params": [
                            {
                                "name": "runTime",
                                "value": "2023-07-14 17:23:00"
                            },
                            {
                                "name": "runType",
                                "value": "schedule"
                            }
                        ],
                        "steps": [
                            {
                                "id": "3386114898216636417",
                                "displayName": "SQL输入",
                                "type": "SQL_INPUT",
                                "buildConfig": {
                                    "driverPath": "/Users/striveonger/development/repository/org/postgresql/postgresql/42.2.6",
                                    "driverClassName": "org.postgresql.Driver",
                                    "url": "jdbc:postgresql://localhost:5432/postgres",
                                    "username": "postgres",
                                    "password": "123456",
                                    "sql": "SELECT * FROM test.air",
                                    "columns": [
                                        {"originalName":"","showName":"id","columnName":"id","columnLength":255,"columnType":1,"baseType":12,"isPrimaryKey":true,"status":0},
                                        {"originalName":"","showName":"name","columnName":"name","columnLength":255,"columnType":1,"baseType":12,"isPrimaryKey":false,"status":0},
                                        {"originalName":"","showName":"age","columnName":"age","columnLength":255,"columnType":1,"baseType":12,"isPrimaryKey":false,"status":0}
                                    ]
                                }
                            },
                            {
                                "id": "3386114898794823681",
                                "displayName": "表输出",
                                "type": "TABLE_OUTPUT",
                                "buildConfig": {
                                    "driverPath": "/Users/striveonger/development/repository/org/postgresql/postgresql/42.2.6",
                                    "driverClassName": "org.postgresql.Driver",
                                    "url": "jdbc:postgresql://localhost:5432/postgres",
                                    "username": "postgres",
                                    "password": "123456",
                                    "tableName": "test.water",
                                    "nonExistCreate": true,
                                    "updateType": "ADDITION",
                                    "columns": [
                                        {"originalName":"","showName":"id","columnName":"id","columnLength":255,"columnType":1,"baseType":12,"isPrimaryKey":true,"status":0},
                                        {"originalName":"","showName":"name","columnName":"name","columnLength":255,"columnType":1,"baseType":12,"isPrimaryKey":false,"status":0},
                                        {"originalName":"","showName":"age","columnName":"age","columnLength":255,"columnType":1,"baseType":12,"isPrimaryKey":false,"status":0}
                                    ]
                                }
                            }
                        ],
                        "topology": {
                            "3386114898216636417": ["3386114898794823681"],
                            "3386114898794823681": []
                        }
                    }
                """;
        // TODO: 临时手动 注册解析类型与type之间的对应关系
        ObjectMapper mapper = JacksonUtils.getMapper();
        mapper.registerSubtypes(new NamedType(SQLInputBuildConfig.class, "SQL_INPUT"));
        mapper.registerSubtypes(new NamedType(TableOutputBuildConfig.class, "TABLE_OUTPUT"));
        this.task = JacksonUtils.toObject(body, TaskBody.class);
    }

    @Test
    public void test() {
        assert task != null;
        System.out.println(task.getId());
        TaskExecutor executor = new TaskExecutor();
        TaskStatus status = executor.exec(task);
        System.out.println(status);

    }

}