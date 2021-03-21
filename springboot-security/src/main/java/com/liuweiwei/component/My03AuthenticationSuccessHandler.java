package com.liuweiwei.component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.liuweiwei.config.WebMvcAutoConfig;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author liuweiwei
 * @since 2020-05-20
 */
@Component
public class My03AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    /**
     * 日志-实现层：logback
     */
    private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(this.getClass());

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("登录成功...");
        response.setStatus(HttpStatus.OK.value());
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(new ObjectMapper().writeValueAsString(authentication));
        new DefaultRedirectStrategy().sendRedirect(request, response, "indexPage");
    }
}
