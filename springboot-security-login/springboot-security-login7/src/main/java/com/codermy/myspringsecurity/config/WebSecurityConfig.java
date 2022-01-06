package com.codermy.myspringsecurity.config;

import com.codermy.myspringsecurity.dao.PermissionDao;
import com.codermy.myspringsecurity.auth.UserDetailsDto;
import com.codermy.myspringsecurity.auth.UserDto;
import com.codermy.myspringsecurity.eneity.TbPermission;
import com.codermy.myspringsecurity.eneity.TbUser;
import com.codermy.myspringsecurity.auth.MyAuthenticationSuccessHandler;
import com.codermy.myspringsecurity.auth.MyLogoutSuccessHandle;
import com.codermy.myspringsecurity.auth.RestAuthenticationAccessDeniedHandler;
import com.codermy.myspringsecurity.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author LiuWeiWei
 * @since 2022-01-06
 */
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserService userService;
    @Autowired
    private PermissionDao permissionDao;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        UserDetailsService userDetailsService = new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                TbUser dbUser = userService.getUser(username);
                if (dbUser == null) {
                    throw new AuthenticationCredentialsNotFoundException("用户名不存在");
                } else if (dbUser.getStatus() == TbUser.Status.LOCKED) {
                    throw new LockedException("用户被锁定,请联系管理员");
                }
                List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
                List<TbPermission> permissions = permissionDao.listByUserId(dbUser.getId().intValue());
                List<String> collect = permissions.stream().map(TbPermission::getPermission).collect(Collectors.toList());
                for (String authority : collect) {
                    if (authority != null) {
                        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(authority);
                        grantedAuthorities.add(grantedAuthority);
                    }
                }
                String options = "userDetailsDto";
                if ("userDto".equals(options)) {
                    UserDto userDto = new UserDto(dbUser.getUserName(), dbUser.getPassword(), grantedAuthorities);
                    userDto.setId(dbUser.getId());
                    userDto.setNickName(dbUser.getNickName());
                    return  userDto;
                } else if ("userDetailsDto".equals(options)) {
                    UserDetailsDto userDetailsDto = new UserDetailsDto();
                    userDetailsDto.setId(dbUser.getId());
                    userDetailsDto.setNickName(dbUser.getNickName());
                    userDetailsDto.setUsername(dbUser.getUserName());
                    userDetailsDto.setPassword(dbUser.getPassword());
                    userDetailsDto.setAuthorities(grantedAuthorities);
                    return  userDetailsDto;
                }
                return null;
            }
        };
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    //-------------------- 华丽的分割线 --------------------


    @Override
    public void configure(WebSecurity web) throws Exception {
        super.configure(web);
    }

    //-------------------- 华丽的分割线 --------------------

    @Autowired
    private MyAuthenticationSuccessHandler myAuthenticationSuccessHandler;

    @Autowired
    private RestAuthenticationAccessDeniedHandler restAuthenticationAccessDeniedHandler;
    @Autowired
    private MyLogoutSuccessHandle myLogoutSuccessHandle;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().sessionManagement();

        http.headers().frameOptions().sameOrigin();

        http.authorizeRequests().antMatchers("/login", "/login.html", "/xadmin/**", "/treetable-lay/**", "/static/**").permitAll().anyRequest().authenticated();

        String options = "first";
        switch (options) {
            case "first":
                http.formLogin().successHandler(myAuthenticationSuccessHandler);
                break;
            case "second":
                http.formLogin().loginPage("/login.html").successHandler(myAuthenticationSuccessHandler);
                break;
            case "third":
                http.formLogin().loginProcessingUrl("/authentication/form").successHandler(myAuthenticationSuccessHandler);
                break;
            case "fourth":
                http.formLogin().loginPage("/login.html").loginProcessingUrl("/authentication/form").successHandler(myAuthenticationSuccessHandler);
                break;
            default:
                break;
        }

        http.logout().permitAll().invalidateHttpSession(true).deleteCookies("JSESSIONID").logoutSuccessHandler(myLogoutSuccessHandle);

        http.exceptionHandling().accessDeniedHandler(restAuthenticationAccessDeniedHandler);
    }
}
