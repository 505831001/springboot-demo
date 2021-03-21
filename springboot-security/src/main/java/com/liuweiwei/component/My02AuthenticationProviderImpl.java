package com.liuweiwei.component;

import com.liuweiwei.config.WebMvcAutoConfig;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.Collection;
import java.util.Objects;

/**
 * @author liuweiwei
 * @since 2020-05-20
 */
@Component(value = "my02AuthenticationProviderImpl")
public class My02AuthenticationProviderImpl implements AuthenticationProvider {

    /**
     * 日志-实现层：logback
     */
    private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(this.getClass());

    @Qualifier("my01UserDetailsServiceImpl")
    @Autowired
    private UserDetailsService userDetailsService;

    private UsernamePasswordAuthenticationToken token;

    private UserDetails userDetails;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException { ;
        String username = (String) authentication.getPrincipal();
        log.info("【第一步】获取页面用户名称：" + username);
        String password = (String) authentication.getCredentials();
        log.info("【第二步】获取页面用户密码：" + password);
        userDetails = userDetailsService.loadUserByUsername(username);
        if (Objects.nonNull(userDetails)) {
            log.info("【第六步】获取安全框架User对象：" + userDetails.toString());
            boolean matches = new BCryptPasswordEncoder().matches(password, userDetails.getPassword());
            log.info("【第七步】比较页面用户密码和安全框架密码a：" + matches);
            boolean equals = password.equals(userDetails.getPassword());
            log.info("【第七步】比较页面用户密码和安全框架密码b：" + equals);
            token = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            log.info("【第八步】设置安全框架User对象到Token：" + token.toString());
            return token;
        }
        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(token);
    }
}
