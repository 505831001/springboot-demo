package com.liuweiwei.component;

import com.liuweiwei.config.WebMvcAutoConfig;
import com.liuweiwei.model.TbUser;
import com.liuweiwei.service.TbUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 用户登录认证信息查询
 *
 * @author liuweiwei
 * @since 2020-05-20
 */
@Component(value = "my01UserDetailsServiceImpl")
@Slf4j
public class My01UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private TbUserService tbUserService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("【第三步】获取页面用户名称：" + username);
        String md5Password = tbUserService.findPasswordByName(username);
        log.info("【第四步】通过页面用户名称查询数据库用户密码：" + md5Password);
        if (!StringUtils.isEmpty(username) && !StringUtils.isEmpty(md5Password)) {
            User user = new User(username, md5Password, AuthorityUtils.commaSeparatedStringToAuthorityList("ADMIN"));
            log.info("【第五步】把页面用户名称和数据库用户密码设到安全框架Usre对象：" + user.toString());
            return user;
        }
        return null;
    }
}
