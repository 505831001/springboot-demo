package org.liuweiwei.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Objects;

/**
 * @author Liuweiwei
 * @since 2020-12-30
 */
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
@EnableWebSecurity
@Configuration
@Log4j2
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * 注意：PasswordEncoder对象在OAuth2认证服务中要使用，提取放入IOC容器中
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        PasswordEncoder encoder = NoOpPasswordEncoder.getInstance();
        encoder = new BCryptPasswordEncoder();
        return encoder;
    }

    /**
     * 注意：AuthenticationManager对象在OAuth2认证服务中要使用，提取放入IOC容器中
     * @return
     * @throws Exception
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * 注意：在使用refresh_token刷新令牌的时候，需要在认证服务器上面设置
     * @return
     */
    @Bean
    @Override
    protected UserDetailsService userDetailsService() {
        return super.userDetailsService();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        String dbUsername = "admin";
        String dbPassword = passwordEncoder().encode("123456");
        String dbRoles    = "ADMIN,GUEST";
        log.info("Spring Security 安全框架用户名称:{}", dbUsername);
        log.info("Spring Security 安全框架用户密码:{}", dbPassword);
        log.info("Authorization:{}", new org.apache.tomcat.util.codec.binary.Base64().encodeAsString("my-client-1:12345678".getBytes()));
        log.info("Authorization:{}", java.util.Base64.getEncoder().encodeToString("my-client-1:12345678".getBytes()));
        auth.inMemoryAuthentication().withUser(dbUsername).password(dbPassword).roles(dbRoles);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/assets/**", "/css/**", "/images/**");
        web.ignoring().antMatchers("/css/**", "/js/**", "/plugins/**", "/favicon.ico");
    }

    /**
     * http.antMatcher() 说白了单个用它。测试过没鸟用。允许将{HttpSecurity}配置为仅在匹配提供的ANT模式时调用。如果需要更高级的配置，请考虑使用{requestMatchers()}。
     * http.requestMatchers() 说白了多个用它。允许指定此将在哪些{HttpServletRequest}实例上调用。如果只有一个是必需的，请考虑使用{antMatcher(String)}。
     * http.authorizeRequests() 说白了带角色用它。允许使用示例配置基于{HttpServletRequest}限制访问。最基本的示例是将所有URL配置为需要角色"ROLE_USER"。下面的配置要求对每个URL进行身份验证，并将授予用户"admin"和"user"访问权限。
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http/*.antMatcher("/oauth/**")*/
            .requestMatchers()
            .antMatchers("/oauth/**", "/login/**", "/logout/**")
            .and().authorizeRequests().anyRequest().authenticated()
            .and().formLogin().and().httpBasic()
            .and().csrf().disable();
    }
}
