package com.striveonger.study.task.executor;

import com.striveonger.study.core.utils.JacksonUtils;
import com.striveonger.study.task.executor.beans.BuildConfig;
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
                                            "driverClassName": "org.postgresql.Driver",\s
                                            "url": "jdbc:postgresql://localhost:5432/postgres",\s
                                            "username": "postgres",\s
                                            "password": "123456",
                                            "sql": "SELECT * FROM test.air"
                                            "columns": [
                                                {"originalName":"","showName":"id","columnName":"id","columnLength":255,"columnType":1,"baseType":12,"isPrimaryKey":true,"status":0},
                                                {"originalName":"","showName":"name","columnName":"name","columnLength":255,"columnType":1,"baseType":12,"isPrimaryKey":false,"status":0},
                                                {"originalName":"","showName":"age","columnName":"age","columnLength":255,"columnType":1,"baseType":12,"isPrimaryKey":false,"status":0}
                                            ]
                                           \s
                                        }
                            
                                    },
                                    {
                                        "id": "3386114898794823681",
                                        "displayName": "表输出",
                                        "type": "TABLE_OUTPUT",
                                        "buildConfig": {
                                            "driverPath": "/Users/striveonger/development/repository/org/postgresql/postgresql/42.2.6",
                                            "driverClassName": "org.postgresql.Driver",\s
                                            "url": "jdbc:postgresql://localhost:5432/postgres",\s
                                            "username": "postgres",\s
                                            "password": "123456",
                                            "tablename": "test.water",
                                            "columns": [
                                                {"originalName":"","showName":"id","columnName":"id","columnLength":255,"columnType":1,"baseType":12,"isPrimaryKey":true,"status":0},
                                                {"originalName":"","showName":"name","columnName":"name","columnLength":255,"columnType":1,"baseType":12,"isPrimaryKey":false,"status":0},
                                                {"originalName":"","showName":"age","columnName":"age","columnLength":255,"columnType":1,"baseType":12,"isPrimaryKey":false,"status":0}
                                            ]
                                        }
                                    }
                                ]
                            }
                """;
    }

}