package org.liuweiwei.controller;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.liuweiwei.entity.TbUser;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Liuweiwei
 * @since 2021-08-04
 */
@Controller
@Api(value = "", tags = "Html页面跳转控制器")
@Log4j2
public class HtmlController {

    private static final String    USERNAME = Claims.SUBJECT;
    private static final String     CREATED = "created";
    private static final String AUTHORITIES = "authorities";
    private static final String      SECRET = "secret";
    private static final long   EXPIRE_TIME = 12 * 60 * 60 * 1000;

    @GetMapping(value = "/auth")
    @ApiOperation(value = "上下文信息", notes = "上下文信息", tags = "")
    @ResponseBody
    public Object auth() {
        log.info("来自于：SecurityContextHolder.getContext().setAuthentication(token)");
        return SecurityContextHolder.getContext().getAuthentication();
    }

    @GetMapping(value = "/index")
    @ApiOperation(value = "公司主页", notes = "公司主页", tags = "")
    public String index() {
        log.info("请求Url.index()");
        return "index_page";
    }

    @GetMapping(value = "/loginPage")
    @ApiOperation(value = "登录页面", notes = "登录页面", tags = "")
    public String login() {
        log.info("请求Url.loginPage(String loginPage)");
        return "login_page";
    }

    @GetMapping(value = "/successPage")
    @ApiOperation(value = "成功页面", notes = "成功页面", tags = "")
    public String success() {
        log.info("请求Url.successHandler(String successPage)");
        return "success_page";
    }

    @GetMapping(value = "/failurePage")
    @ApiOperation(value = "失败页面", notes = "失败页面", tags = "")
    public String failure() {
        log.info("请求Url.failureHandler(String failurePage)");
        return "failure_page";
    }

    @GetMapping(value = "/error")
    @ApiOperation(value = "异常页面", notes = "异常页面", tags = "")
    public String error() {
        log.info("请求Url.errorHandler(String errorPage)");
        return "error_page";
    }

    // --- 关联方法 ---

    /**
     * TODO->java-jwt依赖包创建方式：JWT.create().withAudience(user.getId()).sign(Algorithm.HMAC256(user.getPassword()));
     *
     * @param user
     * @return
     */
    private String getToken(TbUser user) {
        String token = Jwts
                .builder().claim("auth", new HashMap<>())
                .setId(String.valueOf(user.getId()))
                .signWith(SignatureAlgorithm.HS512, user.getPassword())
                .compact();
        return token;
    }

    /**
     * TODO->jjwt依赖包构建方式
     *
     * @param claims
     * @return
     */
    private String generateToken(Map<String, Object> claims) {
        String token = Jwts
                .builder()
                .setClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE_TIME))
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
        return token;
    }
}
