package com.excel.poi.config;

import com.excel.poi.web.VerifyCodeFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
import org.springframework.security.core.GrantedAuthority;
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
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Collection;
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
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        String dbUsername = "admin";
        String dbPassword = passwordEncoder().encode("123456");
        String dbRoles    = "ADMIN";
        auth.inMemoryAuthentication().withUser(dbUsername).password(dbPassword).roles(dbRoles);

        UserDetailsService userDetailsService = new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                log.info("[step 03] Http request username - {}", username);
                String password = dbPassword;
                log.info("[step 04] Http request username from DB password - {}", password);
                if (!org.springframework.util.StringUtils.isEmpty(username) && !StringUtils.isEmpty(password)) {
                    User user = new User(username, password, AuthorityUtils.commaSeparatedStringToAuthorityList("ADMIN"));
                    log.info("[step 05] Spring security convert username - {}", user.getUsername());
                    log.info("[step 05] Spring security convert password - {}", user.getPassword());
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

        /**
         * Using generated security password: 40c9e5c2-cf56-488d-bb57-cb73756d1100
         * super.configure(auth);
         */
    }

    /**
     * 重写此方法以配置{@link WebSecurity}。例如，如果您希望忽略某些请求。
     * @param web
     * @throws Exception
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/css/**", "/js/**", "/image/**", "/fonts/**", "/favicon.ico");
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
            .anyRequest().authenticated()
            //外挂过滤器。
            .and().addFilterBefore(new VerifyCodeFilter(), UsernamePasswordAuthenticationFilter.class)
            //TODO -> 登录功能。
            .formLogin().loginPage("/loginPage").loginProcessingUrl("/authentication/form")
            //.defaultSuccessUrl("/successPage")
            .successHandler(new AuthenticationSuccessHandler() {
                @Override
                public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                    /**
                     * 一、http无状态协议
                     * 1.web应用采用Browser/Server架构，http作为通信协议。
                     * 2.http是无状态协议，浏览器的每一次请求，服务器会独立处理，不与之前或之后的请求产生关联，这个过程用下图说明，三次请求/响应对之间没有任何联系。
                     * 3.但这也同时意味着，任何用户都能通过浏览器访问服务器资源，如果想保护服务器的某些资源，必须限制浏览器请求。
                     * 4.要限制浏览器请求，必须鉴别浏览器请求，响应合法请求，忽略非法请求；要鉴别浏览器请求，必须清楚浏览器请求状态。
                     * 5.既然http协议无状态，那就让服务器和浏览器共同维护一个状态，这就是会话机制。
                     * 二、session会话机制
                     * 1.浏览器第一次请求服务器，服务器创建一个会话，并将会话的id(JSESSIONID)作为响应的一部分发送给浏览器。
                     * 2.浏览器存储会话id(JSESSIONID)，并在后续第二次和第三次请求中带上会话id(JSESSIONID)，服务器取得请求中的会话id(JSESSIONID)就知道是不是同一个用户了。
                     * 3.这个过程用下图说明，后续请求与第一次请求产生了关联。
                     * {
                     *     JSESSIONID=712238f4321ea0ea5bfa3db0ca73a25e
                     * }
                     * 三、登录状态
                     * 1.有了会话机制，登录状态就好明白了，我们假设浏览器第一次请求服务器需要输入用户名与密码验证身份。
                     * 2.服务器拿到用户名密码去数据库比对，正确的话说明当前持有这个会话的用户是合法用户。
                     * 3.应该将这个会话标记为"已授权"或者"已登录"等等之类的状态，既然是会话的状态，自然要保存在会话对象中，tomcat在会话对象中设置登录状态如下。
                     * {
                     *     HttpSession session = request.getSession();
                     *     session.setAttribute("Authorization", "712238f4321ea0ea5bfa3db0ca73a25e");
                     * }
                     * 4.用户再次访问时，tomcat在会话对象中查看登录状态。
                     * {
                     *     HttpSession session = request.getSession();
                     *     session.getAttribute("Authorization");
                     * }
                     * 5.每次请求受保护资源时都会检查会话对象中的登录状态，只有Authorization=712238f4321ea0ea5bfa3db0ca73a25e的会话才能访问，登录机制因此而实现。
                     * 四、多系统的复杂性
                     * 1.单系统登录解决方案的核心是cookie，cookie携带会话id在浏览器与服务器之间维护会话状态。
                     * 2.cookie是有限制的，这个限制就是cookie的域（通常对应网站的域名），浏览器发送http请求时会自动携带与该域匹配的cookie，而不是所有cookie。
                     * 3.因此，我们需要一种全新的登录方式来实现多系统应用群的登录，这就是单点登录。
                     * 五、单点登录
                     * 方法一：利用cookie的域实现
                     * 1.我们上面讲到，浏览器会设置cookie，cookie中保存中和服务器通信的session id。
                     * 2.但是cookie是有限制的，限制就是域（网站的域名），
                     * 3.浏览器发送http请求时会去匹配，当请求的地址和cookie的域相匹配时，就会把这个cookie携带，不匹配就不携带。
                     * 4.这样就好办了。比如我们有两个系统要做单点（~~），我们我们让这两个系统的域名保持一致不就行了，
                     * 5.比如A系统域名：www.myapp.A，B系统域名www.myapp.B设置的时候这是cookie的域为www.myapp，
                     * 6.那么访问A，B系统时都会携带这个cookie，也实现了单点。
                     * 7.以前的系统很多都是这么做单点登录的，优点就是简单方便，缺点就是不够动态，
                     * 8.首先域名要统一，其次，web服务器还得统一，tomcat是JSESSIONID，php，.net服务器就不是JSESSIONID了。
                     * 方法二：增加业务应用实现
                     * 1.还是有两个系统要做单点（系统A，B）,我们可以不用cookie来实现单点，增加一个系统注册应用（系统C）。
                     * 2.把系统A，B的功能模块全部注册到系统C中，同时关联账号，
                     * 3.系统C的 user1 关联系统A的 userA1，关联系统B的 userB1，同时登录的入口唯一，即只开放系统C的登录。
                     * 4.那么我们user1登录了系统C，同时要访问系统A的某个模块，我们就可以跳转到系统A，同时在系统A中创建userA1的session，
                     * 5.要访问系统B的某个模块，我们就可以跳转到系统B，同时在系统B中创建userB1的session，这样也可以实现单点。
                     * 6.优点就是比较适合后台管理类，权限多的应用。缺点就是相对复杂。
                     * 方法三：通过认证中心实现
                     * 1.sso-client
                     *     1.拦截子系统未登录用户请求，跳转至sso认证中心。
                     *     2.接收并存储sso认证中心发送的令牌。
                     *     3.与sso-server通信，校验令牌的有效性。
                     *     4.建立局部会话。
                     *     5.拦截用户注销请求，向sso认证中心发送注销请求。
                     *     6.接收sso认证中心发出的注销请求，销毁局部会话。
                     * 2.sso-server
                     *     1.验证用户的登录信息。
                     *     2.创建全局会话。
                     *     3.创建授权令牌。
                     *     4.与sso-client通信发送令牌。
                     *     5.校验sso-client令牌有效性。
                     *     6.系统注册。
                     *     7.接收sso-client注销请求，注销所有会话。
                     * 0.java web应用拦截请求的三种方式Servlet、Filter、Listener三种方式。
                     * 0.java web应用拦截请求的三种方式Filter、Interceptor、Aspect三种方式。
                     *
                     * 总结：
                     * 开始从http无状态协议，
                     * 再到Session会话机制，
                     * 然后单应用登录状态（这句话描述的好：这个会话标记为"已授权"或者"已登录"等等之类的状态”），
                     * 然后多应用同域名共享Session会话机制，此时已经是单点登录，
                     * 最后，单点登录技术就开始有认证中心OAuth2等实现方式鸟。
                     * 完美！！！
                     * TODO->[http无状态协议]->[session会话机制]->[登录状态]->[共享会话机制]->[sso单点登录]
                     *
                     */
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

        /**
         * Can't configure anyRequest after itself.
         * super.configure(http);
         */
    }
}
