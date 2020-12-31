package com.liuweiwei.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.Collection;

/**
 * @author liuweiwei
 * @since 2020-05-20
 */
@Component
public class Jwt02AuthenticationProviderImpl implements AuthenticationProvider {

    @Autowired
    private Jwt01UserDetailsServiceImpl userDetailsServiceImpl;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String userName = authentication.getName();
        String password = (String) authentication.getCredentials();

        UserDetails info = userDetailsServiceImpl.loadUserByUsername(userName);
        if (ObjectUtils.isEmpty(info)) {
            throw new BadCredentialsException("用户不存在！");
        }
        boolean flag = passwordEncoder.matches(password, info.getPassword());
        if (flag == false) {
            throw new BadCredentialsException("密码不正确！");
        }
        Collection<? extends GrantedAuthority> authorities = info.getAuthorities();
        return new UsernamePasswordAuthenticationToken(info, password, authorities);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
