package com.liuweiwei.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.liuweiwei.model.TbToken;
import com.liuweiwei.security.JwtAuthenticatioToken;
import com.liuweiwei.utils.SecurityUtils;
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
    public TbToken login(@RequestParam(name = "username", defaultValue = "abc") @Valid String username,
                        @RequestParam(name = "password", defaultValue = "123") @Valid String password,
                        HttpServletRequest request) throws IOException {
        /**
         * 系统登录认证
         */
        JwtAuthenticatioToken token = SecurityUtils.login(request, username, password, authenticationManager);
        TbToken tbToken = JSON.parseObject(JSON.toJSONString(token), new TypeReference<TbToken>() {});
        return tbToken;
    }

    @PreAuthorize("hasAuthority('sys:user:view')")
    @GetMapping(value = "/findAll")
    public String findAll() {
        return "the findAll service is called success";
    }

    @PreAuthorize("hasAuthority('sys:user:edit')")
    @GetMapping(value = "/edit")
    public String edit() {
        return "the edit service is called success";
    }

    @PreAuthorize("hasAuthority('sys:user:delete')")
    @GetMapping(value = "/delete")
    public String delete() {
        return "the delete service is called success";
    }
}