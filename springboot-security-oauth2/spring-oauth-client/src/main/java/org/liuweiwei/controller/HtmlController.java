package org.liuweiwei.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Liuweiwei
 * @since 2021-08-04
 */
@Controller
@Api(value = "", tags = "Html页面跳转控制器")
@Log4j2
public class HtmlController {

    @GetMapping(value = "/product/{id}")
    public ResponseEntity echo(@PathVariable String id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("SecurityContextHolder.getContext().getAuthentication() ->{}", authentication);
        return ResponseEntity.ok("Product id:" + id);
    }

    @GetMapping(value = "/login")
    @ApiOperation(value = "登录页面", notes = "登录页面", tags = "")
    public String login(@RequestParam(value = "code") String code) {
        log.info("请求Url.login() -> 授权码:{}", code);
        return "login_page";
    }

    @PostMapping(value = "/oauth")
    @ApiOperation(value = "授权页面", notes = "授权页面", tags = "")
    public String oauth() {
        log.info("请求Url.oauth()");
        return "oauth_page";
    }
}
