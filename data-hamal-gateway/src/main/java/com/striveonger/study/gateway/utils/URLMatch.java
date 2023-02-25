package com.striveonger.study.gateway.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import org.springframework.util.AntPathMatcher;

import java.util.Collection;

/**
 * @author Mr.Lee
 * @description:
 * @date 2023-02-23 15:55
 */
public class URLMatch {

    private final static AntPathMatcher matcher = new AntPathMatcher();

    /**
     * 验证用户要访问的URL, 是否在白名单中
     * @param visitURL 要访问的URL
     * @param whites 白名单
     * @return
     */
    public static boolean match(String visitURL, Collection<String> whites) {
        if (StrUtil.isBlank(visitURL) || CollUtil.isEmpty(whites)) return false;
        for (String white : whites) {
            if (match(visitURL, white)) return true;
        }
        return false;
    }

    /**
     * 验证用户要访问的URL, 是否与白名单相匹配
     * @param visitURL 要访问的URL
     * @param white 白名单
     * @return
     */
    public static boolean match(String visitURL, String white) {
        return matcher.match(white, visitURL);
    }

}
