package com.liuweiwei.component;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author Liuweiwei
 * @since 2021-04-06
 */
@Component(value = "my06AccessDeniedHandler")
@Slf4j
public class My06AccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json;charset=utf-8");
        PrintWriter out = response.getWriter();
        log.error("权限不足，请联系管理员。");
        out.write(new ObjectMapper().writeValueAsString("权限不足，请联系管理员。"));
        out.flush();
        out.close();
    }
}
