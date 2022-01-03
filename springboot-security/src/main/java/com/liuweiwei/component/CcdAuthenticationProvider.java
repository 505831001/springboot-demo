package com.liuweiwei.component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * @author liuweiwei
 * @since 2020-05-20
 */
@Slf4j
public class CcdAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    /**
     * //TODO->5-根据传入的【自定义身份】{AuthenticationProvider}添加验证。由于{AuthenticationProvider}实现未知，因此必须在外部完成所有自定义，并立即返回{AuthenticationManagerBuilder}。
     *
     * @param authentication
     * @return
     * @throws AuthenticationException
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        //TODO->4-根据传入的【自定义身份】{UserDetailsService}添加验证。然后返回一个{DaoAuthenticationConfigurer}，以允许自定义身份验证。根据传入的自定义添加身份验证。
        UserDetailsService userDetailsService = new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                String dbUsername = username;
                log.info("【第三步】获取页面用户名称获取数据库用户对象：" + dbUsername);
                String dbPassword = bCryptPasswordEncoder.encode("123456");
                log.info("【第四步】通过页面用户名称查询数据库用户密码：" + dbPassword);
                List<GrantedAuthority> dbAuthorities = AuthorityUtils.commaSeparatedStringToAuthorityList("ADMIN");
                log.info("【第五步】获取页面用户名称获取数据库用户权限：" + dbAuthorities);
                if (!StringUtils.isEmpty(dbUsername) && !StringUtils.isEmpty(dbPassword)) {
                    User user = new User(dbUsername, dbPassword, dbAuthorities);
                    log.info("【第五步】把页面用户名称和数据库用户密码设到安全框架User对象：" + user.toString());
                    return user;
                }
                return null;
            }
        };

        String httpUsername = (String) authentication.getPrincipal();
        log.info("【第一步】获取页面用户名称<principal>：" + httpUsername);
        String httpPassword = (String) authentication.getCredentials();
        log.info("【第二步】获取页面用户密码<credentials>：" + httpPassword);

        UserDetails details = userDetailsService.loadUserByUsername(httpUsername);
        if (Objects.nonNull(details)) {
            String dataUsername = details.getUsername();
            log.info("【第六步】获取安全框架User对象用户：" + dataUsername);
            String dataPassword = details.getPassword();
            log.info("【第六步】获取安全框架User对象密码：" + dataPassword);
            Collection<? extends GrantedAuthority> dataAuthorities = details.getAuthorities();
            log.info("【第六步】获取安全框架User对象权限：" + dataPassword);

            boolean matches = bCryptPasswordEncoder.matches(httpPassword, dataPassword);
            if (matches) {
                log.info("【第七步】比较页面用户明文密码和安全框架密文密码是否相等：" + matches);
                UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(dataUsername, dataPassword, dataAuthorities);
                log.info("【第八步】设置安全框架User对象到Token账号：" + token.getPrincipal());
                log.info("【第八步】设置安全框架User对象到Token密码：" + token.getCredentials());
                SecurityContextHolder.getContext().setAuthentication(token);
                return token;
            }
        }
        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
