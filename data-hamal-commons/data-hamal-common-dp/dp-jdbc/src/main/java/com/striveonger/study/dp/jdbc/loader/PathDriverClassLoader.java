package com.striveonger.study.dp.jdbc.loader;

import cn.hutool.core.io.IoUtil;
import com.striveonger.study.core.constant.ResultStatus;
import com.striveonger.study.core.exception.CustomException;
import com.striveonger.study.core.utils.FileUtil;
import com.striveonger.study.core.utils.JacksonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;

/**
 * @author Mr.Lee
 * @description:
 * @date 2023-06-17 18:06
 */
public class PathDriverClassLoader extends URLClassLoader {

    private final Logger log = LoggerFactory.getLogger(PathDriverClassLoader.class);

    private static final String CLASS_FILE_SUFFIX = ".class";

    /**
     * 加载路径
     */
    private final File path;

    /**
     * 要强制加载路径之外的类名
     */
    private final Set<String> extraForceLoads = new HashSet<>();

    public PathDriverClassLoader(String path) {
        this(new File(path), null);
    }

    private PathDriverClassLoader(File path, ClassLoader parent) {
        super(fullFilesToURL(path).toArray(URL[]::new), parent);
        this.path = path;
        this.extraForceLoads.add(DriverLoader.class.getName());
        this.extraForceLoads.add(DriverLoader.Config.class.getName());
        this.extraForceLoads.add(CustomException.class.getName());
        this.extraForceLoads.add(ResultStatus.class.getName());
    }

    public File getPath() {
        return path;
    }

    public void setOutside(Set<String> outside) {
        this.extraForceLoads.addAll(outside);
    }

    @Override
    public Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        // JDK标准库应由父类加载
        if (isJDKStandardClassName(name)) {
            return Class.forName(name, resolve, getParentClassLoader());
        }

        // check local cache...
        Class<?> clazz = findLoadedClass(name);

        // 尝试加载路径下的 Class (破坏双亲委派, 加载顺序而已...)
        if (clazz == null) {
            try {
                clazz = findClass(name);
            } catch (ClassNotFoundException e) {
                // log.warn("Path [{}] not found [{}]", path, name);
            }
        }

        // 尝试强制加载扩展的 Class
        if (clazz == null && isExtra(name)) {
            InputStream in = getParentClassLoader().getResourceAsStream(toPath(name));
            if (in == null) {
                throw new ClassNotFoundException(name);
            }
            byte[] bytes = IoUtil.readBytes(in, true);
            clazz = defineClass(name, bytes, 0, bytes.length);
        }

        // 代理父类加载JDK扩展库
        if (clazz == null && isJDKExtClassName(name)) {
            ClassLoader parent = getParentClassLoader();
            clazz = Class.forName(name, false, parent);
        }

        if (clazz == null) {
            throw new ClassNotFoundException(name);
        }

        if (resolve) {
            resolveClass(clazz);
        }

        return clazz;
    }

    /**
     * 获取上级类加载器
     */
    private ClassLoader getParentClassLoader() {
        ClassLoader parent = getParent();
        if (parent == null)
            parent = PathDriverClassLoader.class.getClassLoader();

        return parent;
    }

    private static List<URL> fullFilesToURL(File path) {
        List<URL> result = new ArrayList<>();
        if (path.isDirectory()) {
            File[] children = Optional.ofNullable(path.listFiles()).orElse(new File[]{});
            for (File child : children) {
                if (child.isDirectory()) {
                    List<URL> list = fullFilesToURL(child);
                    result.addAll(list);
                } else {
                    result.add(FileUtil.toURL(child));
                }
            }
        } else {
            result.add(FileUtil.toURL(path));
        }
        return result;
    }


    /**
     * 判断是否需要扩展加载
     */
    private boolean isExtra(String name) {
        return this.extraForceLoads.contains(name)
            || isHikariPoolClassName(name)
            || isSlf4jClassName(name)
            || isJacksonClassName(name);
    }

    /**
     * 是否JDK标准类名
     * @param name 全类名
     * @return
     */
    private boolean isJDKStandardClassName(String name) {
        return name.startsWith("java.")
            || name.startsWith("javax.");
    }

    /**
     * 是否JDK扩展类名
     *
     * @param name 全类名
     * @return
     */
    private boolean isJDKExtClassName(String name) {
        return name.startsWith("sun.")
            || name.startsWith("org.ietf.")
            || name.startsWith("org.omg.")
            || name.startsWith("org.w3c.")
            || name.startsWith("org.xml.")
            || name.startsWith("jdk.");
    }

    /**
     * 是否HikariPool连接池类名
     * @param name
     * @return
     */
    private boolean isHikariPoolClassName(String name) {
        return name.startsWith("com.zaxxer.hikari.")
            || name.startsWith("org.slf4j.");
    }

    /**
     * 是否日志框架类名
     * @param name
     * @return
     */
    private boolean isSlf4jClassName(String name) {
        return name.startsWith("ch.qos.logback.")
            || name.startsWith("org.slf4j.");
    }

    /**
     * 是否日志框架类名
     * @param name
     * @return
     */
    private boolean isJacksonClassName(String name) {
        return name.startsWith("com.fasterxml.jackson.")
            || name.startsWith("com.striveonger.study.core.utils.JacksonUtils");
    }

    /**
     * 全类名 转 Class文件路径
     * @param name 全类名
     * @return
     */
    private String toPath(String name) {
        return name.replace('.', '/') + CLASS_FILE_SUFFIX;
    }
}
