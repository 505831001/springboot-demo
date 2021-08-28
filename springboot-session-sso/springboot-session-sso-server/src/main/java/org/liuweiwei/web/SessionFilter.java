package org.liuweiwei.web;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

/**
 * @author Liuweiwei
 * @since 2021-08-28
 */
@WebFilter(filterName = "sessionFilter", urlPatterns = {"/login", "/logout"})
public class SessionFilter implements Filter {

    @Resource
    private RestTemplate restTemplate;
    @Resource
    private RedisTemplate redisTemplate;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

    }

    @Override
    public void destroy() {

    }
}
