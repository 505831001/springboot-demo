package org.example;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * http://localhost:8283/swagger-ui/
 *
 * @author Administrator
 * @since 2021-09-11
 */
@SpringBootApplication
@Log4j2
public class DemoSwagger3Application {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(DemoSwagger3Application.class, args);
        ConfigurableEnvironment environment = context.getEnvironment();
        log.info(environment.getProperty("java.vendor.url"));
        log.info(environment.getProperty("java.vendor.url.bug"));
        log.info(environment.getProperty("sun.java.command") + " started successfully.");
    }
}
