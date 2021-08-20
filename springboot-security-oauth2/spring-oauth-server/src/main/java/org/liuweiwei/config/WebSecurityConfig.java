package org.liuweiwei.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Objects;

/**
 * @author Liuweiwei
 * @since 2020-12-30
 */
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
@EnableWebSecurity
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * PasswordEncoder对象在OAuth2认证服务中要使用，提取放入IOC容器中
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        PasswordEncoder encoder = new PasswordEncoder() {
            @Override
            public String encode(CharSequence charSequence) {
                return charSequence.toString();
            }

            @Override
            public boolean matches(CharSequence charSequence, String string) {
                return Objects.equals(charSequence.toString(), string);
            }
        };
        encoder = NoOpPasswordEncoder.getInstance();
        encoder = new BCryptPasswordEncoder();
        return encoder;
    }
    /**
     * AuthenticationManager对象在OAuth2认证服务中要使用，提取放入IOC容器中
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        String dbUsername = "admin";
        String dbPassword = passwordEncoder().encode("123456");
        String dbRoles    = "ADMIN";
        auth.inMemoryAuthentication().withUser(dbUsername).password(dbPassword).roles(dbRoles);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        super.configure(web);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
            .antMatchers("/oauth/*").permitAll()
            .anyRequest().authenticated()
            .and().formLogin().loginProcessingUrl("/login").permitAll()
            .and().csrf().disable();
    }
}
