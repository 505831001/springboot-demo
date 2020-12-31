package org.liuweiwei.controller;

import com.alibaba.fastjson.JSON;
import org.liuweiwei.component.JwtAuthenticatioToken;
import org.liuweiwei.utils.JwtTokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author Liuweiwei
 * @since 2020-12-31
 */
@Controller
public class TbUserController {

    @Autowired
    private AuthenticationManager authenticationManager;

    /**
     * 登录接口
     */
    @GetMapping(value = "/login")
    @ResponseBody
    public String login(@RequestParam(name = "username", defaultValue = "abc") String username,
                        @RequestParam(name = "password", defaultValue = "123") String password,
                        HttpServletRequest request) throws IOException {
        WebAuthenticationDetails details = new WebAuthenticationDetailsSource().buildDetails(request);
        // 系统登录认证
        JwtAuthenticatioToken token = new JwtAuthenticatioToken(username, password);
        token.setDetails(details);

        Authentication authentic = authenticationManager.authenticate(token);
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(authentic);

        token.setToken(JwtTokenUtils.generateToken(authentic));

        String json = JSON.toJSONString(token);
        return json;
    }

    @PreAuthorize("hasAuthority('sys:user:view')")
    @PostMapping(value = "/user/findAll")
    public String success() {
        return "success";
    }

    @PreAuthorize("hasAuthority('sys:user:edit')")
    @PutMapping(value = "/user/edit")
    public String edit() {
        return "edit";
    }

    @PreAuthorize("hasAuthority('sys:user:delete')")
    @DeleteMapping(value = "/user/delete")
    public String delete() {
        return "delete";
    }
}