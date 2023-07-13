package com.striveonger.study.dp.jdbc.loader;

import com.striveonger.study.core.constant.ResultStatus;
import com.striveonger.study.core.exception.CustomException;
import com.striveonger.study.core.utils.JacksonUtils;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Properties;
import java.util.function.Supplier;

public class ClassLoaderTest {

    @Test
    public void testPathDriverClassLoader() throws Exception {
        // 1. 直接创建数据库连接对象
        String path = "/Users/striveonger/development/repository/org/postgresql/postgresql/42.2.6";
        PathDriverClassLoader pathClassLoader = new PathDriverClassLoader(path);
        System.out.println(pathClassLoader);

        // DriverLoader.class
        Class<?> clazz = pathClassLoader.loadClass(DriverLoader.class.getName());

        Supplier<Connection> supplier = null;

        String driverClassName = "org.postgresql.Driver", url = "jdbc:postgresql://localhost:5432/postgres", username = "postgres", password = "123456";


        Object object = clazz.getDeclaredConstructor().newInstance();
        System.out.println(object);
        Class.forName(driverClassName, true, pathClassLoader);
        Object result = object.getClass().getMethod("getDriver", String.class).invoke(object, driverClassName);
        if (result instanceof Driver driver) {
            Properties info = new Properties();
            info.put("user", username);
            info.put("password", password);
            supplier = () -> {
                try {
                    return driver.connect(url, info);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            };
            extract(supplier);
        }
    }

    @Test
    public void testDataSource() throws Exception {
        // 2. 用连接池来管理创建
        String path = "/Users/striveonger/development/repository/org/postgresql/postgresql/42.2.6";
        PathDriverClassLoader pathClassLoader = new PathDriverClassLoader(path);
        System.out.println(pathClassLoader);

        // DriverLoader.class
        Class<?> clazz = pathClassLoader.loadClass(DriverLoader.class.getName());

        Supplier<Connection> supplier = null;
        DriverLoader.Config config = new DriverLoader.Config();
        config.setDriverClassName("org.postgresql.Driver");
        config.setUrl("jdbc:postgresql://localhost:5432/postgres");
        config.setUsername("postgres");
        config.setPassword("123456");
        String s = JacksonUtils.toJSONString(config);
        // 这里注意, DriverLoader.class 是用默认的ClassLoader加载的, 这样是无法加载指定路径下的Driver类的
        // 要记得用
        Object object = clazz.getDeclaredConstructor().newInstance();
        Object result = object.getClass().getMethod("getDataSource", String.class).invoke(object, s);

        // Object object = clazz.getConstructor(DriverLoader.Config.class).newInstance(config);
        // Object result = object.getClass().getMethod("getDataSource").invoke(object);

        if (result instanceof DataSource ds) {
            supplier = () -> {
                try {
                    return ds.getConnection();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            };
            extract(supplier);
        }
    }

    private void extract(Supplier<Connection> supplier) {
        try (Connection connect = supplier.get(); Statement statement = connect.createStatement()) {
            ResultSet set = statement.executeQuery("SELECT * FROM test.air");
            while (set.next()) {
                String id = set.getString("id");
                String name = set.getString("name");
                System.out.println(id + " --- " + name);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(ResultStatus.ACCIDENT);
        }
    }
}