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


package net.mingsoft.basic.constant.e;

import net.mingsoft.base.constant.e.BaseSessionEnum;

/**
 * session枚举
 *
 * @author ms dev group
 * @version 版本号：100-000-000<br/>
 *          创建日期：2012-03-15<br/>
 *          历史修订：<br/>
 */
public enum SessionConstEnum implements BaseSessionEnum {


    /**
     * 普通管理员的sesison
     */
    MANAGER_SESSION("manager_session"),

    /**
     * 验证码session
     */
    CODE_SESSION("rand_code"),
    ;

    /**
     * 设置session常量
     *
     * @param attr
     *            常量
     */
    SessionConstEnum(String attr) {
        this.attr = attr;
    }

    private String attr;

    /**
     * 返回SessionConst常量的字符串表示
     *
     * @return 字符串
     */
    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return attr;
    }

}
