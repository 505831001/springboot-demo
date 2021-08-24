package org.liuweiwei;

import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * OAuth2 资源服务器
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
