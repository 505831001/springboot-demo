package com.liuweiwei.controller;

import com.liuweiwei.service.TbLoginService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author Liuweiwei
 * @since 2020-12-23
 */
@RestController
@Slf4j
public class LoginController {

    @Autowired
    private TbLoginService loginService;

    /**
     * http://localhost:8088/login?username=liuweiwei&password=123456
     *
     * @param username
     * @param password
     * @return
     */
    @GetMapping("/login")
    public String login(@RequestParam(name = "username") @Valid String username,
                        @RequestParam(name = "password") @Valid String password) {
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            return null;
        }
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        //用户认证信息
        Subject subject = SecurityUtils.getSubject();
        try {
            //进行验证，这里可以捕获异常，然后返回对应信息
            subject.login(token);
            subject.checkRole("admin");
            subject.checkPermissions("insert", "delete", "update", "select");
        } catch (UnknownAccountException e) {
            log.error("用户名不存在！", e);
            return "用户名不存在！";
        } catch (AuthenticationException e) {
            log.error("账号或密码错误！", e);
            return "账号或密码错误！";
        } catch (AuthorizationException e) {
            log.error("没有权限！", e);
            return "没有权限！";
        }
        return "登录成功！";
    }

    @RequiresRoles("admin")
    @GetMapping("/admin")
    public String admin() {
        return "admin success!";
    }

    @RequiresPermissions("select")
    @GetMapping("/index")
    public String index() {
        return "index success!";
    }

    @RequiresPermissions("insert")
    @GetMapping("/add")
    public String add() {
        return "add success!";
    }
}