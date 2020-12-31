package org.liuweiwei.controller;

import com.alibaba.fastjson.JSON;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.liuweiwei.component.JwtAuthenticatioToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Liuweiwei
 * @since 2020-12-31
 */
@Controller
public class TbUserController {

    private static final String    USERNAME = Claims.SUBJECT;
    private static final String     CREATED = "created";
    private static final String AUTHORITIES = "authorities";
    private static final String      SECRET = "abcdefgh";
    private static final long   EXPIRE_TIME = 12 * 60 * 60 * 1000;

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

        Map<String, Object> claims = new HashMap<>(3);
        String username2 = null;
        if (authentic != null) {
            Object principal = authentic.getPrincipal();
            if (principal != null && principal instanceof UserDetails) {
                username2 = ((UserDetails) principal).getUsername();
            }
        }
        claims.put(USERNAME, username2);
        claims.put(CREATED, new Date());
        claims.put(AUTHORITIES, authentic.getAuthorities());
        String generateToken = generateToken(claims);

        token.setToken(generateToken);

        String json = JSON.toJSONString(token);
        return json;
    }

    private String generateToken(Map<String, Object> claims) {
        Date expirationDate = new Date(System.currentTimeMillis() + EXPIRE_TIME);
        String compact = Jwts.builder().setClaims(claims).setExpiration(expirationDate).signWith(SignatureAlgorithm.HS512, SECRET).compact();
        return compact;
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