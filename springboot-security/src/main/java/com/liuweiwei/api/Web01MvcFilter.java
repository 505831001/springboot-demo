package com.liuweiwei.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.Filter;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.FilterChain;
import javax.servlet.ServletResponse;
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
@Slf4j
public class Web01MvcFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info("01 - Filter 过滤器：doFilter(request, response, chain) 方法。");
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
