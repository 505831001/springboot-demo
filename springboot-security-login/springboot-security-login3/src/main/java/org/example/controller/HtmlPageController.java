package org.example.controller;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author Administrator
 * @since 2021-12-29
 */
@Controller
@Api(value = "", tags = "页面请求管理控制器")
@Slf4j
public class HtmlPageController {

    //-------------------- 无限制访问页面 --------------------

    @GetMapping("/200")
    public String page200() {
        System.out.println("[step 00] -> 无限制页面方法page200()...");
        return "200";
    }

    @GetMapping("/403.html")
    public String page403() {
        System.out.println("[step 00] -> 请求异常页面方法page403()...");
        return "403";
    }

    //-------------------- 登录请求访问页面 --------------------

    @GetMapping(value = "/login")
    private String loginPage() {
        System.out.println("[step 00] -> 登录方法loginPage()...");
        return "login";
    }

    @GetMapping(value = "/login.html")
    private String loginHtmlPage() {
        System.out.println("[step 00] -> 登录方法loginHtmlPage()...");
        return "login";
    }

    //-------------------- 权限请求访问页面 --------------------

    @PreAuthorize(value = "hasAnyAuthority('ROLE_ADMIN', 'ROLE_GUEST', 'ROLE_USER')")
    @GetMapping(value = "/admin/index")
    public String adminPage() {
        System.out.println("[step 00] -> 请求权限控制：adminPage()...");
        return "admin/index";
    }

    @PreAuthorize(value = "hasAnyAuthority('ROLE_GUEST')")
    @GetMapping(value = "/guest/index")
    public String guestPage() {
        System.out.println("[step 00] -> 请求权限控制：adminPage()...");
        return "guest/index";
    }

    @PreAuthorize(value = "hasAnyAuthority('ROLE_USER')")
    @GetMapping(value = "/user/index")
    public String userPage() {
        System.out.println("[step 00] -> 请求权限控制：adminPage()...");
        return "user/index";
    }

    //-------------------- 表单响应访问页面 --------------------

    /**
     * Request method 'POST' not supported
     *
     * @return
     */
    @PostMapping("/success")
    public String successPage() {
        System.out.println("[step 00] -> 登录成功方法successPage()...");
        return "success";
    }

    /**
     * Request method 'POST' not supported
     *
     * @return
     */
    @PostMapping("/failure")
    public String failurePage() {
        System.out.println("[step 00] -> 登录失败方法failurePage()...");
        return "failure";
    }
}
