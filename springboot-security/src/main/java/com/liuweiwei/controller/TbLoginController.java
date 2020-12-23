package com.liuweiwei.controller;

import com.liuweiwei.security.JwtAuthenticatioToken;
import com.liuweiwei.utils.SecurityUtils;
import com.liuweiwei.vo.HttpResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.PostMapping;
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
    @PostMapping(value = "/login")
    public HttpResult login(@RequestParam(name = "username") @Valid String username,
                            @RequestParam(name = "password") @Valid String password,
                            HttpServletRequest request) throws IOException {
        /**
         * 系统登录认证
         */
        JwtAuthenticatioToken token = SecurityUtils.login(request, username, password, authenticationManager);
        return HttpResult.ok(token);
    }
}