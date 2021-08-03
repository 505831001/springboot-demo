package com.excel.poi.config;

import com.excel.poi.api.Web01MvcInterceptor;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.async.CallableProcessingInterceptor;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.context.request.async.DeferredResultProcessingInterceptor;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * @author liuweiwei
 * @since 2021-02-28
 */
@Configuration
public class WebMvcAutoConfig extends WebMvcAutoConfiguration implements WebMvcConfigurer {

    @Resource
    private Web01MvcInterceptor mvcInterceptor;

    // --- ---

    /**
     * 添加解析器以支持自定义控制器方法参数类型。这不会覆盖解析处理程序方法参数的内置支持。要自定义参数解析的内置支持，请直接配置{RequestMappingHandlerAdapter}。
     * @param resolvers
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {

    }

    /**
     * 配置跨源请求处理。
     *   1. 允许跨域访问的路径。
     *   2. 允许跨域访问的源。
     *   3. 允许请求方法。
     *   4. 预检间隔时间。
     *   5. 允许头部设置。
     *   6. 是否发送cookie。
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("POST", "DELETE", "PUT", "GET", "OPTIONS")
                .maxAge(60 * 60 * 24 * 7)
                .allowedHeaders("*")
                .allowCredentials(true);
    }

    /**
     * 除了默认注册的{Converter}和{Formatter}之外，还要添加{Converter}和{Formatter}。
     * @param registry
     */
    @Override
    public void addFormatters(FormatterRegistry registry) {

    }

    /**
     * 添加SpringMVC生命周期拦截器，用于控制器方法调用和资源处理程序请求的预处理和后处理。可以注册拦截器以应用于所有请求，也可以将拦截器限制为URL模式的子集。
     *   同步请求拦截器：
     *   org.springframework.web.servlet.config.annotation.InterceptorRegistry
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(mvcInterceptor);
    }

    /**
     * 添加处理程序以服务静态资源，例如web应用程序根目录下特定位置的图像、js和css文件、类路径等。
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
        registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    /**
     * 添加处理程序以支持自定义控制器方法返回值类型。使用此选项不会覆盖处理返回值的内置支持。要自定义处理返回值的内置支持，请直接配置RequestMappingHandlerAdapter。
     * @param handlers
     */
    @Override
    public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> handlers) {

    }

    /**
     * 配置预先配置了响应状态代码和/或视图的简单自动控制器，以呈现响应主体。这在不需要自定义控制器逻辑的情况下非常有用，例如，呈现主页、执行简单的站点URL重定向、返回包含HTML内容的404状态、返回不包含任何内容的204状态等等。
     * @param registry
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {

    }

    // --- ---

    /**
     * 配置异步请求处理选项。
     *   异步请求拦截器：
     *   org.springframework.web.context.request.async.CallableProcessingInterceptor
     *   org.springframework.web.context.request.async.DeferredResultProcessingInterceptor
     * @param configurer
     */
    @Override
    public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
        configurer.registerCallableInterceptors(new CallableProcessingInterceptor() {
            @Override
            public <T> void preProcess(NativeWebRequest request, Callable<T> task) throws Exception {

            }
            @Override
            public <T> void postProcess(NativeWebRequest request, Callable<T> task, Object concurrentResult) throws Exception {

            }
            @Override
            public <T> void afterCompletion(NativeWebRequest request, Callable<T> task) throws Exception {

            }
        });
        configurer.registerDeferredResultInterceptors(new DeferredResultProcessingInterceptor() {
            @Override
            public <T> void preProcess(NativeWebRequest request, DeferredResult<T> deferredResult) throws Exception {

            }
            @Override
            public <T> void postProcess(NativeWebRequest request, DeferredResult<T> deferredResult, Object concurrentResult) throws Exception {

            }
            @Override
            public <T> void afterCompletion(NativeWebRequest request, DeferredResult<T> deferredResult) throws Exception {

            }
        });
    }

    /**
     * 配置内容协商选项。
     * @param configurer
     */
    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {

    }

    /**
     * 通过转发到Servlet容器的"默认Servlet"，配置处理程序以委托未处理的请求。这方面的一个常见用例是{DispatcherServlet}映射到"/"从而覆盖Servlet容器对静态资源的默认处理。
     * @param configurer
     */
    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {

    }

    /**
     * 配置异常解析程序。
     * 给定的列表一开始是空的。如果保留为空，框架将配置默认的解析程序集，请参阅{addDefaultHandlerExceptionResolver(List, ContentNegotiationManager)}。
     * 或者，如果列表中添加了任何异常解析程序，则应用程序将有效地接管并必须提供完全初始化的异常解析程序。
     * 或者，您可以使用{extendHandlerExceptionResolver(List)}，它允许您扩展或修改默认配置的异常解析程序列表。
     * @param resolvers
     */
    @Override
    public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> resolvers) {

    }

    /**
     * 将{HttpMessageConverter}配置为用于读取或写入请求或响应的主体。
     * 如果未添加转换器，则会注册转换器的默认列表。
     * 如果将转换器添加到列表中，将关闭默认转换器注册。
     * 为了简单地添加一个不影响默认注册的转换器，请考虑使用方法{extendMessageConverters(List)}。
     * @param converters
     */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {

    }

    /**
     * 帮助配置HandlerMappings路径匹配选项，例如尾部斜杠匹配、后缀注册、路径匹配器和路径帮助器。
     * 已配置的路径匹配器和路径帮助器实例共享用于：
     * @param configurer
     */
    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {

    }

    /**
     * 配置视图解析器，将从控制器返回的基于字符串的视图名称转换为具体的{org.springframework.web.servlet.View}实现，以便使用执行渲染。
     * @param registry
     */
    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {

    }
}
