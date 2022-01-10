package com.codermy.myspringsecurity.config;

import com.codermy.myspringsecurity.auth.BbcUserDetails;
import com.codermy.myspringsecurity.auth.AaaUser;
import com.codermy.myspringsecurity.dao.PermissionDao;
import com.codermy.myspringsecurity.eneity.TbPermission;
import com.codermy.myspringsecurity.eneity.TbUser;
import com.codermy.myspringsecurity.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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

    @Resource
    private UserService userService;
    @Resource
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
                String options = "bbcUserDetails";
                if ("aaaUser".equals(options)) {
                    AaaUser aaaUser = new AaaUser(dbUser.getUserName(), dbUser.getPassword(), grantedAuthorities);
                    aaaUser.setId(dbUser.getId());
                    aaaUser.setNickName(dbUser.getNickName());
                    return  aaaUser;
                } else if ("bbcUserDetails".equals(options)) {
                    BbcUserDetails bbcUserDetails = new BbcUserDetails();
                    bbcUserDetails.setId(dbUser.getId());
                    bbcUserDetails.setNickName(dbUser.getNickName());
                    bbcUserDetails.setUsername(dbUser.getUserName());
                    bbcUserDetails.setPassword(dbUser.getPassword());
                    bbcUserDetails.setAuthorities(grantedAuthorities);
                    return  bbcUserDetails;
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

    @Resource
    private ObjectMapper objectMapper;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        AuthenticationSuccessHandler authenticationSuccessHandler = new AuthenticationSuccessHandler() {
            @Override
            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                response.setHeader("Access-Controller-Allow-Origin", "*");
                response.setHeader("Access-Controller-Allow-Methods", "*");
                response.setContentType("application/json;charset=UTF-8");
                response.setStatus(HttpStatus.OK.value());
                response.getWriter().write(objectMapper.writeValueAsString(authentication));
            }
        };

        LogoutSuccessHandler logoutSuccessHandler = new LogoutSuccessHandler() {
            @Override
            public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                response.sendRedirect("/login");
            }
        };

        AccessDeniedHandler accessDeniedHandler = new AccessDeniedHandler() {
            @Override
            public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
                httpServletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
                httpServletResponse.setCharacterEncoding("utf-8");
                httpServletResponse.sendRedirect("/403.html");
                httpServletResponse.getWriter().println("您无权限");
            }
        };

        //-------------------- 华丽的分割线 --------------------

        //请求拦截处理机制
        http.authorizeRequests().antMatchers("/login", "/login.html", "/xadmin/**", "/treetable-lay/**", "/static/**").permitAll().anyRequest().authenticated();
        //登录跳转处理机制
        String options = "first";
        /**
         * 1-默认页面请求："/login<GET>",默认login
         * 2-定义页面请求："/login.html<GET>",接收loginPage("/login.html")
         * 3-表单页面请求："/authentication/form<POST>",接收loginProcessingUrl("/authentication/form")
         */
        switch (options) {
            case "first":
                //1-两者都不配置：默认页面(login有效(包含其它路径),login.html有效),无表单形式
                http.formLogin().successHandler(authenticationSuccessHandler);
                break;
            case "second":
                //2-只配置loginPage：定义页面(login无效,login.html有效(包含其它路径)),无表单形式
                http.formLogin().loginPage("/login.html").successHandler(authenticationSuccessHandler);
                break;
            case "third":
                //3-只配置loginProcessingUrl：默认页面(login有效(包含其它路径),login.html有效),有表单形式
                http.formLogin().loginProcessingUrl("/authentication/form").successHandler(authenticationSuccessHandler);
                break;
            case "fourth":
                //4-两者都配置：定义页面(login无效,login.html有效(包含其它路径)),有表单形式
                http.formLogin().loginPage("/login.html").loginProcessingUrl("/authentication/form").successHandler(authenticationSuccessHandler);
                break;
            default:
                break;
        }

        //退出登录处理机制
        http.logout().permitAll().invalidateHttpSession(true).deleteCookies("JSESSIONID").logoutSuccessHandler(logoutSuccessHandler);

        //异常登录处理机制
        http.exceptionHandling().accessDeniedHandler(accessDeniedHandler);

        //关闭跨域等其它
        http.csrf().disable().sessionManagement().and().headers().frameOptions().sameOrigin();
    }
}
