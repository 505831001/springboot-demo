package org.liuweiwei.api;

import lombok.extern.log4j.Log4j2;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * @author Administrator
 * @since 2021-08-30
 */
@Component
@Log4j2
public class Web00MvcResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        log.info("00 - Resolver 解析器：supportsParameter(MethodParameter parameter) 方法。");
        return false;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        log.info("00 - Resolver 解析器：resolveArgument(parameter, mavContainer, webRequest, binderFactory) 方法。");
        return null;
    }
}
