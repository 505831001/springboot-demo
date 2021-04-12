package com.liuweiwei.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.liuweiwei.component.*;
import com.liuweiwei.service.TbUserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
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
 * 方法之一：configure(AuthenticationManagerBuilder auth) 方法。
 *     AuthenticationManager        接口。
 *     UserDetailsService           接口。
 *     AuthenticationProvider       接口。
 *     UsernamePasswordAuthenticationToken(principal, credentials, authorities) 实现类。
 * 方法之二：configure(WebSecurity web)                   方法。
 * 方法之三：configure(HttpSecurity http)                 方法。
 *     AuthenticationSuccessHandler 接口。
 *     AuthenticationFailureHandler 接口。
 *     PersistentTokenRepository    接口。
 *     LogoutSuccessHandler         接口。
 *     AccessDeniedHandler          接口。
 *
 * @author liuweiwei
 * @since 2020-05-20
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * 日志-实现层：log4j
     */
    private final Logger logger = Logger.getLogger(this.getClass());

    /**
     * 日志-实现层：logback
     */
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(WebMvcAutoConfig.class);

    @Autowired
    private DataSource dataSource;

    @Autowired
    private TbUserService tbUserService;

    @Autowired
    private My00PasswordEncoderImpl my00PasswordEncoderImpl;

    @Autowired
    private My01UserDetailsServiceImpl my01UserDetailsServiceImpl;

    @Autowired
    private My02AuthenticationProviderImpl my02AuthenticationProviderImpl;

    @Autowired
    private My03AuthenticationSuccessHandler my03AuthenticationSuccessHandler;

    @Autowired
    private My04AuthenticationFailureHandler my04AuthenticationFailureHandler;

    @Autowired
    private My05InvalidSessionStrategy my05InvalidSessionStrategy;

    @Autowired
    private My06SessionInformationExpiredStrategy my06SessionInformationExpiredStrategy;

    @Autowired
    private My07LogoutSuccessHandler my07LogoutSuccessHandler;

    @Autowired
    private My08AccessDeniedHandler my08AccessDeniedHandler;

    /**
     * NoOpPasswordEncoder.getInstance();
     * new BCryptPasswordEncoder();
     *
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        PasswordEncoder encoder = NoOpPasswordEncoder.getInstance();
        encoder = new BCryptPasswordEncoder();
        return encoder;
    }

    /**
     * The abstraction used by {PersistentTokenBasedRememberMeServices} to store the persistent login tokens for a user.
     *
     * @return
     */
    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);
        return tokenRepository;
    }

    /**
     * 1. Used by the default implementation of {@link #authenticationManager()} to attempt to obtain an {AuthenticationManager}.
     * 2. If overridden, the {AuthenticationManagerBuilder} should be used to specify the {AuthenticationManager}.
     *
     * new UserDetailsService() {};
     * new UserDetails() {};
     * new User();
     *
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        /**
         * 方式一：基于内存的认证。在内存中验证添加到{AuthenticationManagerBuilder}并返回一个{InMemoryUserDetailsManagerConfigurer}在内存中允许定制的身份验证。
         */
        String encode2 = this.passwordEncoder().encode("123456");
        String encode4 = this.passwordEncoder().encode("438438");
        log.info("Spring Security 安全框架加密后的密码2：" + encode2);
        log.info("Spring Security 安全框架加密后的密码4：" + encode4);
        boolean matches2 = this.passwordEncoder().matches("123456", encode2);
        boolean matches4 = "123456".equals(encode2);
        log.info("验证密码编码获得存储匹配太编码后提交的原始密码。返回true。存储密码本身是没有解码。" + matches2);
        log.info("验证密码编码获得存储匹配太编码后提交的原始密码。返回true。存储密码本身是没有解码。" + matches4);
        //auth.inMemoryAuthentication().withUser("admin").password(encode2).roles("ADMIN");
        //auth.inMemoryAuthentication().withUser("guest").password(encode2).roles("GUEST");

        /**
         * 方式二：添加身份验证基于自定义{UserDetailsService}传入。然后它返回一个{DaoAuthenticationConfigurer}允许定制的身份验证。
         */
        UserDetailsService userDetailsService = new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                log.info("【第三步】获取页面用户名称：" + username);
                String md5Password = tbUserService.findPasswordByName(username);
                log.info("【第四步】通过页面用户名称查询数据库用户密码：" + md5Password);
                if (!StringUtils.isEmpty(username) && !StringUtils.isEmpty(md5Password)) {
                    User user = new User(username, md5Password, AuthorityUtils.commaSeparatedStringToAuthorityList("ADMIN"));
                    log.info("【第五步】把页面用户名称和数据库用户密码设到安全框架Usre对象：" + user.toString());
                    return user;
                }
                return null;
            }
        };
        //auth.userDetailsService(userDetailsService).passwordEncoder(NoOpPasswordEncoder.getInstance());

        /**
         * 方式三：添加身份验证基于自定义{AuthenticationProvider}传入。自{AuthenticationProvider}实现未知,所有必须完成的定制外部和{AuthenticationManagerBuilder}立即返回。
         */
        AuthenticationProvider authenticationProvider = new AuthenticationProvider() {
            @Override
            public Authentication authenticate(Authentication authentication) throws AuthenticationException {
                UserDetails details = null;
                UsernamePasswordAuthenticationToken token = null;
                String username = (String) authentication.getPrincipal();
                log.info("【第一步】获取页面用户名称：" + username);
                String password = (String) authentication.getCredentials();
                log.info("【第二步】获取页面用户密码String：" + password);
                String md5Password = DigestUtils.md5DigestAsHex(password.getBytes());
                log.info("【第二步】获取页面用户密码MD5：" + md5Password);
                String base64Password = Base64Utils.encodeToString(password.getBytes());
                log.info("【第二步】获取页面用户密码Base64：" + base64Password);
                details = userDetailsService.loadUserByUsername(username);
                if (Objects.nonNull(details)) {
                    log.info("【第六步】获取安全框架User对象用户：" + details.getUsername());
                    log.info("【第六步】获取安全框架User对象密码：" + details.getPassword());
                    log.info("【第六步】获取安全框架User对象权限：" + details.getAuthorities());
                    boolean matches = new BCryptPasswordEncoder().matches(md5Password, details.getPassword());
                    log.info("【第七步】比较页面用户密码和安全框架密码a：" + matches);
                    boolean equals = md5Password.equals(details.getPassword());
                    log.info("【第七步】比较页面用户密码和安全框架密码b：" + equals);
                    token = new UsernamePasswordAuthenticationToken(null, null, null);
                    token = new UsernamePasswordAuthenticationToken(details, md5Password, details.getAuthorities());
                    log.info("【第八步】设置安全框架User对象到Token账号：" + token.getPrincipal());
                    log.info("【第八步】设置安全框架User对象到Token密码：" + token.getCredentials());
                    return token;
                }
                return null;
            }
            @Override
            public boolean supports(Class<?> authentication) {
                return true;
            }
        };
        auth.authenticationProvider(authenticationProvider);
    }

    /**
     * Override this method to configure {@link WebSecurity}. For example, if you wish to ignore certain requests.
     *
     * @param web
     * @throws Exception
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        super.configure(web);
    }

    /**
     * Override this method to configure the {@link HttpSecurity}.
     * Typically subclasses should not invoke this method by calling super as it may override their configuration.
     * The default configuration is:
     *
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        /**
         * .formLogin()                                   指定支持基于表单的身份验证。如果没有指定{FormLoginConfigurer#loginPage(String)}，将生成一个默认的登录页面。
         * .loginPage(String loginPage)                   指定用户需要登录时发送到的URL。如果与{WebSecurityConfigurerAdapter}一起使用，当没有指定此属性时，将生成一个默认的登录页面。
         * .loginProcessingUrl(String loginProcessingUrl) 指定验证凭据的URL。
         * .defaultSuccessUrl(String defaultSuccessUrl)   指定用户在身份验证成功后将被重定向到何处，如果他们在身份验证之前没有访问一个安全页面。这是调用{defaultSuccessUrl(String, boolean)}的快捷方式。
         * .failureUrl(String failureUrl).permitAll()     如果身份验证失败，发送给用户的URL。这是调用{failureHandler(AuthenticationFailureHandler)}的捷径。默认为："/login?error"。
         *
         * .csrf() - There was an unexpected error (type=forbidden,status=403).
         * .csrf() - Invalid CSRF Token 'null' was found on the request parameter '_csrf' or header 'X-CSRF-TOKEN'.
         */

        /*
        http
            .httpBasic()
            .and()
            .authorizeRequests()
            .anyRequest()
            .authenticated();
        */

        /*
        http
            .formLogin()
            .and()
            .authorizeRequests()
            .anyRequest()
            .authenticated();
        */

        /*
        http
            .authorizeRequests()
            .anyRequest()
            .authenticated()
            .and()
            .formLogin()
            .loginPage("/loginPage")
            .loginProcessingUrl("/authentication/form")
            .defaultSuccessUrl("/indexPage")
            .failureUrl("/failurePage").permitAll()
            .and()
            .csrf().disable();
        */

        /*
        http
            .authorizeRequests()
            .antMatchers("/403").permitAll()
            .antMatchers("/404").permitAll()
            .antMatchers("/500").permitAll()
            .antMatchers("/index").permitAll()
            .anyRequest()
            .authenticated()
            .and()
            .formLogin()
            .loginPage("/loginPage")
            .loginProcessingUrl("/authentication/form")
            .defaultSuccessUrl("/indexPage")
            .failureUrl("/failurePage").permitAll()
            .and()
            .csrf().disable()
            .headers().frameOptions().disable();
        */

        /*
        http
            .authorizeRequests()
            .antMatchers("/index").permitAll()
            .antMatchers("/admin/image").hasRole("ADMIN")
            .antMatchers("/guest/css").hasAnyRole("ADMIN", "GUEST")
            .anyRequest()
            .authenticated()

            .and()
            .formLogin()
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

            .and()
            .csrf().disable()
            .headers().frameOptions().disable();
        */

        /*
        http
            .authorizeRequests()
            .antMatchers("/index").permitAll()
            .antMatchers("/admin/image").hasRole("ADMIN")
            .antMatchers("/guest/css").hasAnyRole("ADMIN", "GUEST")
            .anyRequest()
            .authenticated()

            .and()
            .formLogin()
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

            .and()
            .logout()
            .logoutUrl("/logout")
            .clearAuthentication(true)
            .invalidateHttpSession(true)
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
                    response.setContentType("application/json;charset=UTF-8");
                    log.info("登录退出b...");
                }
            })

            .and()
            .csrf().disable()
            .headers().frameOptions().disable();
        */

        http
                .authorizeRequests()
                .antMatchers("/index").permitAll()
                .antMatchers("/login/invalid").permitAll()
                .antMatchers("/admin/image").hasRole("ADMIN")
                .antMatchers("/guest/css").hasAnyRole("ADMIN", "GUEST")
                .anyRequest()
                .authenticated()

                .and()
                .formLogin()
                .loginPage("/loginPage")
                .loginProcessingUrl("/authentication/form")
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

                .and()
                .rememberMe()
                .rememberMeParameter("remember-me")
                .userDetailsService(userDetailsService())
                .tokenRepository(persistentTokenRepository())
                .tokenValiditySeconds(60)

                // Session 超时
                /*
                .and()
                .sessionManagement()
                .invalidSessionUrl("/index")
                */

                // Session 限制最大登录数
                /*
                .and()
                .sessionManagement()
                .maximumSessions(1)
                .expiredUrl("/index")
                */

                // Session 限制最大登录数
                /*
                .and()
                .sessionManagement()
                .invalidSessionUrl("/index")
                .maximumSessions(1)
                .maxSessionsPreventsLogin(false)
                .expiredUrl("/index")
                */

                .and()
                .sessionManagement()
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

                .and()
                .and()
                .logout()
                .logoutUrl("/logout")
                .deleteCookies("JSESSIONID")
                .clearAuthentication(true)
                .invalidateHttpSession(true)
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

                .and()
                .csrf().disable()
                .headers().frameOptions().disable();
    }
}