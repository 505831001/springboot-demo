package org.liuweiwei;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * @author Liuweiwei
 * @since 2020-12-23
 */
@SpringBootApplication
public class Oauth2ServerApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(Oauth2ServerApplication.class);
    }
}
