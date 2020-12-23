package com.liuweiwei.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.liuweiwei.model.TbToken;
import com.liuweiwei.security.JwtAuthenticatioToken;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author liuweiwei
 * @since 2020-05-20
 */
@RestController
public class TbLoginController {

    private static final String USERNAME = Claims.SUBJECT;
    private static final String CREATED = "created";
    private static final String AUTHORITIES = "authorities";
    private static final String SECRET = "abcdefgh";
    private static final long EXPIRE_TIME = 12 * 60 * 60 * 1000;

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
        JwtAuthenticatioToken token = new JwtAuthenticatioToken(username, password);
        WebAuthenticationDetailsSource detailsSource = new WebAuthenticationDetailsSource();
        WebAuthenticationDetails buildDetails = detailsSource.buildDetails(request);
        token.setDetails(buildDetails);

        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = authenticationManager.authenticate(token);
        context.setAuthentication(authentication);

        Map<String, Object> claims = new HashMap<>(3);
        claims.put(USERNAME, ((UserDetails) authentication.getPrincipal()).getUsername());
        claims.put(CREATED, new Date());
        claims.put(AUTHORITIES, authentication.getAuthorities());

        Date expirationDate = new Date(System.currentTimeMillis() + EXPIRE_TIME);
        String generateToken = Jwts.builder().setClaims(claims).setExpiration(expirationDate).signWith(SignatureAlgorithm.HS512, SECRET).compact();

        token.setToken(generateToken);

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