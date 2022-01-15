package com.coder.springsecurity.plus.auth;

import com.alibaba.fastjson.JSON;
import com.coder.springsecurity.plus.utils.Result;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author LiuWeiWei
 * @since 2022-01-06
 */
@Component
public class MyAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        //修改编码格式
        httpServletResponse.setCharacterEncoding("utf-8");
        httpServletResponse.setContentType("application/json");

        if (e instanceof BadCredentialsException){
            httpServletResponse.getWriter().write(JSON.toJSONString(Result.error().message("用户名或密码错误")));
        }else {
            httpServletResponse.getWriter().write(JSON.toJSONString(Result.error().message(e.getMessage())));
        }

    }
}
