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

import net.mingsoft.basic.annotation.LogAnn;
import net.mingsoft.basic.constant.e.OperatorTypeEnum;
import net.mingsoft.basic.constant.e.SessionConstEnum;
import net.mingsoft.basic.entity.ManagerSessionEntity;
import net.mingsoft.basic.util.BasicUtil;
import org.springframework.stereotype.Component;

/**
 *
 * @author by 铭软开发团队
 * @Description TODO
 * @date 2019/11/20 10:28
 */
@Component
public class SystemLogAop extends BaseLogAop{


    @Override
    public String getUserName() {
        //后台用户获取用户名
        ManagerSessionEntity managerSession = (ManagerSessionEntity) BasicUtil
                .getSession(SessionConstEnum.MANAGER_SESSION);
        return managerSession.getManagerName();
    }

    @Override
    public boolean isCut(LogAnn log) {
        //只有后台用户操作才走这个AOP
        return log.operatorType() == OperatorTypeEnum.MANAGE;
    }
}
