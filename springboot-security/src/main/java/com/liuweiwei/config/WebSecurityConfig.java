package com.liuweiwei.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.liuweiwei.component.*;
import com.liuweiwei.service.TbUserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
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
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
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
    private TbUserService tbUserService;

    @Autowired
    private My01UserDetailsServiceImpl my01UserDetailsService;

    @Autowired
    private My02AuthenticationProviderImpl my02AuthenticationProvider;

    @Autowired
    private My03AuthenticationSuccessHandler my03AuthenticationSuccessHandler;

    @Autowired
    private My04AuthenticationFailureHandler my04AuthenticationFailureHandler;

    @Autowired
    private My05LogoutSuccessHandler my05LogoutSuccessHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Used by the default implementation of {@link #authenticationManager()} to attempt to obtain an {AuthenticationManager}.
     * If overridden, the {AuthenticationManagerBuilder} should be used to specify the {AuthenticationManager}.
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
                String password = tbUserService.findPasswordByName(username);
                log.info("【第四步】通过页面用户名称查询数据库用户密码：" + password);
                if (!StringUtils.isEmpty(username) && !StringUtils.isEmpty(password)) {
                    User user = new User(username, password, AuthorityUtils.commaSeparatedStringToAuthorityList("ADMIN"));
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
                UserDetails userDetails = null;
                UsernamePasswordAuthenticationToken token = null;
                String username = (String) authentication.getPrincipal();
                log.info("【第一步】获取页面用户名称：" + username);
                String password = (String) authentication.getCredentials();
                log.info("【第二步】获取页面用户密码：" + password);
                userDetails = userDetailsService.loadUserByUsername(username);
                if (Objects.nonNull(userDetails)) {
                    log.info("【第六步】获取安全框架User对象：" + userDetails.toString());
                    boolean matches = new BCryptPasswordEncoder().matches(password, userDetails.getPassword());
                    log.info("【第七步】比较页面用户密码和安全框架密码a：" + matches);
                    boolean equals = password.equals(userDetails.getPassword());
                    log.info("【第七步】比较页面用户密码和安全框架密码b：" + equals);
                    token = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    log.info("【第八步】设置安全框架User对象到Token：" + token.toString());
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
         * .formLogin() 指定支持基于表单的身份验证。如果没有指定{FormLoginConfigurer#loginPage(String)}，将生成一个默认的登录页面。
         * .loginPage(String loginPage) 指定用户需要登录时发送到的URL。如果与{WebSecurityConfigurerAdapter}一起使用，当没有指定此属性时，将生成一个默认的登录页面。
         * .loginProcessingUrl(String loginProcessingUrl) 指定验证凭据的URL。
         * .defaultSuccessUrl(String defaultSuccessUrl) 指定用户在身份验证成功后将被重定向到何处，如果他们在身份验证之前没有访问一个安全页面。这是调用{defaultSuccessUrl(String, boolean)}的快捷方式。
         * .failureUrl(String failureUrl).permitAll() 如果身份验证失败，发送给用户的URL。这是调用{failureHandler(AuthenticationFailureHandler)}的捷径。默认为："/login?error"。
         */
        /*
        http
                .formLogin()
                .loginPage("/loginPage")
                .loginProcessingUrl("/authentication/form")
                .defaultSuccessUrl("/indexPage")
                .failureUrl("/failurePage").permitAll()

                .and()
                .authorizeRequests()
                .anyRequest()
                .authenticated()

                .and()
                .csrf().disable()
                .headers().frameOptions().disable();
        */

        /*
        http
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
                .authorizeRequests()
                .anyRequest()
                .authenticated()
                .and()
                .csrf().disable()
                .headers().frameOptions().disable();
        */

        http
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
                .authorizeRequests()
                .anyRequest()
                .authenticated()
                .and()
                .csrf().disable()
                .headers().frameOptions().disable();
    }
}