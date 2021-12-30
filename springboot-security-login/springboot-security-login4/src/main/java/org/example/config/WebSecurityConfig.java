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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

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
        String options = "inMemory";
        switch (options) {
            case "inMemory":
                //1-将内存内身份验证添加到并返回以允许自定义内存内身份验证。
                auth.inMemoryAuthentication().withUser("admin").password(bCryptPasswordEncoder().encode("123456")).roles("ADMIN");
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