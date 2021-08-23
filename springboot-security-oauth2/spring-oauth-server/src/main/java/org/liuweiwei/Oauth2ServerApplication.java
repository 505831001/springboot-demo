package org.liuweiwei;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

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
public class Oauth2ServerApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(Oauth2ServerApplication.class);
    }
}
