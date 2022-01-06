package org.example.config;

import lombok.extern.log4j.Log4j2;
import org.example.component.UserDetailsDto;
import org.example.component.UserDto;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
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
        String options = "userDetails";
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
                        String other = "user";
                        if ("user".equals(other)) {
                            return new User(dbUsername, dbPassword, dbAuthorities);
                        } else if ("userDto".equals(other)) {
                            return new UserDto(dbUsername, dbPassword, dbAuthorities);
                        } else if ("userDetailsDto".equals(other)) {
                            return UserDetailsDto.builder().username(dbUsername).password(dbPassword).authorities(dbAuthorities).build();
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

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/500").permitAll()
                .antMatchers("/404").permitAll()
                .antMatchers("/200").permitAll()
                .antMatchers("/login").permitAll()
                .antMatchers("/user/login").permitAll()
                .anyRequest().authenticated();

        String options = "first";
        switch (options) {
            case "first":
                //1-默认登录页面
                http.formLogin().successForwardUrl("/success").failureForwardUrl("/failure");
                break;
            case "second":
                //2-自定义登录页面
                http.formLogin().loginPage("/login").successForwardUrl("/success").failureForwardUrl("/failure");
                break;
            case "third":
                //3-默认登录页面
                http.formLogin().loginProcessingUrl("/authentication/form").successForwardUrl("/success").failureForwardUrl("/failure");
                break;
            case "fourth":
                //4-自定义登录页面
                http.formLogin().loginPage("/login").loginProcessingUrl("/authentication/form").successForwardUrl("/success").failureForwardUrl("/failure");
                break;
            default:
                break;
        }

        http.csrf().disable();
    }
}