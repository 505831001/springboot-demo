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


package net.mingsoft.basic.biz.impl;

import net.mingsoft.base.biz.impl.BaseBizImpl;
import net.mingsoft.base.dao.IBaseDao;
import net.mingsoft.base.entity.ResultData;
import net.mingsoft.basic.bean.RoleBean;
import net.mingsoft.basic.biz.IModelBiz;
import net.mingsoft.basic.biz.IRoleBiz;
import net.mingsoft.basic.biz.IRoleModelBiz;
import net.mingsoft.basic.dao.IModelDao;
import net.mingsoft.basic.dao.IRoleDao;
import net.mingsoft.basic.dao.IRoleModelDao;
import net.mingsoft.basic.entity.RoleEntity;
import net.mingsoft.basic.entity.RoleModelEntity;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


/**
 * 角色业务层接口实现类
 * @author 张敏
 * @version
 * 版本号：100-000-000<br/>
 * 创建日期：2012-03-15<br/>
 * 历史修订：<br/>
 */
@Service("roleBiz")
@Transactional
public class RoleBizImpl extends BaseBizImpl<IRoleDao, RoleEntity> implements IRoleBiz {

    /**
     * 注入角色持久化层
     */
    @Autowired
    private IRoleDao roleDao;

    @Autowired
    private IRoleModelBiz roleModelBiz;

    @Autowired
    private IModelBiz modelBiz;

    /**
     * 获取角色持久化层
     * @return roleDao 返回角色持久化层
     */
    @Override
    public IBaseDao getDao() {
        return roleDao;
    }


    @Override
    public boolean saveOrUpdateRole(RoleBean role) {

        //根据当前角色名称查询
        RoleBean oldRole = new RoleBean();
        oldRole.setRoleName(role.getRoleName());
        oldRole = (RoleBean) roleDao.getByEntity(oldRole);

        // 第一步：先判断是新增角色还是修改角色
        // 通过角色id判断是保存还是修改
        if(StringUtils.isNotEmpty(role.getId())){
            //若为更新角色，数据库中存在该角色名称且当前名称不为更改前的名称，则属于重名
            if(oldRole != null && !oldRole.getId().equals(role.getId())){
                return false;
            }
            roleDao.updateEntity(role);

        }else{
            //判断角色名是否重复
            if(oldRole != null){
                return false;
            }
            //获取管理员id
            this.save(role);
            roleDao.updateCache();
        }

        //第二步：更新角色对应的菜单
        //开始保存相应的关联数据。组织角色模块的列表。
        List<RoleModelEntity> roleModelList = new ArrayList<>();
        if(!StringUtils.isEmpty(role.getIds())){
            for(String id : role.getIds().split(",")){
                RoleModelEntity roleModel = new RoleModelEntity();
                roleModel.setRoleId(Integer.parseInt(role.getId()));
                roleModel.setModelId(Integer.parseInt(id));
                roleModelList.add(roleModel);
            }
            //先删除当前的角色关联菜单，然后重新添加。
            roleModelBiz.deleteEntity(Integer.parseInt(role.getId()));
            modelBiz.updateCache();

            //加上数量参数用于区分IBaseBiz的重名方法
            roleModelBiz.saveBatch(roleModelList, roleModelList.size());
        }else{
            roleModelBiz.deleteEntity(Integer.parseInt(role.getId()));
        }

        return true;
    }
}
