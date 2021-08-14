package com.liuweiwei.component;

import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.web.session.InvalidSessionStrategy;
import org.springframework.stereotype.Component;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Liuweiwei
 * @since 2021-04-12
 */
@Component(value = "my05InvalidSessionStrategy")
@Log4j2
public class My05InvalidSessionStrategy implements InvalidSessionStrategy {

    @Override
    public void onInvalidSessionDetected(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        log.info("失效的会话策略...");
        RequestDispatcher dispatcher = request.getRequestDispatcher(request.getServletPath());
        dispatcher.forward(request, response);
    }
}
