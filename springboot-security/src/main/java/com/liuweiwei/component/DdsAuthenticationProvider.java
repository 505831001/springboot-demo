package com.liuweiwei.component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.Base64Utils;
import org.springframework.util.DigestUtils;

import java.util.Collection;
import java.util.Objects;

/**
 * @author liuweiwei
 * @since 2020-05-20
 */
@Slf4j
public class DdsAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private CcdUserDetailsService ccdUserDetailsService;

    /**
     * //TODO->5-根据传入的【自定义身份】{AuthenticationProvider}添加验证。由于{AuthenticationProvider}实现未知，因此必须在外部完成所有自定义，并立即返回{AuthenticationManagerBuilder}。
     *
     * @param authentication
     * @return
     * @throws AuthenticationException
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String httpUsername = (String) authentication.getPrincipal();
        log.info("【第一步】获取页面用户名称<principal>：" + httpUsername);
        String httpPassword = (String) authentication.getCredentials();
        log.info("【第二步】获取页面用户密码<credentials>：" + httpPassword);
        String md5Password = DigestUtils.md5DigestAsHex(httpPassword.getBytes());
        log.info("【第二步】使用页面用户秘密之MD5加密<md-5>：" + md5Password);
        String base64Password = Base64Utils.encodeToString(httpPassword.getBytes());
        log.info("【第二步】使用页面用户密码之BASE64加密<base-64>：" + base64Password);

        UserDetails details = ccdUserDetailsService.loadUserByUsername(httpUsername);
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
