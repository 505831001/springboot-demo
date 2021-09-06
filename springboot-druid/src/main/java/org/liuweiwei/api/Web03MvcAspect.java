package org.liuweiwei.api;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections4.CollectionUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * java web 应用拦截请求的三种方式：
 * 1. 过滤器 - Filter
 * 2. 拦截器 - Interceptor
 * 3. 切面类 - Aspect
 * 总结：
 * 1. Filter 是 java web 里面的，肯定获取不到 spring 里面 Controller 的信息。
 * 2. Interceptor、Aspect 是和 spring 相关的，所以能获取到 Controller 的信息。
 *
 * 此实例：日志打印
 * @author Liuweiwei
 * @since 2021-03-23
 */
@Component
@Aspect
@Log4j2
public class Web03MvcAspect {

    @Around("execution(* org.liuweiwei.service.impl.*(..))")
    public Object proceedingJoinPoint(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        log.info("03 - Aspect 切片类程序连接点：proceedingJoinPoint(proceedingJoinPoint) 方法。");

        long            start = System.currentTimeMillis();
        Signature   signature = proceedingJoinPoint.getSignature();
        Object[]         args = proceedingJoinPoint.getArgs();
        String    shortString = signature.toShortString();
        List<Object> backArgs = new ArrayList<>(args != null ? args.length : 1);
        if (args != null || CollectionUtils.isNotEmpty(Arrays.asList(args))) {
            for (Object param : args) {
                if (param instanceof HttpServletRequest || param instanceof HttpServletResponse || param instanceof MultipartFile) {
                    continue;
                }
                backArgs.add(param);
            }
        }
        log.info("{} service input:{}", shortString, JSONObject.toJSONString(backArgs));
        Object proceed = null;
        try {
            proceed = proceedingJoinPoint.proceed();
        } catch (Exception ex) {
            if (ex instanceof SQLException) {
                log.error("mysql exception, service:{}, input:{}", shortString, JSONObject.toJSONString(backArgs), ex);
            }
            throw ex;
        }
        long executeTime = System.currentTimeMillis() - start;
        log.info("{} service output:{}, execute time:{} ms", shortString, JSONObject.toJSONString(proceed), executeTime);

        return proceed;
    }
}
