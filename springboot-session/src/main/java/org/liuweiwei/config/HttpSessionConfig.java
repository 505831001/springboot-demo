package org.liuweiwei.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

/**
 * 可选配置类(仅供展示，了解底层源码配置了啥)
 * 1.可以将Spring Session默认的Cookie Key从SESSION替换为原生的JSESSIONID。
 * 2.配置了相关的正则表达式，可以达到同父域下的单点登录的效果。
 * @author Administrator
 * @since 2021-08-28
 */
@EnableRedisHttpSession
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
}
