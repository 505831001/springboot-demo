package org.liuweiwei.controller;

import io.swagger.annotations.Api;
import lombok.extern.log4j.Log4j2;
import org.liuweiwei.entity.TbUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;
import java.util.UUID;

/**
 * @author Liuweiwei
 * @since 2020-12-31
 */
@Controller
@Api(value = "", tags = "用户管理控制器")
@Log4j2
public class TbUserController {

    @GetMapping(value = "/login")
    @ResponseBody
    public String doLogin(HttpServletRequest request, HttpServletResponse response,
                          @RequestParam(value = "username", required = true, defaultValue = "admin") String username,
                          @RequestParam(value = "password", required = true, defaultValue = "12345") String password) {
        TbUser user = new TbUser();
        //用户验证，密码验证，等等一大堆验证
        if (Objects.nonNull(user)) {
            user.setUsername(username);
            user.setPassword(password);
        }
        //生成Token
        String token = UUID.randomUUID().toString().replace("-", "");
        //写入Cookie
        request.setAttribute("paramToken", token);
        request.getSession().setAttribute("cookieToken", token);
        response.setHeader("headerToken", token);
        log.info("请求Url.doLogin()");
        return token;
    }

    @GetMapping(value = "/select")
    @ResponseBody
    public String select(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            log.debug("Request Header Cookie -> {}:{}", cookie.getName(), cookie.getValue());
        }
        log.info("请求Url.select()");
        return "Hello World";
    }
}