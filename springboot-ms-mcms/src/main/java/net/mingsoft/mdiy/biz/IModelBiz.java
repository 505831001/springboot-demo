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
import net.mingsoft.mdiy.bean.ModelJsonBean;
import net.mingsoft.mdiy.entity.ModelEntity;

import java.util.List;


/**
 * 自定义表单接口
 * @author 王天培QQ:78750478
 * @version
 * 版本号：100-000-000<br/>
 * 创建日期：2012-03-15<br/>
 * 历史修订：<br/>
 */
public interface IModelBiz extends IBaseBiz<ModelEntity> {

    /**
     * 导入模型，提供自定义配置和自定义表单使用
     * @param customType 自定义类型（表单、配置）
     * @param modelJsonBean 来自代码生成器的自定义模型json转换成的bean
     * @return
     */
    boolean importConfig(String customType, ModelJsonBean modelJsonBean);

    /**
     * 导入模型，提供自定义模型
     * @param customType 自定义类型（模型）
     * @param modelJsonBean 来自代码生成器的自定义模型json转换成的bean
     * @param modelType 自定义模型类型
     * @return
     */
    boolean importModel(String customType, ModelJsonBean modelJsonBean,String modelType);


    /**
     * 更新导入模型，提供自定义配置和自定义表单使用
     * @param modelId 自定义模型编号
     * @param modelJsonBean 来自代码生成器的自定义模型json转换成的bean
     * @return
     */
    boolean updateConfig(String modelId, ModelJsonBean modelJsonBean);

    /**
     * 更新导入模型，提供自定义配置和自定义表单使用
     * @param modelId 自定义模型编号
     * @param modelJsonBean 来自代码生成器的自定义模型json转换成的bean
     * @param modelType 自定义模型类型，导入模型时候下拉选择的业务类型，如：文章类型，只能在内容管理业务使用
     * @return
     */
    boolean updateConfig(String modelId, ModelJsonBean modelJsonBean,String modelType);

    /**
     * 批量删除，并且删除表
     * @param ids
     * @return
     */
    boolean delete (List<String> ids);

}
