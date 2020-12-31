package com.liuweiwei.config;

import com.liuweiwei.component.Jwt01UserDetailsServiceImpl;
import com.liuweiwei.component.Jwt02AuthenticationProviderImpl;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@Slf4j
@Log4j
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebSecurityConfig.class);

    @Autowired
    private Jwt01UserDetailsServiceImpl userDetailsServiceImpl;
    @Autowired
    private Jwt02AuthenticationProviderImpl authenticationProviderImpl;

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
                .antMatchers("/user/**").hasRole("guest")
                .anyRequest().authenticated()
                .and();
    }

    protected void configure01(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication().passwordEncoder(passwordEncoder())
                .withUser("liuweiwei").password(passwordEncoder().encode("123456")).roles("admin");
        auth.inMemoryAuthentication().passwordEncoder(passwordEncoder())
                .withUser("linyiming").password(passwordEncoder().encode("123456")).roles("guest");
    }

    public void configure02(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsServiceImpl).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProviderImpl);
    }
}