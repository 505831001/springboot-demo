package org.liuweiwei.component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.liuweiwei.annotation.PassToken;
import org.liuweiwei.annotation.UserLoginToken;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.*;

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

    private static final String dbUsername = "liuweiwei";
    private static final String dbPassword = Base64Utils.encodeToString("123456".getBytes());
    private static final String secret = dbPassword;
    private static final String key = secret;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("01 - Interceptor 拦截器：preHandle(request, response, handler) 方法。");
        //http请求头中获取Token
        String token = request.getHeader("Authorization");
        if (StringUtils.isEmpty(token)) {
            token = Jwts.builder()
                    .setClaims(new HashMap<>())
                    .setId(UUID.randomUUID().toString())
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setSubject(dbUsername)
                    .signWith(SignatureAlgorithm.HS256, key).compact();
            log.info("生成签证成功: {}", token);
        } else {
            log.info("签证不能为空: {}", HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
        //校验并解析token，如果token过期或者篡改，则会返回null
        //JWT签名与本地计算的签名不匹配。无法断言JWT有效性，不应信任JWT有效性。
        Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        if (Objects.isNull(claims)) {
            log.info("签证已失效，请重新登录: {}", HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
        //校验通过后设置用户到请求里后在控制器中从请求域中获取用户信息
        request.setAttribute("Authorization", claims);
        request.getSession().setAttribute("Authorization", claims);

        // --- 注解方式签证校验 ---

        //0.如果不是映射到方法直接通过
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        //0.检查是否有PassToken注释，有则跳过认证
        if (method.isAnnotationPresent(PassToken.class)) {
            PassToken passToken = method.getAnnotation(PassToken.class);
            if (passToken.required()) {
                return true;
            }
        }
        //0.检查有没有需要用户权限的注解
        if (method.isAnnotationPresent(UserLoginToken.class)) {
            UserLoginToken userLoginToken = method.getAnnotation(UserLoginToken.class);
            if (userLoginToken.required()) {
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
