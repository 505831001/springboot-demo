package org.liuweiwei.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.annotation.web.http.RedisHttpSessionConfiguration;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

/**
 * 写法二：
 * public class HttpSessionConfig extends RedisHttpSessionConfiguration {
 *     Bean
 *     public CookieSerializer cookieSerializer() {
 *         DefaultCookieSerializer serializer = new DefaultCookieSerializer();
 *         return serializer;
 *     }
 * }
 * Spring Session 分布式Session机制可选择配置类，配置域名的时候可选用。
 *
 * @author LiuWeiWei
 * @since 2021-08-30
 */
@Configuration
public class HttpSessionConfig {

    @Bean
    public CookieSerializer cookieSerializer() {
        DefaultCookieSerializer serializer = new DefaultCookieSerializer();
        //设置Cookie的密钥(Cookie Key)从SESSION替换为原生的JSESSIONID。放默认值，仅供参考。
        serializer.setCookieName("SESSION");
        //设置Cookie的路径。默认情况下，使用来自{HttpServletRequest}的上下文路径。
        serializer.setCookiePath("/");
        //设置Cookie的maxAge属性。默认设置是在浏览器关闭时删除cookie。
        //serializer.setCookieMaxAge(60*60);
        //设置显式域名。这允许跨子域共享cookie。默认设置是使用当前域。
        serializer.setDomainName("example.com");
        //配置了相关的正则表达式，可以达到同父域下的单点登录的效果。无法同时设置domainName和domainNamePattern"。
        //serializer.setDomainNamePattern("^.+?\\.(\\w+\\.[a-z]+)$");
        //设置是否应使用cookie值的Base64编码。这对于支持RFC 6265建议对Cookie值使用Base64编码。
        serializer.setUseBase64Encoding(true);
        return serializer;
    }

    @Bean
    public RedisHttpSessionConfiguration redisHttpSessionConfiguration() {
        RedisHttpSessionConfiguration configuration = new RedisHttpSessionConfiguration();
        configuration.setCookieSerializer(cookieSerializer());
        return configuration;
    }
}
