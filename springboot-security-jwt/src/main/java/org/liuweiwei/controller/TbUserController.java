package org.liuweiwei.controller;

import com.alibaba.fastjson.JSON;
import org.liuweiwei.component.JwtAuthenticatioToken;
import org.liuweiwei.model.TbUser;
import org.liuweiwei.utils.JwtTokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author Liuweiwei
 * @since 2020-12-31
 */
@RestController
public class TbUserController {

    @Autowired
    private AuthenticationManager authenticationManager;

    /**
     * 登录接口
     */
    @PostMapping(value = "/login")
    public String login(@RequestBody TbUser tbUser, HttpServletRequest request) throws IOException {
        WebAuthenticationDetails details = new WebAuthenticationDetailsSource().buildDetails(request);
        String username = tbUser.getUsername();
        String password = tbUser.getPassword();
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
    @GetMapping(value = "/user/findAll")
    public String findAll() {
        return "查询服务操作成功。";
    }

    @PreAuthorize("hasAuthority('sys:user:edit')")
    @GetMapping(value = "/user/edit")
    public String edit() {
        return "编辑服务操作成功。";
    }

    @PreAuthorize("hasAuthority('sys:user:delete')")
    @GetMapping(value = "/user/delete")
    public String delete() {
        return "删除服务操作成功。";
    }
}