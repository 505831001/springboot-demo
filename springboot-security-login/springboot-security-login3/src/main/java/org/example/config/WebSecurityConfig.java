package org.example.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

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
    public PasswordEncoder passwordEncoder(){
        return NoOpPasswordEncoder.getInstance();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        String options = "inMemory";
        switch (options) {
            case "inMemory":
                //1-将内存内身份验证添加到并返回以允许自定义内存内身份验证。
                auth.inMemoryAuthentication().withUser("user").password("123456").roles("ROLE_USER");
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

        String options = "first";
        /**
         * 1-默认页面请求："/login<GET>",默认login
         * 2-定义页面请求："/login.html<GET>",接收loginPage("/login.html")
         * 3-表单页面请求："/authentication/form<POST>",接收loginProcessingUrl("/authentication/form")
         */
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