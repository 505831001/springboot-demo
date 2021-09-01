package org.liuweiwei;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * @author Liuweiwei
 * @since 2020-12-23
 */
@SpringBootApplication
@EnableRedisHttpSession
@Log4j2
public class DemoSessionSsoClientApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(DemoSessionSsoClientApplication.class, args);
        ConfigurableEnvironment environment = context.getEnvironment();
        log.info(environment.getProperty("java.vendor.url"));
        log.info(environment.getProperty("java.vendor.url.bug"));
        log.info(environment.getProperty("sun.java.command") + " started successfully.");
    }
}
