package org.example.config;

import lombok.extern.log4j.Log4j2;
import org.example.component.BbcUserDetails;
import org.example.component.AaaUser;
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
                auth.inMemoryAuthentication().withUser("user").password(bCryptPasswordEncoder().encode("123456")).roles("ROLE_USER");
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
                        List<GrantedAuthority> dbAuthorities = AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_GUEST");
                        System.out.println("用户详细信息服务<authorities>：" + dbAuthorities);
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

    @Override
    protected void configure(HttpSecurity http) throws Exception {
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

        http.csrf().disable();
    }
}