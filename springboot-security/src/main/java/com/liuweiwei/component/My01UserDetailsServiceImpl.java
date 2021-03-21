package com.liuweiwei.component;

import com.liuweiwei.config.WebMvcAutoConfig;
import com.liuweiwei.model.TbUser;
import com.liuweiwei.service.TbUserService;
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
public class My01UserDetailsServiceImpl implements UserDetailsService {

    /**
     * 日志-实现层：logback
     */
    private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(this.getClass());

    @Autowired
    private TbUserService tbUserService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("【第三步】获取页面用户名称：" + username);
        String password = tbUserService.findPasswordByName(username);
        log.info("【第四步】通过页面用户名称查询数据库用户密码：" + password);
        if (!StringUtils.isEmpty(username) && !StringUtils.isEmpty(password)) {
            User user = new User(username, password, AuthorityUtils.commaSeparatedStringToAuthorityList("ADMIN"));
            log.info("【第五步】把页面用户名称和数据库用户密码设到安全框架Usre对象：" + user.toString());
            return user;
        }
        return null;
    }
}
