package com.coder.springsecurity.plus.config;

import cn.hutool.core.util.StrUtil;
import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSON;
import com.coder.springsecurity.plus.auth.AaaUser;
import com.coder.springsecurity.plus.auth.BbcUserDetails;
import com.coder.springsecurity.plus.utils.Result;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

/**
 * @author LiuWeiWei
 * @since 2022-01-06
 */
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder(31);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        /**
         * 0-[登录认证]请求
         */
        String requestAuthentic = "inMemory";
        switch (requestAuthentic) {
            case "inMemory":
                //1-将内存内身份验证添加到并返回以允许自定义内存内身份验证。
                auth.inMemoryAuthentication().withUser("user").password(bCryptPasswordEncoder().encode("123456")).roles("USER");
                break;
            case "jdbc":
                //2-将【JDBC身份】验证添加到{}并返回{}以允许自定义JDBC身份验证。
                auth.jdbcAuthentication();
                break;
            case "ldap":
                //3-将【LDAP身份】验证添加到{}并返回{}以允许自定义LDAP身份验证。
                auth.ldapAuthentication();
                break;
            case "userDetails":
                //4-根据传入的自定义添加身份验证。然后返回一个，以允许自定义身份验证。
                auth.userDetailsService(new UserDetailsService() {
                    @Override
                    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                        String dbUsername = username;
                        System.out.println("Data身份验证提供者账号<username>：" + dbUsername);
                        String dbPassword = bCryptPasswordEncoder().encode("438438");
                        System.out.println("Data身份验证提供者密码<password>：" + dbPassword);
                        List<GrantedAuthority> dbAuthorities = AuthorityUtils.commaSeparatedStringToAuthorityList("GUEST");
                        System.out.println("Data身份验证提供者权限<authorities>：" + dbAuthorities);

                        String other = "user";
                        if ("user".equals(other)) {
                            return new User(dbUsername, dbPassword, dbAuthorities);
                        } else if ("userDto".equals(other)) {
                            return new AaaUser(dbUsername, dbPassword, dbAuthorities);
                        } else if ("userDetailsDto".equals(other)) {
                            return BbcUserDetails.builder().username(dbUsername).password(dbPassword).authorities(dbAuthorities).build();
                        }
                        return null;
                    }
                });
                break;
            case "authenticProvider":
                //5-根据传入的自定义添加身份验证。由于实现未知，因此必须在外部完成所有自定义，并立即返回。
                UserDetailsService userDetailsService = new UserDetailsService() {
                    @Override
                    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                        String dbUsername = username;
                        System.out.println("Data身份验证提供者账号<username>：" + dbUsername);
                        String dbPassword = bCryptPasswordEncoder().encode("12345678");
                        System.out.println("Data身份验证提供者密码<password>：" + dbPassword);
                        List<GrantedAuthority> dbAuthorities = AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_ADMIN");
                        System.out.println("Data身份验证提供者权限<authorities>：" + dbAuthorities);

                        String other = "user";
                        if ("user".equals(other)) {
                            return new User(dbUsername, dbPassword, dbAuthorities);
                        } else if ("userDto".equals(other)) {
                            return new AaaUser(dbUsername, dbPassword, dbAuthorities);
                        } else if ("userDetailsDto".equals(other)) {
                            return BbcUserDetails.builder().username(dbUsername).password(dbPassword).authorities(dbAuthorities).build();
                        }
                        return null;
                    }
                };
                auth.authenticationProvider(new AuthenticationProvider() {
                    @Override
                    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
                        Object principal = authentication.getPrincipal();
                        System.out.println("Http身份验证提供者账号<principal>：" + principal);
                        Object credentials = authentication.getCredentials();
                        System.out.println("Http身份验证提供者密码<credentials>：" + credentials);

                        UserDetails userDetails = userDetailsService.loadUserByUsername((String) principal);
                        String dbUsername = userDetails.getUsername();
                        System.out.println("Data身份验证提供者账号<username>：" + dbUsername);
                        String dbPassword = userDetails.getPassword();
                        System.out.println("Data身份验证提供者密码<password>：" + dbPassword);
                        Collection<? extends GrantedAuthority> dbAuthorities = userDetails.getAuthorities();
                        System.out.println("Data身份验证提供者权限<authorities>：" + dbAuthorities);

                        if (StringUtils.isNotEmpty((String) credentials)) {
                            boolean matches = bCryptPasswordEncoder().matches((String) credentials, dbPassword);
                            System.out.println("比较前端传入的明文密码和数据库中存储的密文密码是否相等：" + matches);
                            if (matches) {
                                return new UsernamePasswordAuthenticationToken(dbUsername, dbPassword, dbAuthorities);
                            }
                        }
                        return null;
                    }

                    @Override
                    public boolean supports(Class<?> authentication) {
                        return authentication.equals(UsernamePasswordAuthenticationToken.class);
                    }
                });
                break;
            default:
                break;
        }
    }

    //-------------------- 华丽的分割线 --------------------


    @Override
    public void configure(WebSecurity web) throws Exception {
        //放行静态资源
        web.ignoring()
                .antMatchers(HttpMethod.GET,
                        "/swagger-resources/**",
                        "/PearAdmin/**",
                        "/component/**",
                        "/admin/**",
                        "/**/*.html",
                        "/**/*.css",
                        "/**/*.js",
                        "/swagger-ui.html",
                        "/webjars/**",
                        "/v2/**",
                        "/druid/**");
    }

    //-------------------- 华丽的分割线 --------------------

    private String defaultFilterProcessUrl = "/login";
    private String method = "POST";

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        OncePerRequestFilter oncePerRequestFilter = new OncePerRequestFilter() {
            @Override
            protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
                if (method.equalsIgnoreCase(request.getMethod()) && defaultFilterProcessUrl.equals(request.getServletPath())) {
                    // 登录请求校验验证码，非登录请求不用校验
                    HttpSession session = request.getSession();
                    String requestCaptcha = request.getParameter("captcha");
                    //验证码的信息存放在seesion种，具体看EasyCaptcha官方解释
                    String genCaptcha = (String) request.getSession().getAttribute("captcha");
                    response.setContentType("application/json;charset=UTF-8");
                    if (StrUtil.isEmpty(requestCaptcha)) {
                        //删除缓存里的验证码信息
                        session.removeAttribute("captcha");
                        response.getWriter().write(JSON.toJSONString(Result.error().message("验证码不能为空!")));
                        return;
                    }
                    if (StrUtil.isEmpty(genCaptcha)) {
                        response.getWriter().write(JSON.toJSONString(Result.error().message("验证码已失效!")));
                        return;
                    }
                    if (!StrUtil.equalsIgnoreCase(genCaptcha, requestCaptcha)) {
                        session.removeAttribute("captcha");
                        response.getWriter().write(JSON.toJSONString(Result.error().message("验证码错误!")));
                        return;
                    }
                }
                filterChain.doFilter(request, response);
            }
        };
        AuthenticationEntryPoint authenticationEntryPoint = new AuthenticationEntryPoint() {
            @Override
            public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
                response.setCharacterEncoding("UTF-8");
                response.setContentType("application/json");
                response.getWriter().println(JSON.toJSONString(Result.error().message("尚未登录，或者登录过期   " + authException.getMessage())));
                response.getWriter().flush();
            }
        };
        AuthenticationSuccessHandler authenticationSuccessHandler = new AuthenticationSuccessHandler() {
            @Override
            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                HttpSession session = request.getSession();
                //删除缓存里的验证码信息
                session.removeAttribute("captcha");
                Result result = Result.ok().message("登录成功");
                //修改编码格式
                response.setCharacterEncoding("utf-8");
                response.setContentType("application/json");
                //输出结果
                response.getWriter().write(JSON.toJSONString(result));
            }
        };
        AuthenticationFailureHandler authenticationFailureHandler = new AuthenticationFailureHandler() {
            @Override
            public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
                //修改编码格式
                response.setCharacterEncoding("utf-8");
                response.setContentType("application/json");
                if (exception instanceof BadCredentialsException) {
                    response.getWriter().write(JSON.toJSONString(Result.error().message("用户名或密码错误")));
                } else {
                    response.getWriter().write(JSON.toJSONString(Result.error().message(exception.getMessage())));
                }
            }
        };
        AccessDeniedHandler accessDeniedHandler = new AccessDeniedHandler() {
            @Override
            public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException, ServletException {
                response.setCharacterEncoding("UTF-8");
                response.setContentType("application/json");
                response.getWriter().println(JSONUtils.toJSONString(Result.error().message(e.getMessage())));
                response.getWriter().flush();
            }
        };

        //验证码
        http.addFilterBefore(oncePerRequestFilter, UsernamePasswordAuthenticationFilter.class);

        //未登陆时返回 JSON 格式的数据给前端
        http.httpBasic().authenticationEntryPoint(authenticationEntryPoint);

        //任何人都能访问这个请求
        http.authorizeRequests().antMatchers("/captcha").permitAll().anyRequest().authenticated();

        //登录页面不设限访问
        http.formLogin().loginPage("/login.html").loginProcessingUrl("/login").successHandler(authenticationSuccessHandler).failureHandler(authenticationFailureHandler).permitAll();

        //记住我功能
        http.rememberMe().rememberMeParameter("rememberme");

        //无权访问 JSON 格式的数据
        http.exceptionHandling().accessDeniedHandler(accessDeniedHandler);

        //关闭csrf
        http.csrf().disable();
        //禁用缓存
        http.headers().cacheControl();
        //防止iframe造成跨域
        http.headers().frameOptions().disable().and();
        //关闭跨站
        http.csrf().disable().sessionManagement().and().headers().frameOptions().sameOrigin();
    }
}
