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


package net.mingsoft.basic.realm;

import net.mingsoft.base.entity.BaseEntity;
import net.mingsoft.basic.biz.IManagerBiz;
import net.mingsoft.basic.biz.IModelBiz;
import net.mingsoft.basic.entity.ManagerEntity;
import net.mingsoft.basic.entity.ModelEntity;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 管理员shiro权限控制
 *
 * @author killfenQQ:78750478
 * @version 版本号：<br/>
 *          创建日期：2015年9月9日<br/>
 *          历史修订：<br/>
 */
public class ManagerAuthRealm extends BaseAuthRealm {
    /**
     * 管理员业务层
     */
    @Autowired
    private IManagerBiz managerBiz;

    /**
     * 模块业务层
     */
    @Autowired
    private IModelBiz modelBiz;

    /**
     * 新登用户验证
     */
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        UsernamePasswordToken upToken = (UsernamePasswordToken) token;
        ManagerEntity newManager = new ManagerEntity();
        newManager.setManagerName(upToken.getUsername());
        ManagerEntity manager = (ManagerEntity) managerBiz.getEntity(newManager);
        if (manager != null) {
            return new SimpleAuthenticationInfo(manager, manager.getManagerPassword(), getName());
        }
        return null;
    }

    /**
     * 功能操作授权
     */
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        ManagerEntity newManager = (ManagerEntity) principalCollection.fromRealm(getName()).iterator().next();
        ManagerEntity manager = (ManagerEntity) managerBiz.getEntity(newManager);
        if (null == manager) {
            return null;
        } else {
            SimpleAuthorizationInfo result = new SimpleAuthorizationInfo();
            // 查询管理员对应的角色
            List<ModelEntity> models = modelBiz.queryModelByRoleId(manager.getRoleId());
            for (BaseEntity e:models) {
                ModelEntity me = (ModelEntity) e;
                if (!StringUtils.isEmpty(me.getModelUrl())) {
                    result.addStringPermission(me.getModelUrl());
                }
            }
            return result;

        }
    }

}
