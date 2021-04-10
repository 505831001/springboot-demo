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
        log.info("01 - Web Mvc 过滤器：doFilter(request, response, chain) 方法。");
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
