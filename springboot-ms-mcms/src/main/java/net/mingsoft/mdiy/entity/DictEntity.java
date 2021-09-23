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
package net.mingsoft.mdiy.entity;


import net.mingsoft.base.entity.BaseEntity;

/**
 * 字典表实体
 * @author 铭飞开发团队
 * @version
 * 版本号：1<br/>
 * 创建日期：2017-8-12 14:22:36<br/>
 * 历史修订：<br/>
 */
public class DictEntity extends BaseEntity {

    private static final long serialVersionUID = 1502518956351L;

    /**
     * 数据值
     */
    private String dictValue;
    /**
     * 标签名
     */
    private String dictLabel;
    /**
     * 类型
     */
    private String dictType;
    /**
     * 描述
     */
    private String dictDescription;
    /**
     * 排序（升序）
     */
    private Integer dictSort;
    /**
     * 子业务关联
     */
    private String isChild;
    /**
     * 启用状态
     */
    private Boolean dictEnable;
    /**
     * 备注信息
     */
    private String dictRemarks;


    /**
     * 设置数据值
     */
    public void setDictValue(String dictValue) {
        this.dictValue = dictValue;
    }

    /**
     * 获取数据值
     */
    public String getDictValue() {
        return this.dictValue;
    }

    /**
     * 设置标签名
     */
    public void setDictLabel(String dictLabel) {
        this.dictLabel = dictLabel;
    }

    /**
     * 获取标签名
     */
    public String getDictLabel() {
        return this.dictLabel;
    }

    /**
     * 设置类型
     */
    public void setDictType(String dictType) {
        this.dictType = dictType;
    }

    /**
     * 获取类型
     */
    public String getDictType() {
        return this.dictType;
    }

    /**
     * 设置描述
     */
    public void setDictDescription(String dictDescription) {
        this.dictDescription = dictDescription;
    }

    /**
     * 获取描述
     */
    public String getDictDescription() {
        return this.dictDescription;
    }

    /**
     * 设置排序（升序）
     */
    public void setDictSort(Integer dictSort) {
        this.dictSort = dictSort;
    }

    /**
     * 获取排序（升序）
     */
    public Integer getDictSort() {
        return this.dictSort;
    }


    /**
     * 设置备注信息
     */
    public void setDictRemarks(String dictRemarks) {
        this.dictRemarks = dictRemarks;
    }

    /**
     * 获取备注信息
     */
    public String getDictRemarks() {
        return this.dictRemarks;
    }
    /**
     * 获取字典业务
     */
    public String getIsChild() {
        return isChild;
    }
    /**
     * 设置字典业务
     */
    public void setIsChild(String isChild) {
        this.isChild = isChild;
    }
    /**
     * 设置启用状态
     */
    public void setDictEnable(Boolean dictEnable) {
        this.dictEnable = dictEnable;
    }
    /**
     * 获取启用状态
     */
    public Boolean getDictEnable() {
        return this.dictEnable;
    }
}
