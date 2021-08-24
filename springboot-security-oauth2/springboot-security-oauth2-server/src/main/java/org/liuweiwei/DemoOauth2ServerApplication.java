package org.liuweiwei;

import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * Spring Security + OAuth2 授权服务器。使用 OAuth2 密码授权方式提供令牌。
 *     1. 允许内存、数据库、JWT等方式存储令牌。
 *     2. 允许 JWT 方式验证令牌。
 *     3. 允许从内存、数据库中读取客户端详情。
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
