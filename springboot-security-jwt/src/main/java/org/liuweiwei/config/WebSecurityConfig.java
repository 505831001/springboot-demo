package org.liuweiwei.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.liuweiwei.web.JwtAuthTokenFilter;
import org.liuweiwei.web.VerifyCodeFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
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
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.Base64Utils;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * Spring Security 安全框架配置类。
 *     1.configure(AuthenticationManagerBuilder auth);
 *         auth.inMemoryAuthentication();
 *         auth.jdbcAuthentication();
 *         auth.ldapAuthentication();
 *         auth.userDetailsService(null);
 *         auth.authenticationProvider(null);
 *     2.configure(WebSecurity web);
 *         web.ignoring().antMatchers("/css/**", "/js/**", "/image/**", "/fonts/**", "/favicon.ico");
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
 * @since 2020-12-31
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
        PasswordEncoder encoder = NoOpPasswordEncoder.getInstance();
        encoder = new BCryptPasswordEncoder();
        return encoder;
    }

    /**
     * 被使用的默认实现{authenticationManager()}尝试获得{@link AuthenticationManager}。
     * 如果被重写，则{AuthenticationManagerBuilder}应用于指定{@link AuthenticationManager}。
     * 1.将【内存内身份】验证添加到{AuthenticationManagerBuilder}并返回{InMemoryUserDetailsManagerConfigurer}以允许自定义内存内身份验证。
     * auth.inMemoryAuthentication();
     * 2.将【JDBC身份】验证添加到{AuthenticationManagerBuilder}并返回{JdbcUserDetailsManagerConfigurer}以允许自定义JDBC身份验证。
     * auth.jdbcAuthentication();
     * 3.将【LDAP身份】验证添加到{AuthenticationManagerBuilder}并返回{LdapAuthenticationProviderConfigurer}以允许自定义LDAP身份验证。
     * auth.ldapAuthentication();
     * 4.根据传入的【自定义身份】{UserDetailsService}添加验证。然后返回一个{DaoAuthenticationConfigurer}，以允许自定义身份验证。
     * auth.userDetailsService(null);
     * 5.根据传入的【自定义身份】{AuthenticationProvider}添加验证。由于{AuthenticationProvider}实现未知，因此必须在外部完成所有自定义，并立即返回{AuthenticationManagerBuilder}。
     * auth.authenticationProvider(null);
     * @param auth
     * @throws Exception
     */
    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        String dbUsername = "admin";
        String dbPassword = passwordEncoder().encode("123456");
        String dbRoles    = "ADMIN,GUEST";
        auth.inMemoryAuthentication().withUser(dbUsername).password(dbPassword).roles(dbRoles);

        UserDetailsService userDetailsService = new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                log.info("[step 03] Http request username - {}", username);
                String password = dbPassword;
                log.info("[step 04] Http request username from DB password - {}", password);
                if (!org.springframework.util.StringUtils.isEmpty(username) && !StringUtils.isEmpty(password)) {
                    User user = new User(username, password, AuthorityUtils.commaSeparatedStringToAuthorityList(dbRoles));
                    log.info("[step 05] Spring security convert username - {}", user.getUsername());
                    log.info("[step 05] Spring security convert password - {}", user.getPassword());
                    log.info("[step 05] Spring security convert authored - {}", user.getAuthorities());
                    return user;
                }
                return null;
            }
        };
        auth.userDetailsService(userDetailsService);

        AuthenticationProvider authenticationProvider = new AuthenticationProvider() {
            @Override
            public Authentication authenticate(Authentication authentication) throws AuthenticationException {
                UsernamePasswordAuthenticationToken token = null;
                String httpUsername = (String) authentication.getPrincipal();
                log.info("[step 01] Http request username - {}", httpUsername);
                String httpPassword = (String) authentication.getCredentials();
                log.info("[step 02] Http request password - {}", httpPassword);
                String md5Password = DigestUtils.md5DigestAsHex(httpPassword.getBytes());
                log.info("[step 02] Http request password convert MD5 - {}", md5Password);
                String base64Password = Base64Utils.encodeToString(httpPassword.getBytes());
                log.info("[step 02] Http request password convert BASE64 - {}", base64Password);
                UserDetails userDetails = userDetailsService.loadUserByUsername(httpUsername);
                if (Objects.nonNull(userDetails)) {
                    String bCryptUser = userDetails.getUsername();
                    log.info("[step 06] Http request username convert BCrypt - {}", bCryptUser);
                    String bCryptPass = userDetails.getPassword();
                    log.info("[step 06] Http request password convert BCrypt - {}", bCryptPass);
                    log.info("[step 07] BCrypt http request password with spring security password - {}", passwordEncoder().matches(dbPassword, bCryptPass));
                    log.info("[step 07] String http request password with spring security password - {}", dbPassword.equals(bCryptPass));
                    token = new UsernamePasswordAuthenticationToken(bCryptUser, bCryptPass, userDetails.getAuthorities());
                    log.info("[step 08] Http request username convert BCrypt to Token - {}", token.getPrincipal());
                    log.info("[step 08] Http request password convert BCrypt to Token - {}", token.getCredentials());
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
        auth.authenticationProvider(authenticationProvider);
    }

    /**
     * 重写此方法以配置{@link WebSecurity}。例如，如果您希望忽略某些请求。
     * @param web
     * @throws Exception
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/css/**", "/js/**", "/image/**", "/fonts/**", "/favicon.ico");
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
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
            //外挂丝袜哥。{AntPathMatcher}蚂蚁路径请求匹配器。指定任何人都允许使用此URL。
            .antMatchers("/swagger-ui.html").permitAll()
            .antMatchers("/webjars/**").permitAll()
            .antMatchers("/swagger-resources/**").permitAll()
            .antMatchers("/v2/*").permitAll()
            .antMatchers("/csrf").permitAll()
            .antMatchers("/").permitAll()
            //外挂验证码。{AntPathMatcher}蚂蚁路径请求匹配器。指定任何人都允许使用此URL。
            .antMatchers("/getVerifyCode").permitAll()
            .antMatchers("/login/invalid").permitAll()
            .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
            .anyRequest().authenticated().and()
            //外挂过滤器。
            .addFilterBefore(new JwtAuthTokenFilter(), UsernamePasswordAuthenticationFilter.class)
            //外挂过滤器。
            .addFilterBefore(new VerifyCodeFilter(), UsernamePasswordAuthenticationFilter.class)
            //TODO -> 登录功能。
            .formLogin().loginPage("/loginPage").loginProcessingUrl("/authentication/form")
            //.defaultSuccessUrl("/successPage")
            .successHandler(new AuthenticationSuccessHandler() {
                @Override
                public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                    //校验通过后设置OAuth到请求头中
                    String      oauth = "712238f4321ea0ea5bfa3db0ca73a25e";
                    request.setAttribute("AuthorizationA", oauth);
                    request.getSession().setAttribute("AuthorizationB", oauth);
                    //校验通过后设置Token到响应头后再在控制器中从请求域中获取Token
                    String      token = "712238f4321ea0ea5bfa3db0ca73a25e";
                    String startsWith = "Bearer" + " ";
                    response.setHeader("AuthorizationX", startsWith + token);
                    response.addHeader("AuthorizationY", token);
                    //校验通过后设置Token到饼干中后再在控制器中从请求域中获取Token
                    response.addCookie(new Cookie("AuthorizationZ", token));
                    log.info("登录成功...");
                    response.setStatus(HttpStatus.OK.value());
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.setContentType("application/json;charset=utf-8");
                    response.getWriter().write(new ObjectMapper().writeValueAsString(authentication));
                    new DefaultRedirectStrategy().sendRedirect(request, response, "/successPage");
                }
            })
            .failureUrl("/failurePage").permitAll()
            //TODO -> 记住我功能。记住我参数。始终记得。令牌有效期秒数。持久令牌存储数据库。记住我功能整合{Authentication}身份验证功能必须加载用户详细信息服务。提示：会再次调用UserDetailsService。
            .and().rememberMe().rememberMeParameter("remember-me").alwaysRemember(true).tokenValiditySeconds(60).userDetailsService(userDetailsService())
            //TODO -> 会话功能。会话过期跳转Url。会话最大值(1)。会话最大值是否保留登录(否)。会话已过期重定向到Url。
            .and().sessionManagement().invalidSessionUrl("/login/invalid").maximumSessions(1).maxSessionsPreventsLogin(false).expiredUrl("/index").and()
            //TODO -> 退出功能。登出后跳转Url。删除饼干(JSESSIONID)。清除身份验证。失效Http会话。登出成功跳转Url。
            .and().logout().logoutUrl("/logout").deleteCookies("JSESSIONID").clearAuthentication(true).invalidateHttpSession(true).logoutSuccessUrl("/index")
            //TODO -> 异常功能。
            .and().exceptionHandling().accessDeniedPage("/errorPage")
            .and().csrf().disable().headers().frameOptions().disable();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}