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

import cn.hutool.crypto.SecureUtil;
import net.mingsoft.basic.util.BasicUtil;
import org.apache.commons.lang3.StringUtils;

/**
 * 自定义表单表实体
 * @author 蓝精灵
 * @version
 * 版本号：1<br/>
 * 创建日期：2017-8-12 15:58:29<br/>
 * 历史修订：<br/>
 */
public class PostEntity extends ModelEntity {

    private static final long serialVersionUID = 1502524709328L;
    /**
     * 表单的访问地址
     */
    private String formUrl;

    public String getFormUrl() {
        if(StringUtils.isNotBlank(this.id)){
            formUrl=SecureUtil.aes(SecureUtil.md5(BasicUtil.getApp().getAppId() +"").substring(16).getBytes()).encryptHex(this.id);
        }
        return formUrl;
    }

    public void setFormUrl(String formUrl) {
        this.formUrl = formUrl;
    }

}
