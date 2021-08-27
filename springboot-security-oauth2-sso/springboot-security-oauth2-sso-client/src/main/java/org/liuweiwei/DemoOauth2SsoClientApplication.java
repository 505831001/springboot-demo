package org.liuweiwei;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.web.client.RestTemplate;

/**
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
public class DemoOauth2SsoClientApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(DemoOauth2SsoClientApplication.class, args);
        ConfigurableEnvironment environment = context.getEnvironment();
        log.info(environment.getProperty("java.vendor.url"));
        log.info(environment.getProperty("java.vendor.url.bug"));
        log.info(environment.getProperty("sun.java.command") + " started successfully.");
    }

    @LoadBalanced
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
