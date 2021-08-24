package org.liuweiwei.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

/**
 * OAuth2 资源服务器配置
 *
 * @author Liuweiwei
 * @since 2020-12-30
 */
@EnableResourceServer
@Configuration
@Log4j2
public class WebOauth2ResourceConfig extends ResourceServerConfigurerAdapter {

    /**
     * 指定当前资源的ID(必填项)
     * 底层代码：org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer
     * 属性默认值：String resourceId = "oauth2-resource"
     */
    private static final String RESOURCE_ID = "oauth2-resource";

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.resourceId(RESOURCE_ID).stateless(true);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
            .antMatchers(HttpMethod.GET,    "/**").access("#oauth2.hasScope('read')  and hasRole('ADMIN')")
            .antMatchers(HttpMethod.POST,   "/**").access("#oauth2.hasScope('write') and hasRole('ADMIN')")
            .antMatchers(HttpMethod.PATCH,  "/**").access("#oauth2.hasScope('write') and hasRole('ADMIN')")
            .antMatchers(HttpMethod.PUT,    "/**").access("#oauth2.hasScope('write') and hasRole('ADMIN')")
            .antMatchers(HttpMethod.DELETE, "/**").access("#oauth2.hasScope('write') and hasRole('ADMIN')")
            .antMatchers("/admin/**").hasRole("admin")
            .antMatchers("/guest/**").hasRole("guest")
            .anyRequest().authenticated()
            .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
            .and()
            .headers()
            .addHeaderWriter((request, response) -> {
                response.addHeader("Access-Control-Allow-Origin", "*");
                if ("OPTIONS".equals(request.getMethod())) {
                    response.setHeader("Access-Control-Allow-Methods", request.getHeader("Access-Control-Request-Method"));
                    response.setHeader("Access-Control-Allow-Headers", request.getHeader("Access-Control-Request-Headers"));
                }
            });
    }
}