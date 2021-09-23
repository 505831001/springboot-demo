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

import net.mingsoft.base.constant.e.BaseEnum;

/**
 * 菜单类型枚举
 * @Package net.mingsoft.basic.constant.e
 * @author 李书宇
 * @version
 * 版本号：<br/>
 * 创建日期：@date 2015年10月21日<br/>
 * 历史修订：<br/>
 */
public enum ModelIsMenuEnum implements BaseEnum{
    /**
     * 是否是菜单0非菜单
     */
    MODEL_NOTMENU(0,"否"),

    /**
     * 是否是菜单1菜单
     */
    MODEL_MEUN(1,"是");

    private String code;

    private int id;

    /**
     * 构造方法
     * @param id 默认ID
     * @param code 传入的枚举类型
     */
    ModelIsMenuEnum(int id,String code) {
        this.code = code;
        this.id = id;
    }

    @Override
    public int toInt() {
        return this.id;
    }

    @Override
    public String toString() {
        return this.code.toString();
    }

}
