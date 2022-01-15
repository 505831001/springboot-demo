package com.coder.springsecurity.config;

import com.coder.springsecurity.auth.AaaUser;
import com.coder.springsecurity.auth.BbcUserDetails;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
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
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
        return new BCryptPasswordEncoder();
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
        super.configure(web);
    }

    //-------------------- 华丽的分割线 --------------------

    @Resource
    private ObjectMapper objectMapper;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //登录过滤处理机制
        http.authorizeRequests()
                .antMatchers( "/login.html","/xadmin/**","/treetable-lay/**","/static/**")
                .permitAll()
                .anyRequest()
                .authenticated();

        //登录跳转处理机制
        http.formLogin()
                .loginPage("/login.html")
                .loginProcessingUrl("/login")
                .successHandler(new AuthenticationSuccessHandler() {
                    @Override
                    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                        response.setHeader("Access-Controller-Allow-Origin","*");
                        response.setHeader("Access-Controller-Allow-Methods","*");
                        response.setContentType("application/json;charset=UTF-8");
                        response.setStatus(HttpStatus.OK.value());
                        response.getWriter().write(objectMapper.writeValueAsString(authentication));
                    }
                })
                .and().logout()
                .permitAll()
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .logoutSuccessHandler(new LogoutSuccessHandler() {
                    @Override
                    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                        response.sendRedirect("/login");
                    }
                });

        //登录异常处理机制
        http.exceptionHandling().accessDeniedHandler(new AccessDeniedHandler() {
            @Override
            public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException, ServletException {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.setCharacterEncoding("utf-8");
                response.sendRedirect("/403.html");
                response.getWriter().println("您无权限");
            }
        });

        //关闭跨站
        http.csrf().disable().sessionManagement().and().headers().frameOptions().sameOrigin();
    }
}
