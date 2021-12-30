package org.example.config;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
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
        String options = "authenticProvider";
        switch (options) {
            case "inMemory":
                //1-将内存内身份验证添加到并返回以允许自定义内存内身份验证。
                auth.inMemoryAuthentication().withUser("admin").password(bCryptPasswordEncoder().encode("123456")).roles("ADMIN");
                break;
            case "userDetails":
                //2-根据传入的自定义添加身份验证。然后返回一个，以允许自定义身份验证。
                auth.userDetailsService(new UserDetailsService() {
                    @Override
                    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                        String dbUsername = username;
                        System.out.println("用户详细信息服务<username>：" + dbUsername);
                        String dbPassword = bCryptPasswordEncoder().encode("438438");
                        System.out.println("用户详细信息服务<password>：" + dbPassword);
                        List<GrantedAuthority> dbAuthorities = AuthorityUtils.commaSeparatedStringToAuthorityList("ADMIN");
                        System.out.println("用户详细信息服务<authorities>：" + dbAuthorities);
                        return new User(dbUsername, dbPassword, dbAuthorities);
                    }
                });
                break;
            case "authenticProvider":
                //3-根据传入的自定义添加身份验证。由于实现未知，因此必须在外部完成所有自定义，并立即返回。
                UserDetailsService userDetailsService = new UserDetailsService() {
                    @Override
                    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                        String dbUsername = username;
                        System.out.println("用户详细信息服务<username>：" + dbUsername);
                        String dbPassword = bCryptPasswordEncoder().encode("12345678");
                        System.out.println("用户详细信息服务<password>：" + dbPassword);
                        List<GrantedAuthority> dbAuthorities = AuthorityUtils.commaSeparatedStringToAuthorityList("ADMIN");
                        System.out.println("用户详细信息服务<authorities>：" + dbAuthorities);
                        return new User(dbUsername, dbPassword, dbAuthorities);
                    }
                };
                auth.authenticationProvider(new AuthenticationProvider() {
                    @Override
                    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
                        Object principal = authentication.getPrincipal();
                        System.out.println("Http身份验证提供者姓名<principal>：" + principal);
                        Object credentials = authentication.getCredentials();
                        System.out.println("Http身份验证提供者密码<credentials>：" + credentials);

                        UserDetails userDetails = userDetailsService.loadUserByUsername((String) principal);
                        String dbUsername = userDetails.getUsername();
                        System.out.println("Data身份验证提供者姓名<username>：" + dbUsername);
                        String dbPassword = userDetails.getPassword();
                        System.out.println("Data身份验证提供者密码<password>：" + dbPassword);

                        if (StringUtils.isNotEmpty((String) credentials)) {
                            boolean matches = bCryptPasswordEncoder().matches((String) credentials, dbPassword);
                            System.out.println("比较前端传入的明文密码和数据库中存储的密文密码是否相等：" + matches);
                            if (matches) {
                                return new UsernamePasswordAuthenticationToken(dbUsername, dbPassword);
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
        http.authorizeRequests()
                .antMatchers("/500").permitAll()
                .antMatchers("/404").permitAll()
                .antMatchers("/200").permitAll()
                .antMatchers("/user/login").permitAll()
                .anyRequest()
                .authenticated()

                .and()
                .formLogin()
                .loginProcessingUrl("/authentication/form")
                .successForwardUrl("/success")
                .failureForwardUrl("/failure")

                .and()
                .csrf()
                .disable();
    }
}