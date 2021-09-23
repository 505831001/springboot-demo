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

import cn.hutool.core.util.ObjectUtil;
import net.mingsoft.basic.constant.e.SessionConstEnum;
import net.mingsoft.basic.entity.ManagerSessionEntity;
import net.mingsoft.basic.util.BasicUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Date;

/**
 * 拦截保存更新方法，设置操作人和时间
 * @author by Administrator
 * @Description TODO
 * @date 2019/11/12 10:14
 */
@Component
@Aspect
public class SaveOrUpdateAop extends BaseAop {

    @Pointcut("execution(* net.mingsoft..*Action.save(..))")
    public void save() {

    }

    @Pointcut("execution(* net.mingsoft..*Action.update(..))")
    public void update() {
    }

    @Before("save()")
    public void save(JoinPoint jp) {
        setField(jp, "createDate", new Date());
        setField(jp, "updateDate", new Date());
        ManagerSessionEntity session = (ManagerSessionEntity) BasicUtil.getSession(SessionConstEnum.MANAGER_SESSION);
        if (session != null) {
            setField(jp, "createBy", session.getId());
        }

    }


    @Before("update()")
    public void update(JoinPoint jp) {
        setField(jp, "updateDate", new Date());
        ManagerSessionEntity session = (ManagerSessionEntity) BasicUtil.getSession(SessionConstEnum.MANAGER_SESSION);
        if (session != null) {
            setField(jp, "updateBy", session.getId());
        }
    }


    private void setField(JoinPoint jp, String name, Object obj) {
        try {
            Object[] objs = jp.getArgs();
            if (objs.length == 0 || ObjectUtil.isNull(objs[0])) {
                return;
            }
            //获取对象所有字段
            Field[] allFields = BasicUtil.getAllFields(objs[0]);
            for (Field field : allFields) {
                //判断是否存在
                if (name.equals(field.getName())) {
                    field.setAccessible(true);
                    //设置时间
                    field.set(objs[0], obj);
                }
            }
        } catch (Exception e) {
            LOG.error("Aop错误：", e);
        }
    }

}
