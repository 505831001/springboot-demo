/**
 * The MIT License (MIT) * Copyright (c) 2020 铭软科技(mingsoft.net)
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package net.mingsoft.mdiy.util;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import net.mingsoft.basic.util.BasicUtil;
import net.mingsoft.basic.util.SpringUtil;
import net.mingsoft.mdiy.biz.IDictBiz;
import net.mingsoft.mdiy.entity.DictEntity;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 字典工具类
 * @author 铭飞开源团队
 */
public class DictUtil {

    /**
     * 根据字典类型获取列表
     * @param dictType 字典类型
     * @return 字典集合
     */
    public static List<DictEntity> list(String dictType) {
        DictEntity dict = new DictEntity();
        dict.setDictType(dictType);
        return (List<DictEntity>) SpringUtil.getBean(IDictBiz.class).query(dict);
    }


    /**根据字典类型与字典值获取标签名
     * @param dictType 字典类型
     * @param dictValue 数据值
     * @param defaultValue 默认值
     * @return
     */
    public static String getDictLabel(String dictType, String dictValue, String defaultValue) {
        if (StringUtils.isNotBlank(dictType) && StringUtils.isNotBlank(dictValue)) {
            DictEntity dictEntity = get(dictType, null, dictValue);
            if (ObjectUtil.isNotNull(dictEntity)) {
                return dictEntity.getDictLabel();
            }
        }
        return defaultValue;
    }

    /**
     * 根据字典类型与标签名获取字典值
     * @param dictType 字典类型
     * @param dictLabel 标签名
     * @param defaultValue 默认值
     * @return
     */
    public static String getDictValue(String dictType, String dictLabel, String defaultValue) {
        if (StringUtils.isNotBlank(dictType) && StringUtils.isNotBlank(dictLabel)) {
            DictEntity dictEntity = get(dictType, dictLabel, null);
            if (ObjectUtil.isNotNull(dictEntity)) {
                return dictEntity.getDictValue();
            }
        }
        return defaultValue;
    }

    /**
     * 获取字典实体
     * @param dictLabel 标签名
     * @param dictType 类型
     * @param dictValue 数据值
     * @return DictEntity 字典实体
     */
    public static DictEntity get(String dictType, String dictLabel, String dictValue) {
        DictEntity dict = new DictEntity();
        dict.setDictLabel(dictLabel);
        dict.setDictType(dictType);
        dict.setDictValue(dictValue);
        return (DictEntity) SpringUtil.getBean(IDictBiz.class).getEntity(dict);
    }


    /**
     * 根据字典类型与标签名获取字典值
     * @param dictType 字典类型
     * @param dictLabel 数据值
     * @return
     */
    public static String getDictValue(String dictType, String dictLabel) {
        return getDictValue(dictType, dictLabel, "");
    }


    /**
     * 根据字典类型与字典值获取标签名
     * @param dictType 字典类型
     * @param dictValue 数据值
     * @return
     */
    public static String getDictLabel(String dictType, String dictValue) {
        return getDictLabel(dictType, dictValue, "");
    }

    /**
     * 根据字典类型与逗号分隔的字典值获取逗号分隔的字典名
     * @param dictType 字典类型
     * @param dictValues  逗号分隔的字典值
     * @param defaultValue 默认值
     * @return
     */
    public static String getDictLabels(String dictType, String dictValues, String defaultValue) {
        if (StringUtils.isNotBlank(dictType) && StringUtils.isNotBlank(dictValues)) {
            List labels = new ArrayList();
            String[] values = dictValues.split(",");
            for (int i = 0; i < values.length; i++) {
                String value = values[i];
                String dictLabel = getDictLabel(dictType, value, defaultValue);
                if (!StringUtils.isBlank(dictLabel)) {
                    labels.add(dictLabel);
                }
            }
            return StringUtils.join(labels, ",");
        } else {
            return defaultValue;
        }
    }

    /**
     * 根据字典类型与逗号分隔的字典值获取逗号分隔的字典名
     * @param dictType 字典类型
     * @param dictValues 逗号分隔的字典值
     * @return
     */
    public static String getDictLabels(String dictType, String dictValues) {

        return getDictLabels(dictType, dictValues, "");
    }

    /**
     * 根据字典类型与逗号分隔的字典名获取逗号分隔的字典值
     * @param dictType 字典类型
     * @param dictLabels 逗号分隔的字典名
     * @param defaultValue 默认值
     * @return 逗号分隔的字典值
     */
    public static String getDictValues(String dictType, String dictLabels, String defaultValue) {
        if (StringUtils.isNotBlank(dictType) && StringUtils.isNotBlank(dictLabels)) {
            List values = new ArrayList();
            String[] labels = dictLabels.split(",");
            for (int i = 0; i < labels.length; i++) {
                String value = labels[i];
                values.add(getDictValue(dictType, value, defaultValue));
            }
            return StringUtils.join(values, ",");
        } else {
            return defaultValue;
        }
    }

    /**
     * 根据字典类型与逗号分隔的字典名获取逗号分隔的字典值
     * @param dictType 字典类型
     * @param dictLabels 逗号分隔的字典名
     * @return 逗号分隔字典值
     */
    public static String getDictValues(String dictType, String dictLabels) {
        return getDictValues(dictType, dictLabels, "");
    }
}
