package org.liuweiwei.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.UUID;

/**
 * @author Liuweiwei
 * @since 2021-08-04
 */
@Controller
@Log4j2
public class HtmlController {

    @GetMapping(value = "/echo")
    @ResponseBody
    public String echo(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.setAttribute("save_session", UUID.randomUUID().toString());
        log.info("请求Url.echo()");
        return String.valueOf(session.getAttribute("save_session"));
    }

    @GetMapping(value = "/admin/echo")
    @ResponseBody
    public String echo2() {
        log.info("请求Url.echo2012()");
        return "Hello World 2212";
    }

    @GetMapping(value = "/guest/echo")
    @ResponseBody
    public String echo4() {
        log.info("请求Url.echo2014()");
        return "Hello World 2214";
    }
}
