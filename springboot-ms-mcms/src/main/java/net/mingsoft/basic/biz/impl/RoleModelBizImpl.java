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
import net.mingsoft.basic.biz.IRoleModelBiz;
import net.mingsoft.basic.dao.IRoleModelDao;
import net.mingsoft.basic.entity.RoleModelEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * 角色模块关联业务层接口实现类
 * @author 张敏
 * @version
 * 版本号：100-000-000<br/>
 * 创建日期：2012-03-15<br/>
 * 历史修订：<br/>
 */
@Service("roleModelBiz")
@Transactional
public class RoleModelBizImpl extends BaseBizImpl<IRoleModelDao, RoleModelEntity> implements IRoleModelBiz {

    /**
     * 角色模块关联持久化层
     */
    @Autowired
    private IRoleModelDao roleModelDao;

    /**
     * 获取角色模块持久化层
     * @return roleModelDao 返回角色模块持久化层
     */
    @Override
    public IBaseDao getDao() {
        // TODO Auto-generated method stub
        return roleModelDao;
    }

    @Override
    public void saveEntity(List<RoleModelEntity> roleModelList){
        // TODO Auto-generated method stub
        roleModelDao.saveEntity(roleModelList);
    }

    @Override
    public void updateEntity(List<RoleModelEntity> roleModelList){
        // TODO Auto-generated method stub
        roleModelDao.updateEntity(roleModelList);
    }

    @Override
    public List<RoleModelEntity> queryByRoleId(int roleId) {
        // TODO Auto-generated method stub
        return roleModelDao.queryByRoleId(roleId);
    }

    @Override
    public void deleteByRoleId(int roleId) {
        roleModelDao.deleteByRoleId(roleId);
    }


}
