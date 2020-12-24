package com.liuweiwei.config;

import com.liuweiwei.component.Jwt01UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author liuweiwei
 * @since 2020-05-20
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private Jwt01UserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .headers().frameOptions().disable()
                .and();
        http
                .formLogin()
                .loginPage("/loginPage")
                .loginProcessingUrl("/form")
                .defaultSuccessUrl("/indexPage")
                .failureUrl("/loginError")
                .permitAll()
                .and();
        http
                .authorizeRequests()
                .antMatchers( "/css/**", "/error404").permitAll()
                .antMatchers("/user/**").hasRole("USER")
                .anyRequest().authenticated()
                .and();
    }

    protected void configure01(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication().passwordEncoder(passwordEncoder())
                .withUser("liuweiwei").password(passwordEncoder().encode("123456")).roles("admin");
        auth.inMemoryAuthentication().passwordEncoder(passwordEncoder())
                .withUser("linyiming").password(passwordEncoder().encode("123456")).roles("guest");
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }
}