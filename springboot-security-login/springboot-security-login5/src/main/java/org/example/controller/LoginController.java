package org.example.controller;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author Administrator
 * @since 2021-12-29
 */
@Controller
@Api(value = "", tags = "用户管理控制器")
@Slf4j
public class LoginController {
    @GetMapping("/500")
    public String notFoundPage() {
        return "500";
    }

    @GetMapping("/404")
    public String accessError() {
        return "404";
    }

    @GetMapping("/200")
    public String internalError() {
        return "200";
    }

    @GetMapping(value = "/login")
    private String loginPage() {
        System.out.println("[step 00] -> 登录方法loginPage()...");
        return "login";
    }

    @GetMapping(value = "/user/login")
    private String loginUserPage() {
        System.out.println("[step 00] -> 登录方法loginUserPage()...");
        return "login";
    }

    /**
     * Request method 'POST' not supported
     *
     * @return
     */
    @PostMapping("/success")
    public String successPage() {
        return "success";
    }

    /**
     * Request method 'POST' not supported
     *
     * @return
     */
    @PostMapping("/failure")
    public String failurePage() {
        return "failure";
    }
}
