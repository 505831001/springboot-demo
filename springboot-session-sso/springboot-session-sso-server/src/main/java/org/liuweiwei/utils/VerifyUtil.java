package org.liuweiwei.utils;

import java.io.Serializable;

/**
 * @author Liuweiwei
 * @since 2021-08-28
 */
public class VerifyUtil implements Serializable {
    /**
     * 字符串判空
     * @param str 字符串
     * @return 不为空返回true
     */
    public static boolean isNotEmpty(String str) {
        return (null != str && !str.equals(""));
    }

    /**
     * 字符串判空
     * @param args 多个字符串
     * @return 全部不为空返回true
     */
    public static boolean isNotEmpty(String... args) {
        for (String str : args) {
            if (null == str || str.equals("")) {
                return false;
            }
        }
        return true;
    }

    /**
     * 检查字符串是否为null
     * @param arg 多个字符串
     * @return 全部不为null返回true
     */
    public static boolean checkNull(String... arg) {
        for (String str : arg) {
            if (!checkNull(str)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 对象判空
     * @param object 对象
     * @return 不为空返回true
     */
    public static boolean checkNull(Object object) {
        return null != object;
    }

    public static <T> T cast(Object object) {
        return (T) object;
    }
}
