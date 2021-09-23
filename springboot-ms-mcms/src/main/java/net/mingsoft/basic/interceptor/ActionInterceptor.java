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


package net.mingsoft.basic.interceptor;

import net.mingsoft.basic.constant.Const;
import net.mingsoft.basic.constant.e.SessionConstEnum;
import net.mingsoft.basic.util.BasicUtil;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 所有action的拦截器，主要是设置base与basepath
 *
 * @author ms dev group
 * @version 版本号：100-000-000<br/>
 *          创建日期：2012-03-15<br/>
 *          历史修订：<br/>
 */
public class ActionInterceptor  extends HandlerInterceptorAdapter {

    @Value("${ms.manager.path}")
    private String managerPath;
    /**
     * 所有action的拦截,主要拦截base与basepath
     *
     * @param request
     *            HttpServletRequest对象
     * @param response
     *            HttpServletResponse 对象
     * @param handler
     *            处理器
     * @throws Exception
     *             异常处理
     * @return 处理后返回true
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

//		String modelId = request.getParameter(Const.MODEL_ID); // 获取模块编号
//		if (NumberUtils.isNumber(modelId)) {
//			request.setAttribute(Const.MODEL_ID, modelId);
//			request.getSession().setAttribute(SessionConstEnum.MODEL_ID_SESSION.toString(), modelId);
//			request.getSession().setAttribute(SessionConstEnum.MODEL_TITLE_SESSION.toString(),request.getParameter("modelTitle"));
//		}

        String base = request.getScheme() + "://" + request.getServerName() + (request.getServerPort() == 80 ? "" : ":" + request.getServerPort());
        String contextPath = request.getServletContext().getContextPath();
        //项目路径
        request.setAttribute(Const.BASE,contextPath);
        //设置后台路径
        request.setAttribute(Const.MANAGER_PATH, contextPath + managerPath);
        //系统访问地址
        request.setAttribute(Const.BASE_PATH, base + contextPath);
        //设置当前地址参数，方便页面获取
        request.setAttribute(Const.PARAMS, BasicUtil.assemblyRequestUrlParams());
        return true;
    }

}
