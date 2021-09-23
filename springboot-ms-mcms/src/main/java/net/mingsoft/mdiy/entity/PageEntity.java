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

import com.baomidou.mybatisplus.annotation.TableField;
import net.mingsoft.base.entity.BaseEntity;

/**
 * 自定义页面表实体
 * @author 蓝精灵
 * @version
 * 版本号：1<br/>
 * 创建日期：2017-8-11 14:01:54<br/>
 * 历史修订：<br/>
 */
public class PageEntity extends BaseEntity {

    private static final long serialVersionUID = 1502431314331L;

    /**
     * 自定义页面绑定模板的路径
     */
    private String pagePath;
    /**
     * 自定义页面标题
     */
    private String pageTitle;
    /**
     * 自定义页面访问路径
     */
    private String pageKey;
    /**
     * 页面类型
     */
    private String pageType;

    /**
     * 设置自定义页面绑定模板的路径
     */
    public void setPagePath(String pagePath) {
        this.pagePath = pagePath;
    }

    /**
     * 获取自定义页面绑定模板的路径
     */
    public String getPagePath() {
        return this.pagePath;
    }

    /**
     * 设置自定义页面标题
     */
    public void setPageTitle(String pageTitle) {
        this.pageTitle = pageTitle;
    }

    /**
     * 获取自定义页面标题
     */
    public String getPageTitle() {
        return this.pageTitle;
    }

    /**
     * 设置自定义页面访问路径
     */
    public void setPageKey(String pageKey) {
        this.pageKey = pageKey;
    }

    /**
     * 获取自定义页面访问路径
     */
    public String getPageKey() {
        return this.pageKey;
    }
    /**
     * 设置页面类型
     */
    public void setPageType(String pageType) {
        this.pageType = pageType;
    }

    /**
     * 获取页面类型
     */
    public String getPageType() {
        return this.pageType;
    }

}
