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

import com.baomidou.mybatisplus.annotation.TableName;
import net.mingsoft.base.entity.BaseEntity;

/**
 * 标签实体
 * @author 铭飞开发团队
 * 创建日期：2018-10-24 8:44:34<br/>
 * 历史修订：2021-05-02 合并tagsql<br/>
 */
@TableName("mdiy_tag")
public class TagEntity extends BaseEntity {

    private static final long serialVersionUID = 1540341874663L;

    /**
     * 标签名称
     */
    private String tagName;
    /**
     * 标签类型
     */
    private String tagType;

    /**
     * 描述
     */
    private String tagSql;

    private String tagClass;
    /**
     * 描述
     */
    private String tagDescription;



    /**
     * 设置标签名称
     */
    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    /**
     * 获取标签名称
     */
    public String getTagName() {
        return this.tagName;
    }
    /**
     * 设置标签类型
     */
    public void setTagType(String tagType) {
        this.tagType = tagType;
    }

    /**
     * 获取标签类型
     */
    public String getTagType() {
        return this.tagType;
    }
    /**
     * 设置描述
     */
    public void setTagDescription(String tagDescription) {
        this.tagDescription = tagDescription;
    }

    /**
     * 获取描述
     */
    public String getTagDescription() {
        return this.tagDescription;
    }

    public String getTagSql() {
        return tagSql;
    }

    public void setTagSql(String tagSql) {
        this.tagSql = tagSql;
    }

    public String getTagClass() {
        return tagClass;
    }

    public void setTagClass(String tagClass) {
        this.tagClass = tagClass;
    }
}
