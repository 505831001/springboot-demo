package org.liuweiwei.config;

import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;
import org.springframework.session.web.http.HeaderHttpSessionIdResolver;

/**
 * Spring Session 分布式Session机制可选择配置类，配置域名的时候可选用。
 * @author LiuWeiWei
 * @since 2021-08-30
 */
//@Configuration
public class HttpSessionConfig {

    //@Bean
    public CookieSerializer cookieSerializer() {
        DefaultCookieSerializer serializer = new DefaultCookieSerializer();
        //可以将Spring Session默认的Cookie Key从SESSION替换为原生的JSESSIONID
        serializer.setCookieName("JSESSIONID");
        //可以将Cookie Path设置为根路径
        serializer.setCookiePath("/");
        serializer.setCookieMaxAge(1800);
        //设置显式域名。这允许跨子域共享cookie。默认设置是使用当前域。
        serializer.setDomainName("example.com");
        //配置了相关的正则表达式，可以达到同父域下的单点登录的效果。无法同时设置domainName和domainNamePattern"。
        //serializer.setDomainNamePattern("^.+?\\.(\\w+\\.[a-z]+)$");
        serializer.setUseBase64Encoding(false);
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
