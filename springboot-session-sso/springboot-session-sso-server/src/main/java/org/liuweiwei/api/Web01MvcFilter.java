package org.liuweiwei.api;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * java web 应用拦截请求的三种方式：
 * 1. 过滤器 - Filter
 * 2. 拦截器 - Interceptor
 * 3. 切面类 - Aspect
 * 总结：
 * 1. Filter 是 java web 里面的，肯定获取不到 spring 里面 Controller 的信息。
 * 2. Interceptor、Aspect 是和 spring 相关的，所以能获取到 Controller 的信息。
 *
 * @author Liuweiwei
 * @since 2021-03-22
 */
@Component
@Log4j2
public class Web01MvcFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info("01 - Filter 过滤器：doFilter(request, response, chain) 方法。");

        String sessionId = ((HttpServletRequest) request).getRequestedSessionId();
        //根据某个值或某些值判断是否已登录
        if (StringUtils.isNotEmpty(sessionId) && sessionId != null) {
            //如果已登录则通过放行
            log.info("01 - Filter 过滤器：缓存会话和浏览器会话比较：{}", ((HttpServletRequest) request).getSession().getId());
        } else {
            //如果未登录则重定向跳转至sso认证中心
            log.info("01 - Filter 过滤器：重定向到单点认证中心登录：{}", ((HttpServletRequest) request).getRequestURL().toString());
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
