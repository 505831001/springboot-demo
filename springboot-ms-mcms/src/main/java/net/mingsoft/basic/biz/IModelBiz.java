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


package net.mingsoft.basic.biz;

import net.mingsoft.base.biz.IBaseBiz;
import net.mingsoft.base.constant.e.BaseEnum;
import net.mingsoft.base.entity.BaseEntity;
import net.mingsoft.basic.entity.ModelEntity;
import net.mingsoft.basic.entity.RoleModelEntity;

import java.util.List;

/**
 * 模块业务接口
 * @author ms dev group
 * @version
 * 版本号：100-000-000<br/>
 * 创建日期：2012-03-15<br/>
 * 历史修订：<br/>
 */
public interface IModelBiz extends IBaseBiz<ModelEntity> {


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
    ModelEntity getEntityByModelCode(BaseEnum modelCode);

    /**
     * 根据模块编号查询模块实体
     * @param modelCode 模块编号
     * @return 返回模块实体
     */
    ModelEntity getEntityByModelCode(String modelCode);

    /**
     * 根据模块id获取当前项目中的分类模块id，规则根据modelcode定。**99******,只用是第３位与第４位９９
     * @param modelType 模块类型　
     * @param modelId 模块ID
     * @return 返回模块实体
     */
    ModelEntity getModel(String modelType,int modelId);

    /**	菜单新增递归方法
     * @param modelParent 菜单实体
     * @param parentIds 父级ids
     * @param mangerRoleId  管理员id
     * @param roleModels 角色mode数组
     * @param parentId 父级id
     */
    void reModel(ModelEntity modelParent, String parentIds, int mangerRoleId, List<RoleModelEntity> roleModels, Integer parentId);

    /**
     * 字符串转菜单
     * @param menuStr 菜单json
     * @param mangerRoleId 当前管理员的角色id
     * @param modelId 父级菜单编号，用于导入子菜单设置父级菜单编号
     */
    void jsonToModel(String menuStr,int mangerRoleId,int modelId);

    /**
     * 修改
     * @param model
     */
    void updateEntity(ModelEntity model);

    /**
     * 保存
     * @param model
     */
    void saveEntity(ModelEntity model);

    /**
     * 查询当前菜单所有的子集菜单
     * @param modelEntity
     * @return
     */
    List<ModelEntity> queryChildList(ModelEntity modelEntity);

}
