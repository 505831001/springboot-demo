/**
 * The MIT License (MIT) * Copyright (c) 2020 铭软科技(mingsoft.net)

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
package net.mingsoft.base.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 读取Properties综合类,默认绑定到classpath下的config.properties文件。
 */
public class PropertiesUtil {

    /**
     * 读取资源文件键值
     * @param properties  属性文件包路径 如：net/mingsoft/resources/a.properties
     * @param key 键
     * @return
     * @throws IOException
     */
    public static String get(String properties, String key) throws IOException {
        InputStream in = PropertiesUtil.class.getClassLoader().getResourceAsStream(properties);
        Properties props = new Properties();
        props.load(in);
        String value = props.getProperty(key);
        // 关闭资源
        in.close();
        return value;
    }

    /**
     * 读取资源文件所有信息
     * @param properties  属性文件包路径 如：net/mingsoft/resources/a.properties
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static Map<String, String> getMap(String properties) throws FileNotFoundException, IOException {
        // 保存所有的键值
        Map<String, String> map = new HashMap<String, String>();
        InputStream in = PropertiesUtil.class.getClassLoader().getResourceAsStream(properties);
        Properties props = new Properties();
        props.load(in);
        Enumeration en = props.propertyNames();
        while (en.hasMoreElements()) {
            String key = (String) en.nextElement();
            String Property = props.getProperty(key);
            map.put(key, Property);
        }
        in.close();
        return map;
    }

    /**
     * 设置属性值
     *
     * @param properties
     *            属性文件包路径 如：net/mingsoft/resources/a.properties
     * @param key
     *            键
     * @param value
     *            值
     * @throws IOException
     */
    public void setValue(String properties, String key, String value) throws IOException {
        Properties prop = new Properties();
        InputStream fis = new FileInputStream(PropertiesUtil.class.getClassLoader().getResource(properties).getPath());
        // 从输入流中读取属性列表（键和元素对）
        prop.load(fis);
        // 调用 Hashtable 的方法 put。使用 getProperty 方法提供并行性。
        // 强制要求为属性的键和值使用字符串。返回值是 Hashtable 调用 put 的结果。
        OutputStream fos = new FileOutputStream(
                PropertiesUtil.class.getClassLoader().getResource(properties).getPath());
        prop.setProperty(key, value);
        // 以适合使用 load 方法加载到 Properties 表中的格式，
        // 将此 Properties 表中的属性列表（键和元素对）写入输出流
        prop.store(fos, "last update");
        // 关闭文件
        fis.close();
        fos.close();
    }

}
