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
package net.mingsoft.mdiy.biz;


import net.mingsoft.base.biz.IBaseBiz;
import net.mingsoft.mdiy.entity.DictEntity;

import java.util.List;

/**
 * 字典表业务
 * @author 铭飞开发团队
 * @version
 * 版本号：1.0.0<br/>
 * 创建日期：2016-9-8 17:11:19<br/>
 * 历史修订：<br/>
 */
public interface IDictBiz extends IBaseBiz {
    /**
     *
     * 根据字典类型和标签名获取实体
     * @param dictType 类型
     * @param dictLabel 标签名
     * @return DictEntity 字典实体
     */
    public DictEntity getByTypeAndLabelAndValue(String dictType, String dictLabel , String dictValue);

    /**
     * 获取所有字典类型
     * 根据子数据类型获取所有字典类型
     * @param dictEntity
     * @return
     */
    List<DictEntity> dictType(DictEntity dictEntity);

    /**
     * 使用站群排除appId拼接问题
     * @param dictEntity
     * @return
     */
    List<DictEntity> queryExcludeApp(DictEntity dictEntity);
}
