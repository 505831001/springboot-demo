package com.liuweiwei.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * java web 应用拦截请求的三种方式：
 * 1. 过滤器 - Filter
 * 2. 拦截器 - Interceptor
 * 3. 切面类 - Aspect
 * 总结：
 * 1. Filter 是 java web 里面的，肯定获取不到 spring 里面 Controller 的信息。
 * 2. Interceptor、Aspect 是和 spring 相关的，所以能获取到 Controller 的信息。
 *
 * @author liuweiwei
 * @since 2020-05-20
 */
@Component
@Slf4j
public class Web01MvcInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("01 - Interceptor 拦截器：preHandle(request, response, handler) 方法。");
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
