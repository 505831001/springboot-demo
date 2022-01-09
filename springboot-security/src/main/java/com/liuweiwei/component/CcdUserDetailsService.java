package com.liuweiwei.component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 用户登录认证信息查询
 *
 * @author liuweiwei
 * @since 2020-05-20
 */
@Slf4j
public class CcdUserDetailsService implements UserDetailsService {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    /**
     * //TODO->4-根据传入的【自定义身份】{UserDetailsService}添加验证。然后返回一个{DaoAuthenticationConfigurer}，以允许自定义身份验证。根据传入的自定义添加身份验证。
     *
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
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
            return new AaaUser(dbUsername, dbPassword, dbAuthorities);
        }
        return null;
    }
}
