package org.example.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.example.auth.BbcUserDetails;
import org.example.auth.AaaUser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
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
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * @author Liuweiwei
 * @since 2020-12-31
 */
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Configuration
@Log4j2
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        String options = "userDetails";
        switch (options) {
            case "inMemory":
                //1-将内存内身份验证添加到并返回以允许自定义内存内身份验证。
                auth.inMemoryAuthentication().withUser("user").password(bCryptPasswordEncoder().encode("123456")).roles("ROLE_USER");
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
                        List<GrantedAuthority> dbAuthorities = AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_GUEST");
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
            default:
                break;
        }
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        super.configure(web);
    }

    @Resource
    private ObjectMapper objectMapper;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        AuthenticationSuccessHandler authenticationSuccessHandler = new AuthenticationSuccessHandler() {
            @Override
            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                response.setContentType("application/json;charset=UTF-8");
                log.info("登录成功");
                PrintWriter out = response.getWriter();
                out.write(objectMapper.writeValueAsString("登录成功"));
                out.flush();
                out.close();
            }
        };
        AuthenticationFailureHandler authenticationFailureHandler = new AuthenticationFailureHandler() {
            @Override
            public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
                response.setContentType("application/json;charset=UTF-8");
                log.info("登录失败");
                PrintWriter out = response.getWriter();
                out.write(objectMapper.writeValueAsString("登录失败"));
                out.flush();
                out.close();
            }
        };
        LogoutSuccessHandler logoutSuccessHandler = new LogoutSuccessHandler() {
            @Override
            public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                response.sendRedirect("/login");
            }
        };
        AccessDeniedHandler accessDeniedHandler = new AccessDeniedHandler() {
            @Override
            public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException, ServletException {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.setCharacterEncoding("utf-8");
                response.sendRedirect("/403.html");
                response.getWriter().println("您无此权限");
            }
        };

        /**
         * 1-配置模式：hasAnyRole(),hasRole(),hasAnyAuthority(),hasAuthority()
         * 2-注解方式：@EnableGlobalMethodSecurity(prePostEnabled = true) + @PreAuthorize(value = "hasAnyAuthority('ROLE_USER')")
         */
        String requestFilter = "annotationMethod";
        switch (requestFilter) {
            case "configurationMode":
                http.authorizeRequests()
                        .antMatchers("/200", "/login", "/login.html").permitAll()
                        .antMatchers("/admin/index").hasAnyRole("ROLE_ADMIN", "ROLE_GUEST", "ROLE_USER")
                        .antMatchers("/guest/index").hasRole("ROLE_GUEST")
                        .antMatchers("/user/index").hasRole("ROLE_USER")
                        .anyRequest().authenticated();
                break;
            case "annotationMethod":
                http.authorizeRequests()
                        .antMatchers("/200", "/login", "/login.html").permitAll()
                        .anyRequest().authenticated();
                break;
            default:
                break;
        }

        /**
         * 1-默认页面请求："/login<GET>",默认login
         * 2-定义页面请求："/login.html<GET>",接收loginPage("/login.html")
         * 3-表单页面请求："/authentication/form<POST>",接收loginProcessingUrl("/authentication/form")
         */
        String options = "third";
        switch (options) {
            case "first":
                //1-两者都不配置：默认页面(login有效(包含其它路径),login.html有效),无表单形式
                http.formLogin().successForwardUrl("/success").failureForwardUrl("/failure");
                break;
            case "second":
                //2-只配置loginPage：定义页面(login无效,login.html有效(包含其它路径)),无表单形式
                http.formLogin().loginPage("/login").successForwardUrl("/success").failureForwardUrl("/failure");
                break;
            case "third":
                //3-只配置loginProcessingUrl：默认页面(login有效(包含其它路径),login.html有效),有表单形式
                http.formLogin().loginProcessingUrl("/authentication/form").successForwardUrl("/success").failureForwardUrl("/failure");
                break;
            case "fourth":
                //4-两者都配置：定义页面(login无效,login.html有效(包含其它路径)),有表单形式
                http.formLogin().loginPage("/login").loginProcessingUrl("/authentication/form").successForwardUrl("/success").failureForwardUrl("/failure");
                break;
            default:
                break;
        }

        //退出登录处理机制
        http.logout().permitAll().invalidateHttpSession(true).deleteCookies("JSESSIONID").logoutSuccessHandler(logoutSuccessHandler);

        //异常登录处理机制 -> 暂时先使用全局异常处理：org.example.component.GlobalExceptionConfig
        //http.exceptionHandling().accessDeniedHandler(accessDeniedHandler);

        //关闭跨域
        http.csrf().disable().sessionManagement().and().headers().frameOptions().sameOrigin();
    }
}