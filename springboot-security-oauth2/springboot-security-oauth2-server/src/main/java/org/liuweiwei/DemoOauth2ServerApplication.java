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
 * @author Liuweiwei
 * @since 2020-12-23
 */
@SpringBootApplication
@Log4j2
public class DemoOauth2ServerApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(DemoOauth2ServerApplication.class, args);
        ConfigurableEnvironment environment = context.getEnvironment();
        log.info(environment.getProperty("java.vendor.url"));
        log.info(environment.getProperty("java.vendor.url.bug"));
        log.info(environment.getProperty("sun.java.command") + " started successfully.");
    }
}
