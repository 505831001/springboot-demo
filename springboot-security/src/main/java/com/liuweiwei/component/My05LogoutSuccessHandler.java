package com.liuweiwei.component;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component(value = "my05LogoutSuccessHandler")
public class My05LogoutSuccessHandler implements LogoutSuccessHandler {

    /**
     * 日志-实现层：logback
     */
    private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(this.getClass());

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        response.setContentType("application/json;charset=UTF-8");
        log.info("登录退出b...");
    }
}
