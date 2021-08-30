package org.liuweiwei.component;

import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * java web 应用拦截请求的三种方式：
 * 1. 过滤器 - Filter
 * 2. 拦截器 - Interceptor
 * 3. 切面类 - Aspect
 * 总结：
 * 1. Filter 是 java web 里面的，肯定获取不到 spring 里面 Controller 的信息。
 * 2. Interceptor、Aspect 是和 spring 相关的，所以能获取到 Controller 的信息。
 *
 * @author Liuweiwei
 * @since 2021-03-23
 */
@Component
@Aspect
@Log4j2
public class Web03MvcAspect {

    @Around("execution(* org.liuweiwei.controller.HtmlController.*(..))")
    public Object proceedingJoinPoint(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        log.info("03 - Aspect 切片类：proceedingJoinPoint(proceedingJoinPoint) 方法。");
        Object object = proceedingJoinPoint.proceed();
        return object;
    }
}
