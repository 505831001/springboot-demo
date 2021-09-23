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
package net.mingsoft.base.filter;

import java.math.BigDecimal;

import com.alibaba.fastjson.serializer.ValueFilter;

/**
 *
 * @ClassName:  DoubleValueFilter
 * @Description:TODO(铭飞ms平台－doublue小数位置过滤)
 * @author: 铭飞开发团队
 * @date:   2018年3月19日 下午3:43:40
 *
 * @Copyright: 2018 www.mingsoft.net Inc. All rights reserved.
 */
public class DoubleValueFilter implements ValueFilter {

    private int digits=2;

    public DoubleValueFilter(){}

    /**
     *  指定保留小数个数
     * @param digits 默认2位
     */
    public DoubleValueFilter(int digits){
        this.digits = digits;
    }

    @Override
    public Object process(Object object, String name, Object value) {
        // TODO Auto-generated method stub
        if (value instanceof BigDecimal || value instanceof Double || value instanceof Float) {
            return new BigDecimal(value.toString()).setScale(digits, BigDecimal.ROUND_HALF_UP);
        }
        return value;
    }

}

