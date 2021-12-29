package org.example;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * @author Liuweiwei
 * @since 2021-12-29
 */
@SpringBootApplication
@EnableWebMvc
@Slf4j
public class DemoSecurityLogin2Application {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(DemoSecurityLogin2Application.class, args);
        ConfigurableEnvironment environment = context.getEnvironment();
        log.info(environment.getProperty("java.vendor.url"));
        log.info(environment.getProperty("java.vendor.url.bug"));
        log.info(environment.getProperty("sun.java.command") + " started successfully.");
    }
}
