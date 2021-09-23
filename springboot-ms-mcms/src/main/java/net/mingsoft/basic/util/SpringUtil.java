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

package net.mingsoft.basic.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName: SpringUtil
 * @Description: TODO(Spring工具类)
 * @author 铭软开发团队
 * @date 2020年7月2日
 *
 */
@Component
public class SpringUtil implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    private static ThreadLocal<HttpServletRequest> requestThreadLocal=new ThreadLocal<>();
    /**
     * 获取当前请求对象
     *
     * @return
     */
    public static HttpServletRequest getRequest() {
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            return request;
        } catch (Exception e) {
            return requestThreadLocal.get();
        }
    }
    /**
     * 设置当前请求对象
     *
     * @return
     */
    public static void setRequest(HttpServletRequest request) {
        requestThreadLocal.set(request);
    }

    /**
     * 通过spring的webapplicationcontext上下文对象读取bean对象
     *
     * @param sc
     *            上下文servletConext对象
     * @param beanName
     *            要读取的bean的名称
     * @return 返回获取到的对象。获取不到返回null
     */
    @Deprecated
    public static Object getBean(ServletContext sc, String beanName) {
        return getApplicationContext().getBean(beanName);
    }

    /**
     * 通过spring的webapplicationcontext上下文对象读取bean对象
     *
     * @param beanName
     *            要读取的bean的名称
     * @return 返回获取到的对象。获取不到返回null
     */
    public static Object getBean(String beanName) {
        return getApplicationContext().getBean(beanName);
    }

    /**
     * 通过spring的webapplicationcontext上下文对象读取bean对象
     *
     * @param cls
     *            要读取的类名称
     * @return 返回获取到的对象。获取不到返回null
     */
    public static <T> T getBean(Class<T> cls) {
        return getApplicationContext().getBean(cls);
    }



    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (SpringUtil.applicationContext == null) {
            SpringUtil.applicationContext = applicationContext;
        }
    }
    //获取applicationContext
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }
}
