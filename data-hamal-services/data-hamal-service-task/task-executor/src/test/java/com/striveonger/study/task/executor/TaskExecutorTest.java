package com.striveonger.study.task.executor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.striveonger.study.core.utils.JacksonUtils;
import com.striveonger.study.task.executor.entity.Step;
import com.striveonger.study.task.executor.entity.Task;
import com.striveonger.study.task.plugin.entity.SQLInputBuildConfig;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mr.Lee
 * @description:
 * @date 2023-07-14 17:11
 */
public class TaskExecutorTest {
    private final Logger log = LoggerFactory.getLogger(TaskExecutorTest.class);

    @Test
    public void test() {
        String body = """
                    {
                        "id": "201405271630458970030",
                        "params": [
                            {
                                "name": "runtime",
                                "value": "2023-07-14 17:23:00"
                            },
                            {
                                "name": "runtype",
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
        Task task = JacksonUtils.toObject(body, Task.class);
        System.out.println(task.getId());
    }


    @Test
    public void temp() {
        String body = """
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
                }
                """;


        ObjectMapper mapper = JacksonUtils.getMapper();
        mapper.registerSubtypes(new NamedType(SQLInputBuildConfig.class, "SQL_INPUT"));

        Step step = JacksonUtils.toObject(body, Step.class);
        System.out.println(step.getDisplayName());
        System.out.println(step.getType());


    }

}