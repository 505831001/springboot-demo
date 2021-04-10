package com.liuweiwei.api;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * 1. 切片（类）
 * 2. 切入点（注解），在哪些方法上起作用？在什么时候起作用？
 * 3. 增强（方法），起作用时执行的业务逻辑。
 *
 * @author Liuweiwei
 * @since 2021-03-23
 */
@Component
@Aspect
@Slf4j
public class Web03MvcAspect {

    @Around("execution(* com.liuweiwei.controller.LoginController.*(..))")
    public Object proceedingJoinPoint(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        log.info("03 - Web Mvc 切片类：proceedingJoinPoint(proceedingJoinPoint) 方法。");
        Object object = proceedingJoinPoint.proceed();
        return object;
    }
}
