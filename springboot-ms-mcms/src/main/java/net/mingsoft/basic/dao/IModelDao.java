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


package net.mingsoft.basic.dao;

import net.mingsoft.base.dao.IBaseDao;
import net.mingsoft.base.entity.BaseEntity;
import net.mingsoft.basic.biz.IModelBiz;
import net.mingsoft.basic.entity.ModelEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 模块持久化
 * @author ms dev group
 * @version
 * 版本号：100-000-000<br/>
 * 创建日期：2012-03-15<br/>
 * 历史修订：<br/>
 */
public interface IModelDao extends IBaseDao<ModelEntity>{




    /**
     * 根据角色ID查询模块集合
     * @param roleId 角色ID
     * @return 返回模块集合
     */
    List<ModelEntity> queryModelByRoleId(int roleId);

    /**
     * 根据模块编号查询模块实体
     * @param modelCode 模块编号
     * @return 返回模块实体
     */
    ModelEntity getEntityByModelCode(@Param("modelCode")String modelCode);

    /**
     * 根据模块id获取当前项目中的分类模块id，规则根据modelcode定。**99******,只用是第３位与第４位９９
     * @param modelCodeRegex 规则。详细见IModelBiz
     * @see IModelBiz
     * @param modelId 模块根id
     * @return 返回模块集合
     */
    ModelEntity getModel(@Param("modelCodeRegex") String modelCodeRegex,@Param("modelId") int modelId);


}
