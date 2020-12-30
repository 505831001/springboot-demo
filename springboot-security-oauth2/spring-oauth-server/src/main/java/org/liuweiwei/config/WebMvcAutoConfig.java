package org.liuweiwei.config;

import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Spring MVC 继承：WebMvcAutoConfiguration 和实现：WebMvcConfigurer。
 *
 * @author Liuweiwei
 * @since 2020-12-30
 */
@Configuration
public class WebMvcAutoConfig extends WebMvcAutoConfiguration implements WebMvcConfigurer {

}
