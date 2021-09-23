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
package net.mingsoft.basic.strategy;

import cn.hutool.crypto.SecureUtil;
import net.mingsoft.base.entity.ResultData;
import net.mingsoft.basic.biz.IManagerBiz;
import net.mingsoft.basic.biz.IModelBiz;
import net.mingsoft.basic.constant.e.SessionConstEnum;
import net.mingsoft.basic.entity.ManagerEntity;
import net.mingsoft.basic.entity.ManagerSessionEntity;
import net.mingsoft.basic.entity.ModelEntity;
import net.mingsoft.basic.util.BasicUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 管理员登录列表
 *
 * @author Administrator
 * @version 创建日期：2020/11/18 18:12<br/>
 * 历史修订：<br/>
 */
public class ManagerLoginStrategy implements ILoginStrategy{


    @Autowired
    private IManagerBiz managerBiz;

    @Override
    public Boolean login(ManagerEntity manager) {
        boolean rememberMe = BasicUtil.getBoolean("rememberMe");
        if(manager ==null || StringUtils.isEmpty(manager.getManagerName()) || StringUtils.isEmpty(manager.getManagerPassword())){
            return false;
        }
        // 根据账号获取当前管理员信息
        ManagerEntity newManager = new ManagerEntity();
        newManager.setManagerName(manager.getManagerName());
        ManagerEntity _manager = (ManagerEntity) managerBiz.getEntity(newManager);
        if (_manager == null ) {
            // 系统不存在此用户
            return false;
        } else {
            // 判断当前用户输入的密码是否正确
            if (SecureUtil.md5(manager.getManagerPassword()).equals(_manager.getManagerPassword())) {
//                 创建管理员session对象
                ManagerSessionEntity managerSession = new ManagerSessionEntity();
//                 压入管理员seesion
                BasicUtil.setSession(SessionConstEnum.MANAGER_SESSION, managerSession);
                BeanUtils.copyProperties(_manager, managerSession);
                Subject subject = SecurityUtils.getSubject();
                UsernamePasswordToken upt = new UsernamePasswordToken(managerSession.getManagerName(), managerSession.getManagerPassword());
                upt.setRememberMe(rememberMe);
                subject.login(upt);
                return true;
            } else {
                // 密码错误
                return false;
            }
        }
    }
}
