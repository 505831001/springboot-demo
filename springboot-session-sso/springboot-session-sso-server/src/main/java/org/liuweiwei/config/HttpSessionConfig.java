package org.liuweiwei.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;
import org.springframework.session.web.http.HeaderHttpSessionIdResolver;

/**
 * @author LiuWeiWei
 * @since 2021-08-30
 */
@Configuration
public class HttpSessionConfig {

    @Bean
    public CookieSerializer cookieSerializer() {
        DefaultCookieSerializer serializer = new DefaultCookieSerializer();
        //可以将Spring Session默认的Cookie Key从SESSION替换为原生的JSESSIONID
        serializer.setCookieName("JSESSIONID");
        //可以将Cookie Path设置为根路径
        serializer.setCookiePath("/");
        //配置了相关的正则表达式，可以达到同父域下的单点登录的效果
        serializer.setDomainNamePattern("^.+?\\.(\\w+\\.[a-z]+)$");
        return serializer;
    }

    //@Bean
    public DefaultCookieSerializer defaultCookieSerializer() {
        DefaultCookieSerializer serializer = new DefaultCookieSerializer();
        serializer.setDomainName("sso.com");
        serializer.setCookieName("JSESSIONID");
        serializer.setCookieMaxAge(1800);
        serializer.setCookiePath("/");
        return serializer;
    }

    //@Bean
    public HeaderHttpSessionIdResolver httpSessionStrategy() {
        HeaderHttpSessionIdResolver resolver = new HeaderHttpSessionIdResolver("x-auth-token");
        return resolver;
    }
}
