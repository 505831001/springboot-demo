package org.liuweiwei.controller;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.liuweiwei.entity.TbUser;
import org.liuweiwei.utils.ResultData;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author Liuweiwei
 * @since 2020-12-31
 */
@Controller
@Api(value = "", tags = "用户管理控制器")
@Log4j2
public class TbUserController {

    private static final String    USERNAME = Claims.SUBJECT;
    private static final String     CREATED = "created";
    private static final String AUTHORITIES = "authorities";
    private static final String      SECRET = Base64Utils.encodeToString("secret".getBytes());
    private static final long   EXPIRE_TIME = 12 * 60 * 60 * 1000;
    private static final Date          date = new Date(System.currentTimeMillis() + EXPIRE_TIME);

    @GetMapping(value = "/echo")
    @ApiOperation(value = "获取请求头Token信息", notes = "获取请求头Token信息", tags = "")
    @ResponseBody
    public ResultData echo(HttpServletRequest request) {
        Claims claims = (Claims) request.getAttribute("userToken");
        if (Objects.nonNull(claims)) {
            String username = claims.getSubject();
            return ResultData.success(username);
        } else {
            return ResultData.failure(HttpStatus.UNAUTHORIZED);
        }
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

    // --- 关联方法 ---

    /**
     * TODO->java-jwt依赖包创建方式：JWT.create().withAudience(user.getId()).sign(Algorithm.HMAC256(user.getPassword()));
     * @param user
     * @return
     */
    private String getToken(TbUser user) {
        String token = Jwts.builder().claim("auth", new HashMap<>()).setId(String.valueOf(user.getId())).signWith(SignatureAlgorithm.HS512, user.getPassword()).compact();
        return token;
    }

    /**
     * TODO->jjwt依赖包构建方式
     * @param claims
     * @return
     */
    private String generateToken(Map<String, Object> claims) {
        String token = Jwts.builder().setClaims(claims).setExpiration(date).signWith(SignatureAlgorithm.HS512, SECRET).compact();
        return token;
    }
}