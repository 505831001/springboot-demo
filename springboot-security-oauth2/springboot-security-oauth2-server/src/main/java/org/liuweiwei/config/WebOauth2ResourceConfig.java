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

    /**
     * TODO->被保护的资源，简单的几个资源（都以/admin和/guest开头），只为测试。
     * 如果全部保护，配置如此：http.authorizeRequests().anyRequest().authenticated().and().csrf().disable();
     * 如果不设置会话管理器，那么在通过浏览器访问被保护的任何资源时，每次是不同的SessionID，并且将每次请求的历史都记录在OAuth2Authentication的details的中。
     * @param http
     * @throws Exception
     */
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.requestMatchers()
            .antMatchers("/admin/**")
            .antMatchers("/guest/**")
            .and()
            .authorizeRequests()
            .antMatchers(HttpMethod.GET,    "/**").access("#oauth2.hasScope('read')")
            .antMatchers(HttpMethod.POST,   "/**").access("#oauth2.hasScope('write')")
            .antMatchers(HttpMethod.PATCH,  "/**").access("#oauth2.hasScope('write')")
            .antMatchers(HttpMethod.PUT,    "/**").access("#oauth2.hasScope('write')")
            .antMatchers(HttpMethod.DELETE, "/**").access("#oauth2.hasScope('write')")
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
