/**
 * The MIT License (MIT)
 * Copyright (c) 2021 铭软科技(mingsoft.net)
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:

 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.

 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package net.mingsoft.basic.util;

import cn.hutool.core.util.ArrayUtil;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.sql.NClob;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @ClassName: StringUtil
 * @Description: TODO(字符串工具类)
 * @author 铭软开发团队
 * @date 2020年7月2日
 *
 */
public class StringUtil {
    /**
     * 验证长度
     *
     * @param str 需验证的字符串
     * @param minLength 字符串的最小长度
     * @param maxLength 字符串的最大长度
     * @return 如果验证通过，则返回true，否则返回false
     */
    public static boolean checkLength(String str, int minLength, int maxLength) {
        if (str != null) {
            int len = str.length();
            if (minLength == 0)
                return len <= maxLength;
            else if (maxLength == 0)
                return len >= minLength;
            else
                return (len >= minLength && len <= maxLength);
        }
        return false;
    }
    /**
     * 判断数字数组
     *
     * @param str String数组
     * @return 如果是数字，则返回true，否则返回false
     */
    public static boolean isIntegers(String str[]) {
        try {
            for (int i = 0; i < str.length; i++)
                Integer.parseInt(str[i]);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    /**
     * 字符串转数字型数组
     *
     * @param str String字符串
     * @return 返回数字型数组
     */
    public static Integer[] stringsToIntegers(String str[]) {
        Integer array[] = new Integer[str.length];
        for (int i = 0; i < array.length; i++)
            array[i] = Integer.parseInt(str[i]);
        return array;
    }
    /**
     * 字符串转数字型数组
     *
     * @param str String字符串
     * @return 返回数字型数组
     */
    public static int[] stringsToInts(String str[]) {
        int array[] = new int[str.length];
        for (int i = 0; i < array.length; i++)
            array[i] = Integer.parseInt(str[i]);
        return array;
    }

    /**
     * 程序内部字符串转码，将ISO-8859-1转换成utf-8
     *
     * @param str 需要转码的字符串
     * @return 返回utf8编码字符串
     */
    public static String isoToUTF8(String str) {
        if (StringUtils.isEmpty(str))
            return "";
        try {
            return new String(str.getBytes("ISO-8859-1"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
        }
        return "";
    }

    /**
     * 字符串转double型数组
     *
     * @param str String字符串
     * @return 返回数字型数组
     */
    public static double[] stringsToDoubles(String str[]) {
        double array[] = new double[str.length];
        for (int i = 0; i < array.length; i++)
            array[i] = Double.parseDouble(str[i]);
        return array;
    }
    /**
     * 判断数字数组
     *
     * @param str String数组
     * @return 如果是数组，则返回true，否则返回false
     */
    public static boolean isDoubles(String str[]) {
        try {
            for (int i = 0; i < str.length; i++)
                Double.parseDouble(str[i]);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    /**
     * 获取时间戳
     *
     * @return 返回获取当前系统时间字符串
     */
    public static String getDateSimpleStr() {
        return String.valueOf(System.currentTimeMillis());
    }
    /**
     * 生成随机数
     * 过期，推荐使用hutool RandomUtil.randomInt
     *
     * @param len 随机数长度
     * @return 返回随机数
     */
    @Deprecated
    public static String randomNumber(int len) {
        StringBuffer sb = new StringBuffer();
        Random random = new Random();
        for (int i = 0; i < len; i++) {
            sb.append(Math.abs(random.nextInt()) % 10);
        }
        return sb.toString();
    }
    /**
     * 变量形态转换 int型转为String型
     *
     * @param comment 整型数字
     * @return 返回字符串
     */
    @Deprecated
    public static String int2String(int comment) {
        String srt = "";
        srt = Integer.toString(comment);
        return srt;
    }
    /**
     * 除去字符窜中重复的字符
     *
     * @param content
     *            　原始内容
     * @param target
     *            　重复内容
     * @return　返回除去后的字符串
     */
    public static String removeRepeatStr(String content, String target) {
        StringBuffer sb = new StringBuffer(content);
        for (int i = 0; i < sb.length()-1; i++) {

            if (sb.substring(i, i + target.length()).equals(target) && sb.substring(i, i + target.length()).equals(sb.substring(i + 1, i + target.length() + 1))) {
                sb.delete(i, i + target.length());
                if (i + target.length() + 1 > sb.length()) {
                    break;
                } else {
                    i--;
                }
            }

        }
        return sb.toString();
    }
    /**
     * 降序排序
     * @param str
     * @param delimiter
     * 			分隔符
     * @return
     */
    public static String sort(String str,String delimiter){
        String[] articleTypeArrays = str.split(delimiter);
        Arrays.sort(articleTypeArrays);
        return ArrayUtil.join(articleTypeArrays, delimiter);
    }

    /**
     * 验证手机号码
     *
     * @param phoneNumber 手机号码
     * @return 如果是手机号，则返回true，否则返回false
     */
    public static boolean isMobile(String phoneNumber) {
        phoneNumber = phoneNumber.trim();
        String pattern = "^[1][1-9][0-9]{9}";
        return phoneNumber.matches(pattern);
    }


    /**
     * 验证邮箱格式
     *
     * @param email
     *            邮箱
     * @return 如果是邮箱，则返回true，否则返回false
     */
    public static Boolean isEmail(String email) {
        boolean tag = true;
        final String pattern1 = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        final Pattern pattern = Pattern.compile(pattern1);
        final Matcher mat = pattern.matcher(email);
        if (!mat.find()) {
            tag = false;
        }
        return tag;
    }

    /**
     * 判断字符串是否是数字类型
     *
     * @param str 字符串
     * @return 如果是数字类型，则返回true，否则返回false
     */
    public static boolean isInteger(Object str) {
        if (StringUtil.isBlank(str))
            return false;
        try {
            Integer.parseInt(str.toString());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 组织path路径, 例如:buildPath(a,b,c); 返回:a/b/c
     * @param params 所有对象
     * @return 返回新的路径地址
     */
    public static String buildPath(Object... params) {
        String temp = "";
        for(Object o:params) {
            temp+=File.separator+o;
        }
        return temp;
    }


    /**
     * 组织url 的get请求地址
     *
     * @param url
     *            原地址
     * @param parm
     *            参数 推荐格式:参数=值
     * @return 饭胡新的地址
     */
    public static String buildUrl(String url, String parm) {
        if (url.indexOf("?") > 0) {
            return url += "&" + parm;
        } else {
            return url += "?" + parm;
        }
    }

    /**
     * 组织url 的get请求地址
     *
     * @param url
     *            原地址
     * @param parms
     *            参数集合 格式:key参数=值value
     * @return 返回新的地址
     */
    @SuppressWarnings("rawtypes")
    public static String buildUrl(String url, Map parms) {
        Iterator key = parms.keySet().iterator();
        String paramsStr = "";
        while (key.hasNext()) {
            Object temp = key.next();
            if (isBlank(parms.get(temp))) {
                continue;
            }
            if (paramsStr != "") {
                paramsStr += "&";
            }
            paramsStr += (temp + "=" + parms.get(temp));
        }

        if (paramsStr != "") {
            if (url.indexOf("?") > 0) {
                return url += "&" + paramsStr;
            } else {
                return url += "?" + paramsStr;
            }
        }
        return url;
    }

    /**
     * 读取文件后缀名称
     *
     * @param filePath
     *            文件路径 格式如:/../a.txt
     * @return 返回文件后缀名
     */
    public static String getFileFix(String filePath) {
        String temp = "";
        if (filePath != null) {
            temp = filePath.substring(filePath.indexOf("."), filePath.length());
        }
        return temp;
    }

    /**
     * 字段串是否为空
     * 推荐使用org.apache.commons.lang3.StringUtils 中的isEmpty或isBlank
     *
     * @param str 要判断是否为空的字符串
     * @return 如果为空，则返回true，否则返回false
     */
    @Deprecated
    public static boolean isBlank(Object obj) {
        return (obj == null || obj.toString().trim().equals("") || obj.toString().length() < 0);
    }



    public static String nclobStr(NClob nclob){
        if (nclob == null) {
            return null;
        }
        String content = "";
        try {
            Reader is = nclob.getCharacterStream();
            BufferedReader buff = new BufferedReader(is);// 得到流
            String line = buff.readLine();
            StringBuffer sb = new StringBuffer();
            while (line != null) {// 执行循环将字符串全部取出付值给StringBuffer由StringBuffer转成STRING
                sb.append(line);
                line = buff.readLine();
            }
            content = sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content;
    }


}
