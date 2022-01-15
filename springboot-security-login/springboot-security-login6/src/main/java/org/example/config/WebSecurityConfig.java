package org.example.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.example.auth.AaaUser;
import org.example.auth.BbcUserDetails;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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

import java.util.Collection;
import java.util.List;

/**
 * @author Liuweiwei
 * @since 2020-12-31
 */
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Configuration
@Slf4j
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
        String requestAuthentic = "authenticProvider";
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

    @Override
    public void configure(WebSecurity web) throws Exception {
        super.configure(web);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        /**
         * 0-[登录过滤]请求
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
         * 0-[登录跳转]请求
         * 1-默认页面请求："/login<GET>",默认login
         * 2-定义页面请求："/login.html<GET>",接收loginPage("/login.html")
         * 3-表单页面请求："/authentication/form<POST>",接收loginProcessingUrl("/authentication/form")
         */
        String requestJump = "third";
        switch (requestJump) {
            case "first":
                //1-两者都不配置：默认页面(login有效(包含其它路径),login.html有效),无表单形式
                http.formLogin().successForwardUrl("/success.html").failureForwardUrl("/failure.html");
                break;
            case "second":
                //2-只配置loginPage：定义页面(login无效,login.html有效(包含其它路径)),无表单形式
                http.formLogin().loginPage("/login.html").successForwardUrl("/success.html").failureForwardUrl("/failure.html");
                break;
            case "third":
                //3-只配置loginProcessingUrl：默认页面(login有效(包含其它路径),login.html有效),有表单形式
                http.formLogin().loginProcessingUrl("/authentication/form").successForwardUrl("/success.html").failureForwardUrl("/failure.html");
                break;
            case "fourth":
                //4-两者都配置：定义页面(login无效,login.html有效(包含其它路径)),有表单形式
                http.formLogin().loginPage("/login.html").loginProcessingUrl("/authentication/form").successForwardUrl("/success.html").failureForwardUrl("/failure.html");
                break;
            default:
                break;
        }

        //0-[退出登录]处理机制
        http.logout().permitAll().invalidateHttpSession(true).deleteCookies("JSESSIONID").logoutSuccessUrl("/index.html");

        //0-[异常登录]处理机制
        http.exceptionHandling().accessDeniedPage("/403.html");

        //0-[关闭跨域]
        http.csrf().disable().sessionManagement().and().headers().frameOptions().sameOrigin();
    }
}