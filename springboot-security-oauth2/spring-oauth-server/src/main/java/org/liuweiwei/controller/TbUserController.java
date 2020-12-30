package org.liuweiwei.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;

/**
 * @author Administrator
 */
@Controller
public class TbUserController {

    /**
     * 自定义登录页面
     * @return
     */
    @GetMapping(value = "/login")
    public String login() {
        return "login";
    }

    @GetMapping(value = "/unauthorized")
    public String unauthorized() {
        return "unauthorized";
    }

	/**
	 * 资源服务器提供的受保护接口
	 * @param principal
	 * @return
	 */
    @GetMapping(value = "/user")
    @ResponseBody
    public Principal user(Principal principal) {
        System.out.println(principal);
        return principal;
    }
}
