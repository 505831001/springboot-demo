package com.liuweiwei.component;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Liuweiwei
 * @since 2021-04-12
 */
@Component(value = "my06SessionInformationExpiredStrategy")
@Log4j2
public class My06SessionInformationExpiredStrategy implements SessionInformationExpiredStrategy {

    @Override
    public void onExpiredSessionDetected(SessionInformationExpiredEvent event) throws IOException, ServletException {
        log.info("过期的会话策略...");
        HttpServletRequest request = event.getRequest();
        HttpServletResponse response = event.getResponse();
        SessionInformation information = event.getSessionInformation();
        Map<String, Object> map = new HashMap<>();
        map.put("code", 500);
        map.put("message", "此账号在另一台设备上已登录，您被迫下线。");
        map.put("x", information.isExpired());
        map.put("a", information.getLastRequest());
        map.put("b", information.getPrincipal());
        map.put("c", information.getSessionId());
        String json = new ObjectMapper().writeValueAsString(map);
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(json);
    }
}
