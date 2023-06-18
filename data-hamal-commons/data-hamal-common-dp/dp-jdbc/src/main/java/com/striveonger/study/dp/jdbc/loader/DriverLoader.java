package com.striveonger.study.dp.jdbc.loader;

import com.striveonger.study.core.constant.ResultStatus;
import com.striveonger.study.core.exception.CustomException;
import com.striveonger.study.core.utils.JacksonUtils;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.sql.Driver;
import java.sql.DriverManager;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Mr.Lee
 * @description:
 * @date 2023-06-18 11:36
 */
public class DriverLoader {

    public DriverLoader() { }


    public DataSource getDataSource(String s) {
        Config config = JacksonUtils.toObject(s, Config.class);
        if (config == null) throw new CustomException(ResultStatus.ACCIDENT, "String 's' parse DriverLoader.Config failure...");
        HikariDataSource ds = new HikariDataSource();
        ds.setDriverClassName(config.getDriverClassName());
        ds.setJdbcUrl(config.getUrl());
        ds.setUsername(config.getUsername());
        ds.setPassword(config.getPassword());

        // 添加配置项
        ds.setConnectionTimeout(config.getConnectionTimeout());
        ds.setMinimumIdle(config.getMinimumIdle());
        ds.setMaximumPoolSize(config.getMaximumPoolSize());
        ds.setIdleTimeout(config.getIdleTimeout());
        ds.setMaxLifetime(config.getMaxLifetime());
        return ds;
    }

    /**
     * 获取指定名称的已注册{@linkplain Driver}。
     * <p>
     * 如果没有，此方法将返回{@code null}。
     * </p>
     *
     * @param name Driver 全类名
     * @return
     */
    public Driver getDriver(String name) {
        Set<Driver> drivers = getDrivers();
        for (Driver driver : drivers) {
            if (isAssignableClass(driver.getClass(), name)) {
                return driver;
            }
        }
        return null;
    }

    public Set<Driver> getDrivers() {
        Set<Driver> result = new HashSet<>();
        ClassLoader loader = this.getClass().getClassLoader();
        Enumeration<Driver> drivers = DriverManager.getDrivers();
        while (drivers.hasMoreElements()) {
            Driver driver = drivers.nextElement();
            ClassLoader cl = driver.getClass().getClassLoader();
            if (cl == loader) {
                result.add(driver);
            }
        }
        return result;
    }


    /**
     * Class 向上查找 继承和接口, 检查是否包含name
     * @param clazz
     * @param name
     * @return
     */
    protected boolean isAssignableClass(Class<?> clazz, String name) {
        if (clazz == null) return false;

        if (clazz.getName().equals(name)) return true;

        if (isAssignableClass(clazz.getSuperclass(), name)) return true;

        Class<?>[] interfaces = clazz.getInterfaces();
        for (Class<?> in : interfaces) {
            if (isAssignableClass(in, name)) {
                return true;
            }
        }
        return false;
    }


    public static class Config {
        private String driverClassName, url, username, password;

        private int connectionTimeout = 30000, minimumIdle = 1, maximumPoolSize = 10, idleTimeout = 300000, maxLifetime = 300000;

        public String getDriverClassName() {
            return driverClassName;
        }

        public void setDriverClassName(String driverClassName) {
            this.driverClassName = driverClassName;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public int getConnectionTimeout() {
            return connectionTimeout;
        }

        public void setConnectionTimeout(int connectionTimeout) {
            this.connectionTimeout = connectionTimeout;
        }

        public int getMinimumIdle() {
            return minimumIdle;
        }

        public void setMinimumIdle(int minimumIdle) {
            this.minimumIdle = minimumIdle;
        }

        public int getMaximumPoolSize() {
            return maximumPoolSize;
        }

        public void setMaximumPoolSize(int maximumPoolSize) {
            this.maximumPoolSize = maximumPoolSize;
        }

        public int getIdleTimeout() {
            return idleTimeout;
        }

        public void setIdleTimeout(int idleTimeout) {
            this.idleTimeout = idleTimeout;
        }

        public int getMaxLifetime() {
            return maxLifetime;
        }

        public void setMaxLifetime(int maxLifetime) {
            this.maxLifetime = maxLifetime;
        }
    }

}
