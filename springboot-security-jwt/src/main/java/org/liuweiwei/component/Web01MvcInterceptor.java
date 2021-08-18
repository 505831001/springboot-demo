package org.liuweiwei.component;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.liuweiwei.annotation.PassToken;
import org.liuweiwei.annotation.UserLoginToken;
import org.liuweiwei.entity.TbUser;
import org.liuweiwei.service.TbUserService;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * java web 应用拦截请求的三种方式：
 * 1. 过滤器 - Filter
 * 2. 拦截器 - Interceptor
 * 3. 切面类 - Aspect
 * 总结：
 * 1. Filter 是 java web 里面的，肯定获取不到 spring 里面 Controller 的信息。
 * 2. Interceptor、Aspect 是和 spring 相关的，所以能获取到 Controller 的信息。
 *
 * @author liuweiwei
 * @since 2020-05-20
 */
@Component
@Slf4j
public class Web01MvcInterceptor implements HandlerInterceptor {

    @Resource
    private TbUserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("01 - Interceptor 拦截器：preHandle(request, response, handler) 方法。");
        //从http请求头中取出token
        String token = request.getHeader("token");
        //如果不是映射到方法直接通过
        if(!(handler instanceof HandlerMethod)){
            return true;
        }
        HandlerMethod handlerMethod=(HandlerMethod)handler;
        Method method=handlerMethod.getMethod();
        //检查是否有PassToken注释，有则跳过认证
        if (method.isAnnotationPresent(PassToken.class)) {
            PassToken passToken = method.getAnnotation(PassToken.class);
            if (passToken.required()) {
                return true;
            }
        }
        //检查有没有需要用户权限的注解
        if (method.isAnnotationPresent(UserLoginToken.class)) {
            UserLoginToken userLoginToken = method.getAnnotation(UserLoginToken.class);
            if (userLoginToken.required()) {
                //执行认证
                if (token == null) {
                    throw new RuntimeException("未检测到token，请重新登录");
                }
                //获取token中username
                try {
                    Map<String, Object> claims2 = new HashMap<>(10);
                    TbUser user = userService.findByUsername("liuweiwei");
                    String authToken = Jwts
                            .builder()
                            .claim("token", claims2)
                            .setId(String.valueOf(user.getId()))
                            .signWith(SignatureAlgorithm.HS512, user.getPassword())
                            .compact();
                    String username = Jwts
                            .parser()
                            .setSigningKey("secret")
                            .parseClaimsJws(authToken)
                            .getBody()
                            .getSubject();
                } catch (ExpiredJwtException ex) {
                    throw new RuntimeException(ex.getMessage());
                }
                //验证用户是否存在
                TbUser user = userService.findByUsername("liuweiwei");
                if (user == null || Objects.isNull(user)) {
                    throw new RuntimeException("用户不存在，请重新登录");
                }
                //验证token
                try {
                    Map<String, Object> claims2 = new HashMap<>(10);
                    String authToken = Jwts
                            .builder()
                            .claim("token", claims2)
                            .setId(String.valueOf(user.getId()))
                            .signWith(SignatureAlgorithm.HS512, user.getPassword())
                            .compact();
                    Jws<Claims> jws = Jwts
                            .parser()
                            .setSigningKey("secret")
                            .parseClaimsJws(authToken);
                } catch (ExpiredJwtException ex) {
                    throw new RuntimeException(ex.getMessage());
                }
                return true;
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
