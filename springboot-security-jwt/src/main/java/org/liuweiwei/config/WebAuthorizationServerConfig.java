package org.liuweiwei.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

/**
 * @author Liuweiwei
 * @since 2021-04-13
 */
@Configuration
public class WebAuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    /**
     * NoOpPasswordEncoder.getInstance();
     * new BCryptPasswordEncoder();
     *
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        PasswordEncoder encoder = NoOpPasswordEncoder.getInstance();
        encoder = new BCryptPasswordEncoder();
        return encoder;
    }

    @Override
    public void configure(final AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
        oauthServer.tokenKeyAccess("permitAll()").checkTokenAccess("isAuthenticated()");
    }

    @Override
    public void configure(final ClientDetailsServiceConfigurer clients) throws Exception {
        /**
         * 1. clientId 可以类比为用户名。
         * 2. secret   可以类比为密码。
         * 3. 授权类型，这里选择授权码。
         * 4. 授权范围。
         * 5. 自动认证。
         * 6. 认证成功重定向URL。
         * 7. 超时时间，10s。
         */
        clients.inMemory()
                .withClient("SampleClientId")
                .secret(passwordEncoder().encode("secret"))
                .authorizedGrantTypes("authorization_code")
                .scopes("user_info")
                .autoApprove(true)
                .redirectUris("http://localhost:8882/login","http://localhost:8883/login")
                .accessTokenValiditySeconds(10);
    }
}
