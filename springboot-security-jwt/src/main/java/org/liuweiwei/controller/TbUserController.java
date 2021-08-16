package org.liuweiwei.controller;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.Date;
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
    private static final String      SECRET = "secret";
    private static final long   EXPIRE_TIME = 12 * 60 * 60 * 1000;

    private String generateToken(Map<String, Object> claims) {
        Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
        String compact = Jwts.builder().setClaims(claims).setExpiration(date).signWith(SignatureAlgorithm.HS512, SECRET).compact();
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