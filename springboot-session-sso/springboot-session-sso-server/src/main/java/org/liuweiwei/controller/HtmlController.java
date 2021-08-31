package org.liuweiwei.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

/**
 * @author Liuweiwei
 * @since 2021-08-04
 */
@Controller
@Log4j2
public class HtmlController {

    /**
     * TODO->Session: 7b4f9a21-1cf3-43f0-a415-6b2575581fa5, port: 9301
     * TODO->Cookie: SESSION=MmQzZjcyZjEtZDMwNi00YWY1LThhMDMtZGE2YzVmMDU1ZTU2
     * TODO->Session: 7b4f9a21-1cf3-43f0-a415-6b2575581fa5, port: 9302
     * TODO->Cookie: SESSION=MmQzZjcyZjEtZDMwNi00YWY1LThhMDMtZGE2YzVmMDU1ZTU2
     * localhost:0>keys * [Alt+Enter]
     * 1) "liuweiwei:sessions:7b4f9a21-1cf3-43f0-a415-6b2575581fa5"
     * 2) "liuweiwei:expirations:1630307160000"
     * 3) "liuweiwei:sessions:expires:7b4f9a21-1cf3-43f0-a415-6b2575581fa5"
     *
     * @param request
     * @return
     */
    @GetMapping(value = "/session")
    @ResponseBody
    public String session(HttpServletRequest request) {
        String token = "session: " + request.getSession().getId() + ", port: " + request.getServerPort();
        log.info("请求Url.session() -> {}", token);
        return token;
    }

    @GetMapping(value = "/login")
    public String login(HttpServletRequest request, ModelMap modelMap) {
        modelMap.addAttribute("clientUrl", request.getRequestURL());
        modelMap.addAttribute("sessionId", request.getSession().getId());
        return "index";
    }

    @PostMapping(value = "/login")
    public String login(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam(value = "username", required = true, defaultValue = "guest") String username,
            @RequestParam(value = "password", required = true, defaultValue = "12345") String password) throws IOException {
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            return "index";
        }
        //Spring Session最核心就是存储SessionID，和Token毛关系都没有
        String token = UUID.randomUUID().toString();
        String clientUrl = String.valueOf(request.getRequestURL());
        if (!StringUtils.isEmpty(clientUrl)) {
            if (clientUrl.contains("?")) {
                clientUrl = clientUrl + "&";
            } else {
                clientUrl = clientUrl + "?";
            }
            log.info("请求地址到底是啥：{}", clientUrl + "token=" + token);
        }
        return "success";
    }
}
