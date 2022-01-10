package com.liuweiwei.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.liuweiwei.component.*;
import com.liuweiwei.service.TbUserService;
import com.liuweiwei.web.VerifyCodeFilter;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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

import javax.annotation.Resource;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * (01). Spring Security 安全框架配置类。
 *   [01].configure(AuthenticationManagerBuilder auth);使用被默认实现由{authenticationManager()}，尝试获取{AuthenticationManager}。如果被重写，则应使用{AuthenticationManagerBuilder}指定{AuthenticationManager}。
 *     {01}.auth.inMemoryAuthentication();     TODO->将【内存内身份】验证添加到{AuthenticationManagerBuilder}并返回{InMemoryUserDetailsManagerConfigurer}以允许自定义内存内身份验证。
 *     {02}.auth.jdbcAuthentication();         TODO->将【JDBC身份】验证添加到{AuthenticationManagerBuilder}并返回{JdbcUserDetailsManagerConfigurer}以允许自定义JDBC身份验证。
 *     {03}.auth.ldapAuthentication();         TODO->将【LDAP身份】验证添加到{AuthenticationManagerBuilder}并返回{LdapAuthenticationProviderConfigurer}以允许自定义LDAP身份验证。
 *     {04}.auth.userDetailsService(null);     TODO->根据传入的【自定义身份】{UserDetailsService}添加验证。然后返回一个{DaoAuthenticationConfigurer}，以允许自定义身份验证。根据传入的自定义添加身份验证。
 *     {05}.auth.authenticationProvider(null); TODO->根据传入的【自定义身份】{AuthenticationProvider}添加验证。由于{AuthenticationProvider}实现未知，因此必须在外部完成所有自定义，并立即返回{AuthenticationManagerBuilder}。
 *   [02].configure(WebSecurity web);重写此方法以配置{WebSecurity}。例如，如果您希望忽略某些请求。
 *     {01}.web.ignoring().antMatchers("/css/**", "/js/**", "/image/**", "/fonts/**", "/favicon.ico");
 *     {02}.web.ignoring().antMatchers("/css/**", "/js/**", "/image/**", "/fonts/**", "/favicon.ico");
 *   [03].configure(HttpSecurity http);重写此方法以配置{HttpSecurity}。通常，子类不应该通过调用super来调用此方法，因为它可能会覆盖它们的配置。默认配置为：
 *     {01}.http.formLogin()                   TODO->登录功能。指定支持基于表单的身份验证。如果没有指定{loginPage(String)}，将生成一个默认的登录页面。
 *     {02}.http.rememberMe()                  TODO->记住我功能。允许配置"记住我"身份验证。
 *     {03}.http.sessionManagement()           TODO->Session功能。允许配置会话管理。
 *     {04}.http.logout()                      TODO->退出功能。提供注销支持。
 *     {05}.http.exceptionHandling()           TODO->异常处理。允许配置异常处理。
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
    private AaaUser aaaUser;
    @Resource
    private BbcUserDetails bbcUserDetails;
    @Resource
    private CcdUserDetailsService ccdUserDetailsService;
    @Resource
    private DdsAuthenticationProvider ddsAuthenticationProvider;

    @Resource
    private My00PasswordEncoder my00PasswordEncoder;
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
     * TODO->1-明文密码不加密实现。
     * @Bean
     * public PasswordEncoder passwordEncoder(){
     *     return NoOpPasswordEncoder.getInstance();
     * }
     * TODO->2-使用BCrypt强哈希函数的PasswordEncoder的实现。
     * @return
     */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        String options = "authenticProvider";
        switch (options) {
            case "inMemory":
                //TODO->1-将【内存内身份】验证添加到{AuthenticationManagerBuilder}并返回{InMemoryUserDetailsManagerConfigurer}以允许自定义内存内身份验证。
                auth.inMemoryAuthentication().withUser("user").password(bCryptPasswordEncoder().encode("123456")).roles("USER");
                break;
            case "jdbc":
                //TODO->2-将【JDBC身份】验证添加到{AuthenticationManagerBuilder}并返回{JdbcUserDetailsManagerConfigurer}以允许自定义JDBC身份验证。
                auth.jdbcAuthentication();
                break;
            case "ldap":
                //TODO->3-将【LDAP身份】验证添加到{AuthenticationManagerBuilder}并返回{LdapAuthenticationProviderConfigurer}以允许自定义LDAP身份验证。
                auth.ldapAuthentication();
                break;
            case "userDetails":
                //TODO->4-根据传入的【自定义身份】{UserDetailsService}添加验证。然后返回一个{DaoAuthenticationConfigurer}，以允许自定义身份验证。根据传入的自定义添加身份验证。
                auth.userDetailsService(ccdUserDetailsService);
                break;
            case "authenticProvider":
                //TODO->5-根据传入的【自定义身份】{AuthenticationProvider}添加验证。由于{AuthenticationProvider}实现未知，因此必须在外部完成所有自定义，并立即返回{AuthenticationManagerBuilder}。
                auth.authenticationProvider(ddsAuthenticationProvider);
                break;
            default:
                break;
        }
        //super.configure(auth);
    }

    /**
     * 重写此方法以配置{@link WebSecurity}。例如，如果您希望忽略某些请求。
     * @param web
     * @throws Exception
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/css/**", "/js/**", "/image/**", "/fonts/**", "/favicon.ico");
        //super.configure(web);
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
        AuthenticationSuccessHandler authenticationSuccessHandler = new AuthenticationSuccessHandler() {
            @Override
            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                log.info("Login succeeded.");
                response.setStatus(HttpStatus.OK.value());
                response.setContentType("application/json;charset=utf-8");
                response.getWriter().write(new ObjectMapper().writeValueAsString(authentication));
                new DefaultRedirectStrategy().sendRedirect(request, response, "indexPage");
            }
        };
        AuthenticationFailureHandler authenticationFailureHandler = new AuthenticationFailureHandler() {
            @Override
            public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
                log.info("Login failed.");
                response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
                response.setContentType("application/json;charset=utf-8");
                response.getWriter().write(new ObjectMapper().writeValueAsString(exception.getMessage()));
                new DefaultRedirectStrategy().sendRedirect(request, response, "failurePage");
            }
        };

        AuthenticationSuccessHandler authenticationSuccessHandler2 = new AuthenticationSuccessHandler() {
            @Override
            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                //校验通过后设置OAuth到请求头中
                String oauth = "712238f4321ea0ea5bfa3db0ca73a25e";
                request.setAttribute("AuthorizationA", oauth);
                request.getSession().setAttribute("AuthorizationB", oauth);
                //校验通过后设置Token到响应头后再在控制器中从请求域中获取Token
                String token = "712238f4321ea0ea5bfa3db0ca73a25e";
                String startsWith = "Bearer" + " ";
                response.setHeader("AuthorizationX", startsWith + token);
                response.addHeader("AuthorizationY", token);
                //校验通过后设置Token到饼干中后再在控制器中从请求域中获取Token
                response.addCookie(new Cookie("AuthorizationZ", token));
                log.info("Login succeeded.");
                response.setStatus(HttpStatus.OK.value());
                response.setStatus(HttpServletResponse.SC_OK);
                response.setContentType("application/json;charset=utf-8");
                response.getWriter().write(new ObjectMapper().writeValueAsString(authentication));
                new DefaultRedirectStrategy().sendRedirect(request, response, "/successPage");
            }
        };
        AuthenticationFailureHandler authenticationFailureHandler2 = new AuthenticationFailureHandler() {
            @Override
            public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
                log.info("Login failed.");
                response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.setContentType("application/json;charset=utf-8");
                response.getWriter().write(new ObjectMapper().writeValueAsString(exception.getMessage()));
                new DefaultRedirectStrategy().sendRedirect(request, response, "/failurePage");
            }
        };
        InvalidSessionStrategy invalidSessionStrategy2 = new InvalidSessionStrategy() {
            @Override
            public void onInvalidSessionDetected(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
                log.info("失效的会话策略...");
                RequestDispatcher dispatcher = request.getRequestDispatcher(request.getServletPath());
                dispatcher.forward(request, response);
            }
        };
        SessionInformationExpiredStrategy sessionInformationExpiredStrategy2 = new SessionInformationExpiredStrategy() {
            @Override
            public void onExpiredSessionDetected(SessionInformationExpiredEvent event) throws IOException, ServletException {
                log.info("过期的会话策略...");
                HttpServletRequest request = event.getRequest();
                HttpServletResponse response = event.getResponse();
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
        };
        LogoutHandler logoutHandler2 = new LogoutHandler() {
            @Override
            public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
                response.setContentType("application/json;charset=UTF-8");
                log.info("Log in and out A.");
            }
        };
        LogoutSuccessHandler logoutSuccessHandler2 = new LogoutSuccessHandler() {
            @Override
            public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                String username = ((User) authentication.getPrincipal()).getUsername();
                log.info(username + "Log in and out B.");
                response.setStatus(HttpStatus.OK.value());
                response.setStatus(HttpServletResponse.SC_OK);
                response.setContentType("application/json;charset=utf-8");
                PrintWriter out = response.getWriter();
                out.write(new ObjectMapper().writeValueAsString(authentication));
                out.flush();
                out.close();
                response.sendRedirect("/index");
            }
        };
        AccessDeniedHandler accessDeniedHandler2 = new AccessDeniedHandler() {
            @Override
            public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.setContentType("application/json;charset=utf-8");
                PrintWriter out = response.getWriter();
                out.write(new ObjectMapper().writeValueAsString("权限不足，请联系管理员。"));
                out.flush();
                out.close();
            }
        };

        String url = "fourth";
        switch (url) {
            case "first":
                http.authorizeRequests()
                        .anyRequest().authenticated()
                        .and()
                        .formLogin()
                        .loginPage("/loginPage")
                        .loginProcessingUrl("/authentication/form")
                        .defaultSuccessUrl("/successPage")
                        .failureUrl("/failurePage")
                        .permitAll();
                break;
            case "second":
                http.authorizeRequests()
                        .antMatchers("/200", "/400", "/index").permitAll()
                        .anyRequest().authenticated()
                        .and()
                        .formLogin()
                        .loginPage("/loginPage")
                        .loginProcessingUrl("/authentication/form")
                        .successForwardUrl("/successPage")
                        .failureForwardUrl("/failurePage")
                        .permitAll();
                break;
            case "third":
                http.authorizeRequests()
                        .antMatchers("/index").permitAll()
                        .antMatchers("/admin/image").hasRole("ADMIN")
                        .antMatchers("/guest/css").hasAnyRole("ADMIN", "GUEST")
                        .anyRequest().authenticated().and().formLogin()
                        .loginPage("/loginPage")
                        .loginProcessingUrl("/authentication/form")
                        .successHandler(authenticationSuccessHandler)
                        .failureHandler(authenticationFailureHandler).permitAll();
                break;
            case "fourth":
                http.authorizeRequests()
                        //外挂验证码。{AntPathMatcher}蚂蚁路径请求匹配器。指定任何人都允许使用此URL。
                        .antMatchers("/index").permitAll()
                        .antMatchers("/login/invalid").permitAll()
                        .antMatchers("/getVerifyCode").permitAll()
                        .antMatchers("/admin/image").hasRole("ADMIN")
                        .antMatchers("/guest/css").hasAnyRole("ADMIN", "GUEST")
                        .anyRequest().authenticated()
                        //外挂过滤器。
                        .and().addFilterBefore(new VerifyCodeFilter(), UsernamePasswordAuthenticationFilter.class)
                        //TODO -> 指定支持基于表单的身份验证。如果未指定{@link loginPage(String)}，将生成默认登录页面。
                        .formLogin().loginPage("/loginPage").loginProcessingUrl("/authentication/form")
                        //.successForwardUrl("/successPage")
                        .successHandler(authenticationSuccessHandler2)
                        //.failureForwardUrl("/failurePage")
                        .failureHandler(authenticationFailureHandler2).permitAll()
                        //TODO -> 允许配置"记住我"身份验证。
                        .and().rememberMe().rememberMeParameter("remember-me").alwaysRemember(true).userDetailsService(userDetailsService())
                        //.tokenRepository(persistentTokenRepository())
                        .tokenValiditySeconds(60)
                        //TODO -> 允许配置会话管理。面的配置演示如何强制一次仅对用户的单个实例进行身份验证。
                        //如果用户在未注销的情况下使用用户名"user"进行身份验证，并且尝试使用"user"进行身份验证，
                        //则第一个会话将被强制终止并发送到"/login"expired URL。
                        .and()
                        .sessionManagement()
                        //.invalidSessionUrl("/login/invalid")
                        .invalidSessionStrategy(invalidSessionStrategy2).maximumSessions(1).maxSessionsPreventsLogin(false)
                        //.expiredUrl("/indexPage")
                        .expiredSessionStrategy(sessionInformationExpiredStrategy2).sessionRegistry(new SessionRegistryImpl())
                        //TODO -> 提供注销支持。这在使用时自动应用{@link WebSecurityConfigurerAdapter}。默认情况下，访问URL"/logout"将通过使HTTP会话无效而注销用户，
                        //清理任何{@link rememberMe()}已配置的身份验证，
                        //清理任何{@link SecurityContextHolder}，然后重定向到"/login"登录页面。
                        .and()
                        .and()
                        .logout().logoutUrl("/logout").deleteCookies("JSESSIONID").clearAuthentication(true).invalidateHttpSession(true)
                        //.logoutSuccessUrl("/indexPage")
                        .addLogoutHandler(logoutHandler2).logoutSuccessHandler(logoutSuccessHandler2)
                        //TODO -> 允许配置异常处理。这在使用{@link WebSecurityConfigurerAdapter}时自动应用。
                        .and().exceptionHandling().accessDeniedHandler(accessDeniedHandler2);
                break;
            default:
                break;
        }

        //TODO -> 关闭一些乱七八糟的东西。
        http.csrf().disable().headers().frameOptions().disable();
        http.csrf().disable().headers().frameOptions().disable();
        http.csrf().disable().headers().frameOptions().disable();
        //super.configure(http);
    }
}