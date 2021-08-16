package org.liuweiwei.web;

import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.DisabledException;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Liuweiwei
 * @since 2021-08-06
 */
@Log4j2
public class VerifyCodeFilter extends OncePerRequestFilter {
    /**{AntPathMatcher}蚂蚁路径请求匹配器。*/
    private static final PathMatcher pathMatcher = new AntPathMatcher();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //如果判断失败则怎么滴
        if (isProtectedUrl(request)) {
            String verifyCode = request.getParameter("verifyCode");
            String validateCode = (String) request.getSession().getAttribute("validateCode");
            if (validateVerify(verifyCode)) {
                filterChain.doFilter(request, response);
            } else {
                log.error("生成验证码:{},输入验证码:{}", validateCode.toLowerCase(), verifyCode.toLowerCase());
                //手动设置异常
                request.getSession().setAttribute("SPRING_SECURITY_LAST_EXCEPTION", new DisabledException("验证码输入错误"));
                //请求转发
                request.getRequestDispatcher("/index").forward(request, response);
                //请求重定向
                //response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                //response.setContentType("application/json;charset=utf-8");
                //response.sendRedirect("/index");
            }
        //否则返回成功
        } else {
            filterChain.doFilter(request, response);
        }
    }

    /**
     * 拦截/login的POST请求
     * 登录页面表单action="/login" - "POST".equals(request.getMethod()) && pathMatcher.match("/login", request.getServletPath())
     * @param request
     * @return
     */
    private boolean isProtectedUrl(HttpServletRequest request) {
        return "POST".equals(request.getMethod()) && pathMatcher.match("/authentication/form", request.getServletPath());
    }

    /**
     * validateCode - 请求对象{VerifyCodeUtils}生成验证验证码
     * @param verifyCode 表单输入框验证码
     * @return
     */
    private boolean validateVerify(String verifyCode) {
        //获取当前线程绑定的请求对象
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        //这个请求对象是在{VerifyCodeUtils继承Servlet}中存入Session的名字。不分区大小写。
        String validateCode = ((String) request.getSession().getAttribute("validateCode")).toLowerCase();
        log.info("Http servlet 生成验证码: {}", validateCode);
        verifyCode = verifyCode.toLowerCase();
        log.info("Http request 表单验证码: {}", verifyCode);
        return validateCode.equals(verifyCode);
    }
}
