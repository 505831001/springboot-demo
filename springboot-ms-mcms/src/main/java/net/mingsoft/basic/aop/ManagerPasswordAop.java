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
package net.mingsoft.basic.aop;

import cn.hutool.crypto.SecureUtil;
import net.mingsoft.base.entity.ResultData;
import net.mingsoft.basic.bean.ManagerModifyPwdBean;
import net.mingsoft.basic.biz.IManagerBiz;
import net.mingsoft.basic.constant.e.SessionConstEnum;
import net.mingsoft.basic.entity.ManagerSessionEntity;
import net.mingsoft.basic.util.BasicUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author: xierz
 * @Description:
 * @Date: Create in 2021/03/13 8:40
 */
@Component
@Aspect
public class ManagerPasswordAop extends BaseAop {


    @Resource
    IManagerBiz managerBiz;

    @Pointcut("execution(* net.mingsoft.basic.action.MainAction.updatePassword(..))")
    public void updatePassword() {
    }

    /**
     * 修改密码时候，将密码md5处理
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("updatePassword()")
    public Object updatePassword(ProceedingJoinPoint joinPoint) throws Throwable {
        LOG.debug("basic ManagerPasswordAop 修改密码为md5");
        ManagerModifyPwdBean managerModifyPwdBean = super.getType(joinPoint, ManagerModifyPwdBean.class);
        //获取session
        ManagerSessionEntity managerSession = (ManagerSessionEntity) BasicUtil.getSession(SessionConstEnum.MANAGER_SESSION);
        managerModifyPwdBean.setOldManagerPassword(SecureUtil.md5(managerModifyPwdBean.getOldManagerPassword()));
        return joinPoint.proceed();
    }



}
