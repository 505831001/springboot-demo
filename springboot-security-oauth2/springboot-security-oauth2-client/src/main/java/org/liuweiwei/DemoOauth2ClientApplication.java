package org.liuweiwei;

import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * Spring Security + OAuth2.0 授权服务器。使用 OAuth2 密码授权方式提供令牌。
 *     1.允许内存、数据库、JWT等方式存储令牌。
 *     2.允许 JWT 方式验证令牌。
 *     3.允许从内存、数据库中读取客户端详情。
 * Spring Security + OAuth2.0 资源服务器。
 *     1.授权服务器负责生成并发放访问令牌access_token，客户端在访问受保护的资源时会带上访问令牌，资源服务器需要解析并验证客户端带的这个访问令牌。
 *     2.如果你的资源服务器同时也是一个授权服务器（资源服务器和授权服务器在一起），那么资源服务器就不需要考虑令牌解析的事情了，否则这一步是不可或缺的。
 *     3.同时，把它们放在一起的话还有一个问题需要注意，我们知道过滤器是顺序执行的，因此需要确保那些通过访问令牌来访问的资源路径不能被主过滤拦下了，需要独立出来。
 *
 * 1. Spring Security + OAuth2.0 授权服务器。使用 OAuth2 密码授权方式提供令牌。
 * TODO->第一步：获取授权码Code
 * http://localhost:9200/oauth/authorize?response_type=code&client_id=client&redirect_uri=http://localhost:9202/login
 * {
 *     http://localhost:9202/login?code=ocFg05
 * }
 * TODO->第二步：通过授权码Code获取令牌Token
 * http://localhost:9200/oauth/token?client_id=client&client_secret=secret&grant_type=authorization_code&redirect_uri=http://localhost:9202/login&code=ocFg05
 * {
 *     "access_token":"a85dd23a-b651-4974-8eeb-297e75f575c9",
 *     "token_type":"bearer",
 *     "refresh_token":"e0111c32-3534-4202-84d5-05b34fd42c59",
 *     "expires_in":1799,"scope":"read write"
 * }
 * 2. Spring Security + OAuth2.0 资源服务器。获取 OAuth2 授权码带令牌参数访问。
 * TODO->第三步：访问资源
 * http://localhost:9202/echo 未配置 OAuth2 资源服务器配置。因此只需Security拦截登录即可。
 * http://localhost:9202/admin/echo?access_token=a85dd23a-b651-4974-8eeb-297e75f575c9 未经授权：访问此资源需要完全身份验证。
 * http://localhost:9202/guest/echo?access_token=a85dd23a-b651-4974-8eeb-297e75f575c9 unauthorized: Full authentication is required to access this resource.
 *
 * @author Liuweiwei
 * @since 2020-12-23
 */
@SpringBootApplication
@Log4j2
public class DemoOauth2ClientApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(DemoOauth2ClientApplication.class, args);
        ConfigurableEnvironment environment = context.getEnvironment();
        log.info(environment.getProperty("java.vendor.url"));
        log.info(environment.getProperty("java.vendor.url.bug"));
        log.info(environment.getProperty("sun.java.command") + " started successfully.");
    }
}
