package com.liuweiwei.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * @author Liuweiwei
 * @since 2020-12-23
 */
@Aspect
@Component
public class WebLogAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebLogAspect.class);

    @Pointcut("execution(public * com.liuweiwei.controller.*.*(..))")
    public void webLog() {

    }

    @Before("webLog()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {
        /**
         * 接收到请求，记录请求内容。
         */
        ServletRequestAttributes servlet = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = servlet.getRequest();

        /**
         * 记录下请求内容。
         */
        LOGGER.info("URL : "         + request.getRequestURL().toString());
        LOGGER.info("HTTP_METHOD : " + request.getMethod());
        LOGGER.info("IP : "          + request.getRemoteAddr());
        Enumeration<String> enu = request.getParameterNames();

        while (enu.hasMoreElements()) {
            String name = (String) enu.nextElement();
            LOGGER.info("name:{},value:{}", name, request.getParameter(name));
        }
    }

    @AfterReturning(returning = "ret", pointcut = " webLog()")
    public void doAfterReturning(Object ret) throws Throwable {
        /**
         * 处理完请求，返回内容。
         */
        LOGGER.info("RESPONSE : " + ret);
    }
}
