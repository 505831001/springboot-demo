package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * @author Liuweiwei
 * @since 2021-01-10
 */
@SpringBootApplication
public class DemoJdbcApplication extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(DemoJdbcApplication.class, args);
    }
}
