package com.liuweiwei.component;

import com.liuweiwei.model.TbUser;
import com.liuweiwei.service.TbUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 用户登录认证信息查询
 *
 * @author liuweiwei
 * @since 2020-05-20
 */
@Component
public class Jwt01UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private TbUserService tbUserService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        TbUser tbUser = tbUserService.findUserByName(username);
        List<GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList("admin");
        User user = new User(username, tbUser.getPassword(), authorities);
        return user;
    }
}
