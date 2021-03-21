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
import org.springframework.security.config.annotation.web.builders.WebSecurity;
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
        return new BCryptPasswordEncoder();
    }

    /**
     * Used by the default implementation of {@link #authenticationManager()} to attempt to obtain an {AuthenticationManager}.
     * If overridden, the {AuthenticationManagerBuilder} should be used to specify the {AuthenticationManager}.
     *
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication().passwordEncoder(passwordEncoder()).withUser("admin").password("123456").roles("ADMIN");
        auth.inMemoryAuthentication().passwordEncoder(passwordEncoder()).withUser("guest").password("123456").roles("GUEST");

        // auth.userDetailsService(userDetailsServiceImpl).passwordEncoder(passwordEncoder());

        // auth.authenticationProvider(authenticationProviderImpl);
    }

    /**
     * Override this method to configure {@link WebSecurity}. For example, if you wish to ignore certain requests.
     *
     * @param web
     * @throws Exception
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        super.configure(web);
    }

    /**
     * Override this method to configure the {@link HttpSecurity}.
     * Typically subclasses should not invoke this method by calling super as it may override their configuration.
     * The default configuration is:
     *
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
            .antMatchers( "/css/**", "/error404").permitAll()
            .antMatchers("/user/**").hasRole("guest")
            .anyRequest()
            .authenticated()

            .and()
            .formLogin()
            .loginPage("/loginPage")
            .loginProcessingUrl("/form")
            .defaultSuccessUrl("/indexPage")
            .failureUrl("/loginError").permitAll();

        http.cors().disable()
            .csrf().disable()
            .headers().frameOptions().disable();
    }
}