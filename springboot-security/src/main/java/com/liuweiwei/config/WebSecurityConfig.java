package com.liuweiwei.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.liuweiwei.component.*;
import com.liuweiwei.service.TbUserService;
import com.liuweiwei.web.VerifyCodeFilter;
import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.session.InvalidSessionStrategy;
import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;
import org.springframework.util.Base64Utils;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * (01). Spring Security 安全框架配置类。
 *   [01].configure(AuthenticationManagerBuilder auth);使用被默认实现由{authenticationManager()}，尝试获取{AuthenticationManager}。如果被重写，则应使用{AuthenticationManagerBuilder}指定{AuthenticationManager}。
 *     {01}.auth.inMemoryAuthentication();     TODO->将【内存内身份】验证添加到{AuthenticationManagerBuilder}并返回{InMemoryUserDetailsManagerConfigurer}以允许自定义内存内身份验证。
 *     {02}.auth.jdbcAuthentication();         TODO->将【JDBC身份】验证添加到{AuthenticationManagerBuilder}并返回{JdbcUserDetailsManagerConfigurer}以允许自定义JDBC身份验证。
 *     {03}.auth.ldapAuthentication();         TODO->将【LDAP身份】验证添加到{AuthenticationManagerBuilder}并返回{LdapAuthenticationProviderConfigurer}以允许自定义LDAP身份验证。
 *     {04}.auth.userDetailsService(null);     TODO->根据传入的【自定义身份】{UserDetailsService}添加验证。然后返回一个{DaoAuthenticationConfigurer}，以允许自定义身份验证。根据传入的自定义添加身份验证。
 *     {05}.auth.authenticationProvider(null); TODO->根据传入的【自定义身份】{AuthenticationProvider}添加验证。由于{AuthenticationProvider}实现未知，因此必须在外部完成所有自定义，并立即返回{AuthenticationManagerBuilder}。
 *   [02].configure(WebSecurity web);重写此方法以配置{WebSecurity}。例如，如果您希望忽略某些请求。
 *     {01}.
 *     {02}.
 *   [03].configure(HttpSecurity http);重写此方法以配置{HttpSecurity}。通常，子类不应该通过调用super来调用此方法，因为它可能会覆盖它们的配置。默认配置为：
 *     {01}.http.formLogin()         TODO->登录功能。指定支持基于表单的身份验证。如果没有指定{loginPage(String)}，将生成一个默认的登录页面。
 *     {02}.http.rememberMe()        TODO->记住我功能。允许配置"记住我"身份验证。
 *     {03}.http.sessionManagement() TODO->Session功能。允许配置会话管理。
 *     {04}.http.logout()            TODO->退出功能。提供注销支持。
 *     {05}.http.exceptionHandling() TODO->异常处理。允许配置异常处理。
 * @author liuweiwei
 * @since 2020-05-20
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Log4j2
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Resource
    private DataSource dataSource;
    @Resource
    private TbUserService tbUserService;

    @Resource
    private My00PasswordEncoderImpl my00PasswordEncoderImpl;
    @Resource
    private My01UserDetailsServiceImpl my01UserDetailsServiceImpl;
    @Resource
    private My02AuthenticationProviderImpl my02AuthenticationProviderImpl;
    @Resource
    private My03AuthenticationSuccessHandler my03AuthenticationSuccessHandler;
    @Resource
    private My04AuthenticationFailureHandler my04AuthenticationFailureHandler;
    @Resource
    private My05InvalidSessionStrategy my05InvalidSessionStrategy;
    @Resource
    private My06SessionInformationExpiredStrategy my06SessionInformationExpiredStrategy;
    @Resource
    private My07LogoutSuccessHandler my07LogoutSuccessHandler;
    @Resource
    private My08AccessDeniedHandler my08AccessDeniedHandler;

    /**
     * NoOpPasswordEncoder.getInstance();
     * new BCryptPasswordEncoder();
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        PasswordEncoder encoder = NoOpPasswordEncoder.getInstance();
        encoder = new BCryptPasswordEncoder();
        return encoder;
    }

    /**
     * 使用的抽象类{PersistentTokenBasedRememberMeServices}为用户存储持久登录令牌。
     * @return
     */
    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);
        return tokenRepository;
    }

    /**
     * 被使用的默认实现{authenticationManager()}尝试获得{@link AuthenticationManager}。
     * 如果被重写，则{AuthenticationManagerBuilder}应用于指定{@link AuthenticationManager}。
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        String dbUsername = "admin";
        String dbPassword = passwordEncoder().encode("123456");
        String dbRoles    = "ADMIN";
        auth.inMemoryAuthentication().passwordEncoder(passwordEncoder()).withUser(dbUsername).password(dbPassword).roles(dbRoles);
        //TODO->1.将【内存内身份】验证添加到{AuthenticationManagerBuilder}并返回{InMemoryUserDetailsManagerConfigurer}以允许自定义内存内身份验证。
        //auth.inMemoryAuthentication();
        //TODO->2.将【JDBC身份】验证添加到{AuthenticationManagerBuilder}并返回{JdbcUserDetailsManagerConfigurer}以允许自定义JDBC身份验证。
        //auth.jdbcAuthentication();
        //TODO->3.将【LDAP身份】验证添加到{AuthenticationManagerBuilder}并返回{LdapAuthenticationProviderConfigurer}以允许自定义LDAP身份验证。
        //auth.ldapAuthentication();

        UserDetailsService userDetailsService = new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                log.info("【第三步】获取页面用户名称：" + username);
                String password = dbPassword;/*tbUserService.findPasswordByName(username);*/
                log.info("【第四步】通过页面用户名称查询数据库用户密码：" + password);
                if (!StringUtils.isEmpty(username) && !StringUtils.isEmpty(password)) {
                    User user = new User(username, password, AuthorityUtils.commaSeparatedStringToAuthorityList("ADMIN"));
                    log.info("【第五步】把页面用户名称和数据库用户密码设到安全框架User对象：" + user.toString());
                    return user;
                }
                return null;
            }
        };
        //TODO->4.根据传入的【自定义身份】{UserDetailsService}添加验证。然后返回一个{DaoAuthenticationConfigurer}，以允许自定义身份验证。
        auth.userDetailsService(userDetailsService).passwordEncoder(NoOpPasswordEncoder.getInstance());

        AuthenticationProvider authenticationProvider = new AuthenticationProvider() {
            @Override
            public Authentication authenticate(Authentication authentication) throws AuthenticationException {
                UserDetails details = null;
                UsernamePasswordAuthenticationToken token = null;
                String httpUsername = (String) authentication.getPrincipal();
                log.info("【第一步】获取页面用户名称：" + httpUsername);
                String httpPassword = (String) authentication.getCredentials();
                log.info("【第二步】获取页面用户密码String：" + httpPassword);
                String md5Password = DigestUtils.md5DigestAsHex(httpPassword.getBytes());
                log.info("【第二步】获取页面用户密码MD5：" + md5Password);
                String base64Password = Base64Utils.encodeToString(httpPassword.getBytes());
                log.info("【第二步】获取页面用户密码Base64：" + base64Password);
                details = userDetailsService.loadUserByUsername(httpUsername);
                if (Objects.nonNull(details)) {
                    log.info("【第六步】获取安全框架User对象用户：" + details.getUsername());
                    log.info("【第六步】获取安全框架User对象密码：" + details.getPassword());
                    log.info("【第六步】获取安全框架User对象权限：" + details.getAuthorities());
                    boolean matches = passwordEncoder().matches(dbPassword, details.getPassword());
                    log.info("【第七步】比较页面用户密码和安全框架密码a：" + matches);
                    boolean equals = dbPassword.equals(details.getPassword());
                    log.info("【第七步】比较页面用户密码和安全框架密码b：" + equals);
                    token = new UsernamePasswordAuthenticationToken(null, null, null);
                    token = new UsernamePasswordAuthenticationToken(details, dbPassword, details.getAuthorities());
                    log.info("【第八步】设置安全框架User对象到Token账号：" + token.getPrincipal());
                    log.info("【第八步】设置安全框架User对象到Token密码：" + token.getCredentials());
                    SecurityContextHolder.getContext().setAuthentication(token);
                    return token;
                }
                return null;
            }
            @Override
            public boolean supports(Class<?> authentication) {
                return true;
            }
        };
        //TODO->5.根据传入的【自定义身份】{AuthenticationProvider}添加验证。由于{AuthenticationProvider}实现未知，因此必须在外部完成所有自定义，并立即返回{AuthenticationManagerBuilder}。
        auth.authenticationProvider(authenticationProvider);
    }

    /**
     * 重写此方法以配置{@link WebSecurity}。例如，如果您希望忽略某些请求。
     * @param web
     * @throws Exception
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/css/**", "/js/**");
        /**
         * super.configure(web);
         */
    }

    /**
     * 重写此方法以配置{@link HttpSecurity}。
     * 通常，子类不应该通过调用super来调用此方法，因为它可能会覆盖它们的配置。
     * 默认配置为：http.authorizeRequests().anyRequest().authenticated().and().formLogin().and().httpBasic();
     * TODO -> 默认配置：指定支持基于表单的身份验证。如果未指定{loginPage(String)}，将生成默认登录页面。
     * http.authorizeRequests().anyRequest().authenticated().and().formLogin();
     * http.authorizeRequests().anyRequest().authenticated().and().httpBasic();
     * http.authorizeRequests().anyRequest().authenticated().and().formLogin().and().httpBasic();
     * http.authorizeRequests().anyRequest().authenticated().and().formLogin().and().httpBasic().and().csrf().disable();
     * http.authorizeRequests().anyRequest().authenticated().and().formLogin().and().httpBasic().and().csrf().disable().headers().frameOptions().disable();
     * TODO -> HttpSecurity.formLogin() 登录功能。
     * .formLogin() - 指定支持基于表单的身份验证。如果没有指定{loginPage(String)}，将生成一个默认的登录页面。
     *     .loginPage(String loginPage)                   - 指定用户需要登录时发送到的URL。
     *     .loginProcessingUrl(String loginProcessingUrl) - 指定验证凭据的URL。
     *     .defaultSuccessUrl(String defaultSuccessUrl)   - 指定用户在身份验证成功后将被重定向到何处，如果他们在身份验证之前没有访问一个安全页面。
     *     .failureUrl(String failureUrl).permitAll()     - 如果身份验证失败，发送给用户的URL。
     * TODO -> HttpSecurity.rememberMe() "记住我"功能。
     * .rememberMe() - 允许配置"记住我"身份验证。
     *      .rememberMeParameter("remember-me")           -
     *      .userDetailsService(userDetailsService())     -
     *      .tokenRepository(persistentTokenRepository()) -
     *      .tokenValiditySeconds(60)                     -
     * TODO -> HttpSecurity.sessionManagement() Session 功能。
     * .sessionManagement() - 允许配置会话管理。
     *      .invalidSessionStrategy()                     -
     *      .maximumSessions(1)                           -
     *      .maxSessionsPreventsLogin(false)              -
     *      .expiredSessionStrategy()                     -
     *      .sessionRegistry(new SessionRegistryImpl())   -
     * TODO -> HttpSecurity.logout() 退出功能。
     * .logout() - 提供注销支持。
     *      .logoutUrl("/logout")                         -
     *      .deleteCookies("JSESSIONID")                  -
     *      .clearAuthentication(true)                    -
     *      .invalidateHttpSession(true)                  -
     *      .addLogoutHandler()                           -
     *      .logoutSuccessHandler()                       -
     * TODO -> HttpSecurity.exceptionHandling() 异常处理。
     * .exceptionHandling() - 允许配置异常处理。
     *      .accessDeniedHandler()                        -
     * .csrf() - There was an unexpected error (type=forbidden,status=403).
     * .csrf() - Invalid CSRF Token 'null' was found on the request parameter '_csrf' or header 'X-CSRF-TOKEN'.
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        /*
        http.authorizeRequests().anyRequest().authenticated().and().formLogin()
            .loginPage("/loginPage")
            .loginProcessingUrl("/authentication/form")
            .defaultSuccessUrl("/successPage")
            .failureUrl("/failurePage").permitAll()
            .and().csrf().disable();
        */
        /*
        http.authorizeRequests()
            .antMatchers("/200").permitAll()
            .antMatchers("/400").permitAll()
            .antMatchers("/index").permitAll()
            .anyRequest().authenticated().and().formLogin()
            .loginPage("/loginPage")
            .loginProcessingUrl("/authentication/form")
            .defaultSuccessUrl("/successPage")
            .failureUrl("/failurePage").permitAll()
            .and().csrf().disable().headers().frameOptions().disable();
        */
        /*
        http.authorizeRequests()
            .antMatchers("/index").permitAll()
            .antMatchers("/admin/image").hasRole("ADMIN")
            .antMatchers("/guest/css").hasAnyRole("ADMIN", "GUEST")
            .anyRequest().authenticated().and().formLogin()
            .loginPage("/loginPage")
            .loginProcessingUrl("/authentication/form")
            .successHandler(new AuthenticationSuccessHandler() {
                @Override
                public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                    log.info("登录成功...");
                    response.setStatus(HttpStatus.OK.value());
                    response.setContentType("application/json;charset=utf-8");
                    response.getWriter().write(new ObjectMapper().writeValueAsString(authentication));
                    new DefaultRedirectStrategy().sendRedirect(request, response, "indexPage");
                }
            })
            .failureHandler(new AuthenticationFailureHandler() {
                @Override
                public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
                    log.info("登录失败...");
                    response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
                    response.setContentType("application/json;charset=utf-8");
                    response.getWriter().write(new ObjectMapper().writeValueAsString(exception.getMessage()));
                    new DefaultRedirectStrategy().sendRedirect(request, response, "failurePage");
                }
            }).permitAll()
            .and().csrf().disable().headers().frameOptions().disable();
        */

        http.authorizeRequests()
                //外挂验证码。{AntPathMatcher}蚂蚁路径请求匹配器。指定任何人都允许使用此URL。
                .antMatchers("/index").permitAll()
                .antMatchers("/login/invalid").permitAll()
                .antMatchers("/getVerifyCode").permitAll()
                .antMatchers("/admin/image").hasRole("ADMIN")
                .antMatchers("/guest/css").hasAnyRole("ADMIN", "GUEST")
                .anyRequest().authenticated()
                //外挂过滤器。
                .and()/*.addFilterBefore(new VerifyCodeFilter(), UsernamePasswordAuthenticationFilter.class)*/
                //TODO -> 指定支持基于表单的身份验证。如果未指定{@link loginPage(String)}，将生成默认登录页面。
                .formLogin()
                .loginPage("/loginPage")
                .loginProcessingUrl("/authentication/form")
                //.defaultSuccessUrl("/successPage")
                .successHandler(new AuthenticationSuccessHandler() {
                    @Override
                    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                        log.info("登录成功...");
                        response.setStatus(HttpStatus.OK.value());
                        response.setStatus(HttpServletResponse.SC_OK);
                        response.setContentType("application/json;charset=utf-8");
                        response.getWriter().write(new ObjectMapper().writeValueAsString(authentication));
                        new DefaultRedirectStrategy().sendRedirect(request, response, "/successPage");
                    }
                })
                //.failureUrl("/failurePage")
                .failureHandler(new AuthenticationFailureHandler() {
                    @Override
                    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
                        log.info("登录失败...");
                        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
                        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                        response.setContentType("application/json;charset=utf-8");
                        response.getWriter().write(new ObjectMapper().writeValueAsString(exception.getMessage()));
                        new DefaultRedirectStrategy().sendRedirect(request, response, "/failurePage");
                    }
                }).permitAll()
                //TODO -> 允许配置"记住我"身份验证。
                .and()
                .rememberMe()
                .rememberMeParameter("remember-me")
                .alwaysRemember(true)
                .userDetailsService(userDetailsService())
                //.tokenRepository(persistentTokenRepository())
                .tokenValiditySeconds(60)
                //TODO -> 允许配置会话管理。面的配置演示如何强制一次仅对用户的单个实例进行身份验证。
                //如果用户在未注销的情况下使用用户名"user"进行身份验证，并且尝试使用"user"进行身份验证，
                //则第一个会话将被强制终止并发送到"/login"expired URL。
                .and()
                .sessionManagement()
                //.invalidSessionUrl("/login/invalid")
                .invalidSessionStrategy(new InvalidSessionStrategy() {
                    @Override
                    public void onInvalidSessionDetected(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
                        log.info("失效的会话策略...");
                        RequestDispatcher dispatcher = request.getRequestDispatcher(request.getServletPath());
                        dispatcher.forward(request, response);
                    }
                })
                .maximumSessions(1)
                .maxSessionsPreventsLogin(false)
                //.expiredUrl("/indexPage")
                .expiredSessionStrategy(new SessionInformationExpiredStrategy() {
                    @Override
                    public void onExpiredSessionDetected(SessionInformationExpiredEvent event) throws IOException, ServletException {
                        log.info("过期的会话策略...");
                        HttpServletRequest     request = event.getRequest();
                        HttpServletResponse   response = event.getResponse();
                        SessionInformation information = event.getSessionInformation();
                        Map<String, Object> map = new HashMap<>();
                        map.put("code", 500);
                        map.put("message", "此账号在另一台设备上已登录，您被迫下线。");
                        map.put("x", information.isExpired());
                        map.put("a", information.getLastRequest());
                        map.put("b", information.getPrincipal());
                        map.put("c", information.getSessionId());
                        String json = new ObjectMapper().writeValueAsString(map);
                        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
                        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                        response.setContentType("application/json;charset=utf-8");
                        response.getWriter().write(json);
                    }
                })
                .sessionRegistry(new SessionRegistryImpl())
                //TODO -> 提供注销支持。这在使用时自动应用{@link WebSecurityConfigurerAdapter}。默认情况下，访问URL"/logout"将通过使HTTP会话无效而注销用户，
                //清理任何{@link rememberMe()}已配置的身份验证，
                //清理任何{@link SecurityContextHolder}，然后重定向到"/login"登录页面。
                .and()
                .and()
                .logout()
                .logoutUrl("/logout")
                .deleteCookies("JSESSIONID")
                .clearAuthentication(true)
                .invalidateHttpSession(true)
                //.logoutSuccessUrl("/indexPage")
                .addLogoutHandler(new LogoutHandler() {
                    @Override
                    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
                        response.setContentType("application/json;charset=UTF-8");
                        log.info("登录退出a...");
                    }
                })
                .logoutSuccessHandler(new LogoutSuccessHandler() {
                    @Override
                    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                        String username = ((User) authentication.getPrincipal()).getUsername();
                        log.info(username + "登录退出b...");
                        response.setStatus(HttpStatus.OK.value());
                        response.setStatus(HttpServletResponse.SC_OK);
                        response.setContentType("application/json;charset=utf-8");
                        PrintWriter out = response.getWriter();
                        out.write(new ObjectMapper().writeValueAsString(authentication));
                        out.flush();
                        out.close();
                        response.sendRedirect("/index");
                    }
                })
                //TODO -> 允许配置异常处理。这在使用{@link WebSecurityConfigurerAdapter}时自动应用。
                .and()
                .exceptionHandling()
                .accessDeniedHandler(new AccessDeniedHandler() {
                    @Override
                    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
                        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                        response.setContentType("application/json;charset=utf-8");
                        PrintWriter out = response.getWriter();
                        out.write(new ObjectMapper().writeValueAsString("权限不足，请联系管理员。"));
                        out.flush();
                        out.close();
                    }
                })
                //TODO -> 关闭一些乱七八糟的东西。
                .and().csrf().disable().headers().frameOptions().disable();

        /**
         * Caused by: java.lang.IllegalStateException: Can't configure anyRequest after itself.
         * super.configure(http);
         */
    }
}