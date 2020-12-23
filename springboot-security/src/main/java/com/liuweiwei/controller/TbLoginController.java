package com.liuweiwei.controller;

import com.liuweiwei.security.JwtAuthenticatioToken;
import com.liuweiwei.utils.SecurityUtils;
import com.liuweiwei.vo.HttpResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;

/**
 * @author liuweiwei
 * @since 2020-05-20
 */
@RestController
public class TbLoginController {

    @Autowired
    private AuthenticationManager authenticationManager;

    /**
     * 登录接口
     */
    @GetMapping(value = "/login")
    public HttpResult login(@RequestParam(name = "username", defaultValue = "abc") @Valid String username,
                            @RequestParam(name = "password", defaultValue = "123") @Valid String password,
                            HttpServletRequest request) throws IOException {
        /**
         * 系统登录认证
         */
        JwtAuthenticatioToken token = SecurityUtils.login(request, username, password, authenticationManager);
        return HttpResult.ok(token);
    }

    @PreAuthorize("hasAuthority('sys:user:view')")
    @GetMapping(value="/findAll")
    public HttpResult findAll() {
        return HttpResult.ok("the findAll service is called success.");
    }

    @PreAuthorize("hasAuthority('sys:user:edit')")
    @GetMapping(value="/edit")
    public HttpResult edit() {
        return HttpResult.ok("the edit service is called success.");
    }

    @PreAuthorize("hasAuthority('sys:user:delete')")
    @GetMapping(value="/delete")
    public HttpResult delete() {
        return HttpResult.ok("the delete service is called success.");
    }
}