package com.excel.poi.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Spring Security 安全框架配置类。
 *     1.configure(AuthenticationManagerBuilder auth);
 *         auth.inMemoryAuthentication();
 *         auth.jdbcAuthentication();
 *         auth.ldapAuthentication();
 *         auth.userDetailsService(null);
 *         auth.authenticationProvider(null);
 *     2.configure(WebSecurity web);
 *     3.configure(HttpSecurity http);
 *         http.authorizeRequests().anyRequest().authenticated()
 *             .and().openidLogin()
 *             .and().sessionManagement()
 *             .and().portMapper()
 *             .and().jee()
 *             .and().x509()
 *             .and().rememberMe()
 *             .and().requestCache()
 *             .and().exceptionHandling()
 *             .and().securityContext()
 *             .and().servletApi()
 *             .and().logout()
 *             .and().anonymous()
 *             .and().formLogin()
 *             .and().saml2Login()
 *             .and().oauth2Login()
 *             .and().oauth2Client()
 *             .and().oauth2ResourceServer()
 *             .and().requiresChannel()
 *             .and().httpBasic()
 *             .and().requestMatchers()
 *             .and().cors().disable().csrf().disable().headers().frameOptions().disable();
 * @author Liuweiwei
 * @since 2021-08-03
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Log4j2
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * 使用BCrypt强哈希函数的PasswordEncoder的实现。
     * 客户可以选择提供一个"版本"($2a, $2b, $2y)和一个"强度"(也称为BCrypt中的日志轮次)以及一个SecureRandom实例。
     * 强度参数越大，需要(以指数方式)对密码进行散列的工作就越多。默认值为10。
     * 加密方式：BCRYPT_PATTERN = Pattern.compile("\\A\\$2(a|y|b)?\\$(\\d\\d)\\$[./0-9A-Za-z]{53}");
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        encoder = NoOpPasswordEncoder.getInstance();
        return encoder;
    }

    /**
     * 被使用的默认实现{authenticationManager()}尝试获得{@link AuthenticationManager}。
     * 如果被重写，则{AuthenticationManagerBuilder}应用于指定{@link AuthenticationManager}。
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //将【内存内身份】验证添加到{AuthenticationManagerBuilder}并返回{InMemoryUserDetailsManagerConfigurer}以允许自定义内存内身份验证。
        auth.inMemoryAuthentication();
        //将【JDBC身份】验证添加到{AuthenticationManagerBuilder}并返回{JdbcUserDetailsManagerConfigurer}以允许自定义JDBC身份验证。
        auth.jdbcAuthentication();
        //将【LDAP身份】验证添加到{AuthenticationManagerBuilder}并返回{LdapAuthenticationProviderConfigurer}以允许自定义LDAP身份验证。
        auth.ldapAuthentication();
        //根据传入的【自定义身份】{UserDetailsService}添加验证。然后返回一个{DaoAuthenticationConfigurer}，以允许自定义身份验证。
        auth.userDetailsService(null);
        //根据传入的【自定义身份】{AuthenticationProvider}添加验证。由于{AuthenticationProvider}实现未知，因此必须在外部完成所有自定义，并立即返回{AuthenticationManagerBuilder}。
        auth.authenticationProvider(null);
        super.configure(auth);
    }

    /**
     * 重写此方法以配置{@link WebSecurity}。例如，如果您希望忽略某些请求。
     * @param web
     * @throws Exception
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        super.configure(web);
    }

    /**
     * 重写此方法以配置{@link HttpSecurity}。
     * 通常，子类不应该通过调用super来调用此方法，因为它可能会覆盖它们的配置。
     * 默认配置为：http.authorizeRequests().anyRequest().authenticated().and().formLogin().and().httpBasic();
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        /**
         * The default configuration is:
         * http.authorizeRequests().anyRequest().authenticated().and().formLogin().and().httpBasic();
         * TODO -> 默认配置：指定支持基于表单的身份验证。如果未指定{@link loginPage(String)}，将生成默认登录页面。
         */

        //http.authorizeRequests().anyRequest().authenticated().and().formLogin();
        //http.authorizeRequests().anyRequest().authenticated().and().httpBasic();
        //http.authorizeRequests().anyRequest().authenticated().and().formLogin().and().httpBasic();
        //http.authorizeRequests().anyRequest().authenticated().and().formLogin().and().httpBasic().and().csrf().disable();
        //http.authorizeRequests().anyRequest().authenticated().and().formLogin().and().httpBasic().and().csrf().disable().headers().frameOptions().disable();

        http.authorizeRequests().anyRequest().authenticated()
            .and().formLogin()
            .and().httpBasic()
            .and().rememberMe()
            .and().sessionManagement()
            .and().logout()
            .and().exceptionHandling()
            .and().cors().disable().csrf().disable().headers().frameOptions().disable();

        /**
         * Can't configure anyRequest after itself.
         * super.configure(http);
         */
    }




























}
