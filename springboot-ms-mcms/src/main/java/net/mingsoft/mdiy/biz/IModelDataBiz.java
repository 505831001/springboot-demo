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
package net.mingsoft.mdiy.biz;

import net.mingsoft.base.biz.IBaseBiz;

import java.util.List;
import java.util.Map;


/**
 * 自定义模型数据
 * @author 王天培QQ:78750478
 * @version
 * 版本号：100-000-000<br/>
 * 创建日期：2012-03-15<br/>
 * 历史修订：<br/>
 */
public interface IModelDataBiz extends IBaseBiz {


    /**
     * 保存自定义表单的数据
     * @param modelId 模型编号
     * @param params　参数值集合
     */
    boolean saveDiyFormData(int modelId, Map<String, Object> params);

    /**
     * 更新自定义表单的数据
     * @param modelId 模型编号
     * @param params　参数值集合
     */
    boolean updateDiyFormData(int modelId, Map<String, Object> params);


    /**
     * 查询自定义表单的列表数据
     * @param modelId　模型编号
     * @param map wheres查询条件map
     * @return 返回map fileds:字段列表 list:记录集合
     */
    List queryDiyFormData(int modelId, Map<String, Object> map);

    /**
     * 查询自定义表单的对象数据
     * @param modelId　模型编号
     * @param id 主键编号
     * @return 返回表单对象
     */
    Object getFormData(String modelId,String id);

    /**
     * 删除记录
     * @param id　记录编号
     * @param diyFormId 表单编号
     */
    void deleteQueryDiyFormData(int id, String diyFormId);

    /**
     * 查询总数
     * @param modelId 模型编号
     * @return 返回查询总数
     */
    int countDiyFormData(int modelId, Map<String, Object> params);

}
