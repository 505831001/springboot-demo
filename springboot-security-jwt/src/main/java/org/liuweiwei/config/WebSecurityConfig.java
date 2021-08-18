package org.liuweiwei.config;

import lombok.extern.log4j.Log4j2;
import org.liuweiwei.web.JwtAuthTokenFilter;
import org.liuweiwei.web.VerifyCodeFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.Base64Utils;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import java.util.Objects;

/**
 * @author Liuweiwei
 * @since 2020-12-31
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Log4j2
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public PasswordEncoder passwordEncoder() {
        PasswordEncoder encoder = NoOpPasswordEncoder.getInstance();
        encoder = new BCryptPasswordEncoder();
        return encoder;
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        String dbUsername = "admin";
        String dbPassword = passwordEncoder().encode("123456");
        String dbRoles    = "ADMIN,GUEST";
        auth.inMemoryAuthentication().withUser(dbUsername).password(dbPassword).roles(dbRoles);

        UserDetailsService userDetailsService = new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                log.info("[step 03] Http request username - {}", username);
                String password = dbPassword;
                log.info("[step 04] Http request username from DB password - {}", password);
                if (!org.springframework.util.StringUtils.isEmpty(username) && !StringUtils.isEmpty(password)) {
                    User user = new User(username, password, AuthorityUtils.commaSeparatedStringToAuthorityList(dbRoles));
                    log.info("[step 05] Spring security convert username - {}", user.getUsername());
                    log.info("[step 05] Spring security convert password - {}", user.getPassword());
                    log.info("[step 05] Spring security convert authored - {}", user.getAuthorities());
                    return user;
                }
                return null;
            }
        };
        auth.userDetailsService(userDetailsService);

        AuthenticationProvider authenticationProvider = new AuthenticationProvider() {
            @Override
            public Authentication authenticate(Authentication authentication) throws AuthenticationException {
                UsernamePasswordAuthenticationToken token = null;
                String httpUsername = (String) authentication.getPrincipal();
                log.info("[step 01] Http request username - {}", httpUsername);
                String httpPassword = (String) authentication.getCredentials();
                log.info("[step 02] Http request password - {}", httpPassword);
                String md5Password = DigestUtils.md5DigestAsHex(httpPassword.getBytes());
                log.info("[step 02] Http request password convert MD5 - {}", md5Password);
                String base64Password = Base64Utils.encodeToString(httpPassword.getBytes());
                log.info("[step 02] Http request password convert BASE64 - {}", base64Password);
                UserDetails userDetails = userDetailsService.loadUserByUsername(httpUsername);
                if (Objects.nonNull(userDetails)) {
                    String bCryptUser = userDetails.getUsername();
                    log.info("[step 06] Http request username convert BCrypt - {}", bCryptUser);
                    String bCryptPass = userDetails.getPassword();
                    log.info("[step 06] Http request password convert BCrypt - {}", bCryptPass);
                    log.info("[step 07] BCrypt http request password with spring security password - {}", passwordEncoder().matches(dbPassword, bCryptPass));
                    log.info("[step 07] String http request password with spring security password - {}", dbPassword.equals(bCryptPass));
                    token = new UsernamePasswordAuthenticationToken(bCryptUser, bCryptPass, userDetails.getAuthorities());
                    log.info("[step 08] Http request username convert BCrypt to Token - {}", token.getPrincipal());
                    log.info("[step 08] Http request password convert BCrypt to Token - {}", token.getCredentials());
                    SecurityContextHolder.getContext().setAuthentication(token);
                    return token;
                }
                return null;
            }
            @Override
            public boolean supports(Class<?> authentication) {
                return true;
            }
        };
        auth.authenticationProvider(authenticationProvider);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/css/**", "/js/**", "/image/**", "/fonts/**", "/favicon.ico");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
            //外挂丝袜哥。{AntPathMatcher}蚂蚁路径请求匹配器。指定任何人都允许使用此URL。
            .antMatchers("/swagger-ui.html").permitAll()
            .antMatchers("/webjars/**").permitAll()
            .antMatchers("/swagger-resources/**").permitAll()
            .antMatchers("/v2/*").permitAll()
            .antMatchers("/csrf").permitAll()
            .antMatchers("/").permitAll()
            //外挂验证码。{AntPathMatcher}蚂蚁路径请求匹配器。指定任何人都允许使用此URL。
            .antMatchers("/getVerifyCode").permitAll()
            .antMatchers("/login/invalid").permitAll()
            .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
            .anyRequest().authenticated().and()
            //外挂过滤器。
            .addFilterBefore(new JwtAuthTokenFilter(), UsernamePasswordAuthenticationFilter.class)
            //外挂过滤器。
            .addFilterBefore(new VerifyCodeFilter(), UsernamePasswordAuthenticationFilter.class)
            //登录功能。
            .formLogin().loginPage("/loginPage").loginProcessingUrl("/authentication/form").defaultSuccessUrl("/successPage").failureUrl("/failurePage").permitAll()
            //记住我功能。
            .and().rememberMe().rememberMeParameter("remember-me").alwaysRemember(true).tokenValiditySeconds(60).userDetailsService(userDetailsService())
            //会话功能。
            .and().sessionManagement().invalidSessionUrl("/login/invalid").maximumSessions(1).maxSessionsPreventsLogin(false).expiredUrl("/index").and()
            //退出功能。
            .and().logout().logoutUrl("/logout").deleteCookies("JSESSIONID").clearAuthentication(true).invalidateHttpSession(true).logoutSuccessUrl("/index")
            //异常功能。
            .and().exceptionHandling().accessDeniedPage("/errorPage")
            .and().csrf().disable().headers().frameOptions().disable();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}