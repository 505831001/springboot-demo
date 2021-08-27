package org.liuweiwei.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import javax.annotation.Resource;

/**
 * OAuth2.0 授权服务器配置
 * @author Liuweiwei
 * @since 2020-12-30
 */
@EnableAuthorizationServer
@Configuration
public class WebOauth2AuthorityConfig extends AuthorizationServerConfigurerAdapter {

    /**
     * 指定当前资源的ID(必填项)
     * 底层代码：org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer
     * 属性默认值：String resourceId = "oauth2-resource"
     */
    private static final String RESOURCE_ID = "oauth2-resource";

    @Resource
    private PasswordEncoder passwordEncoder;

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.tokenKeyAccess("permitAll()").checkTokenAccess("isAuthenticated()").allowFormAuthenticationForClients();
    }

    /**
     * 第一步：获取授权码Code
     * TODO->[GET请求][redirectUris(String... uris)配置单个参数时使用][dev环境]
     * TODO->http://localhost:9200/oauth/authorize?response_type=code&client_id=client
     * 或者
     * TODO->[GET请求][redirectUris(String... uris)配置多个参数时使用][uat环境]
     * TODO->http://localhost:9200/oauth/authorize?response_type=code&client_id=client&redirect_uri=http://localhost:9202/login
     * --- 接口(URL)参数 ---
     * 1.http://localhost:9200/ 这里是我服务的地址以及端口。
     * 2.oauth/authorize?       这个是Spring Security OAuth2默认提供的接口。
     * 3.response_type=code     表示授权类型，必选项，此处的值固定为”code”。
     * 4.client_id=baidu        表示客户端的ID，必选项。这里使用的是项目启动时，控制台输出的security.oauth2.client.clientId可自定义。
     * 4.client_secret=secret   表示客户端的密码，可选项。这里使用的是项目启动时，控制台输出的security.oauth2.client.secret可自定义。
     * 5.redirect_uri=https://www.baidu.com 表示重定向URI，可选项。即用户授权成功后，会跳转的地方，通常是第三方应用自己的地址。
     * 6.scope=read,write       表示申请的权限范围，可选项。这一项用于服务提供商区分提供哪些服务数据。
     * 7.state=?                表示客户端的当前状态，可以指定任意值，认证服务器会原封不动地返回这个值。这里没有使用到该值。
     * {
     *     http://localhost:9200/oauth/authorize?response_type=code&client_id=baidu
     * }
     * 跳转到Security安全框架登录页面。
     * http://localhost:9200/login
     * 然后使用Security安全框架认证登录。
     * protected void configure(AuthenticationManagerBuilder auth);
     * 跳转到Oauth2授权页面。
     * http://localhost:9200/oauth/authorize?response_type=code&client_id=client
     * http://localhost:9200/oauth/authorize?response_type=code&client_id=client&redirect_uri=http://localhost:9202/login
     * {
     *   OAuth Approval
     *   Do you authorize "baidu" to access your protected resources?
     *     scope.read:   Approve Deny
     *     scope.write:  Approve Deny
     *   [Authorize]
     * }
     * 勾选同意Approve，点击授权Approve。跳转到已授权页面。
     * public ClientBuilder redirectUris("http://localhost:9202/login")
     * 响应地址：
     * https://www.baidu.com/?code=M8MDDv
     * http://localhost:9202/login?code=ve6gZC
     *
     * 第二步：通过授权码Code获取令牌Token，浏览器方式
     * 提示：需要开启endpoints.allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST)允许的令牌端点请求方法支持。
     * TODO->http://localhost:9200/oauth/token?client_id=client&client_secret=secret&grant_type=authorization_code&redirect_uri=http://localhost:9202/login&code=jrbBZS
     * 或者
     * 第二步：通过授权码Code获取令牌Token，Postman方式
     * TODO->[POST请求][表单-Body(form-data or x-www-form-urlencoded]
     * TODO->http://localhost:9200/oauth/token
     * --- 请求头(Header)参数 ---
     * 1.grant_type=authorization_code(授权码模式), 表示使用的授权模式，必选项。
     * 1.grant_type=password(密码模式),             表示使用的授权模式，必选项。
     * 1.grant_type=client_credentials(客户端模式), 表示使用的授权模式，必选项。
     * 1.grant_type=implicit(简化模式),             表示使用的授权模式，必选项。
     * 2.client_id=baidu        表示客户端ID，必选项。
     * 3.client_secret=123456   表示客户端密码，必选项。
     * 4.username=admin         表示授权用户账号，可选项。
     * 5.password=123456        表示授权用户密码，可选项。
     * 6.code=M8MDDv            表示授权码。
     * 7.redirect_uri=?         表示重定向URI，可选项，且必须与A步骤中的该参数值保持一致。
     * {
     *     grant_type:authorization_code
     *     client_id:baidu
     *     client_secret:123456
     *     username:admin
     *     password:123456
     *     code=M8MDDv
     * }
     * 1.授权码模式：grant_type=authorization_code,密码模式：grant_type=password,客户端模式：client_credentials(客户端模式),implicit(简化模式)
     *     grant_type:authorization_code            //授权模式
     *     client_id:baidu                          //客户端账号
     *     client_secret:123456                     //客户端密钥
     *     username:admin                           //授权用户账号
     *     password:123456                          //授权用户密码
     *     redirect_uri:http://localhost:9202/login //uat环境才需要
     *     code=M8MDDv                              //授权码
     * 2.密码模式：grant_type=password,客户端模式：client_credentials(客户端模式),implicit(简化模式)
     *     grant_type:password                      //授权模式
     *     client_id:baidu                          //客户端账号
     *     client_secret:123456                     //客户端密钥
     *     username:admin                           //授权用户账号
     *     password:123456                          //授权用户密码
     * 3.客户端模式：client_credentials(客户端模式),implicit(简化模式)
     *     grant_type:client_credentials            //授权模式
     *     client_id:baidu                          //客户端账号
     *     client_secret:123456                     //客户端密钥
     * 4.简化模式：implicit(简化模式)
     * --- 获取(Token)响应参数 ---
     * 1.access_token           表示访问令牌，必选项。
     * 2.token_type             表示令牌类型，该值大小写不敏感，必选项，可以是bearer类型或mac类型。
     * 3.refresh_token          表示更新令牌，用来获取下一次的访问令牌，可选项。
     * 4.expires_in             表示过期时间，单位为秒。如果省略该参数，必须其他方式设置过期时间。
     * 5.scope                  表示权限范围，如果与客户端申请的范围一致，此项可省略。
     * {
     *     "access_token": "e1eb928f-646b-47f5-b9c4-0901543b597b",
     *     "token_type": "bearer",
     *     "refresh_token": "100ab29f-8de3-4fa1-a42e-d4aef6d747da",
     *     "expires_in": 43199,
     *     "scope": "read write"
     * }
     * 扩展：在使用refresh_token刷新令牌的时候，需要在认证服务器上面设置。需要登录。暂时未解决。可不刷，照样用。
     * TODO->http://localhost:9200/oauth/token?grant_type=refresh_token&refresh_token=100ab29f-8de3-4fa1-a42e-d4aef6d747da
     *
     * 第三步：访问资源，请求头方式
     * TODO->[POST请求][请求头-Headers]
     * TODO->http://localhost:9201/oauth/token
     * 提示：使用Postman请求，需要登录(Spring Security)。否则需要登录Spring Security。
     * {
     *     Content-Type:application/x-www-form-urlencoded
     *     Authorization:Basic bXktY2xpZW50LTE6MTIzNDU2Nzg=
     * }
     * 第三步：访问资源，浏览器方式
     * TODO->[GET请求]
     * TODO->http://localhost:9200/echo 未配置 OAuth2 资源服务器http.requestMatchers().antMatchers("")。因此只被Spring Security拦截登录即可。
     * TODO->http://localhost:9200/admin/echo?access_token=e2b86af3-827a-4a62-aab0-c41c634937be 未经授权：访问此资源需要完全身份验证。
     * TODO->http://localhost:9200/guest/echo?access_token=e2b86af3-827a-4a62-aab0-c41c634937be unauthorized: Full authentication is required to access this resource.
     * 提示：这是资源服务器融合配置在授权服务器应用中。说白了就是保护授权服务器中的请求。WebOauth2ResourceConfig就是这个文件，授权服务本身不需要它，授权服务器只生成Token而已，哪有什么请求资源。
     * 提示：使用Postman请求，需要登录(Spring Security)。否则拒绝访问。
     * {
     *     "timestamp": "2021-08-24 08:30:10",
     *     "status": 403,
     *     "error": "Forbidden",
     *     "message": "Access Denied",
     *     "path": "/echo"
     * }
     *
     * 第四步：访问资源，资源服务器，或者客户端都已经被OAuth2保护资源，授权后访问。
     * TODO->http://localhost:9202/echo 未配置 OAuth2 资源服务器http.requestMatchers().antMatchers("")。因此只被Spring Security拦截登录即可。
     * TODO->http://localhost:9202/admin/echo?access_token=182e3ed7-8a29-455d-b166-4c00a4bae6db 未经授权：访问此资源需要完全身份验证。
     * TODO->http://localhost:9202/guest/echo?access_token=182e3ed7-8a29-455d-b166-4c00a4bae6db unauthorized: Full authentication is required to access this resource.
     * 提示：这是资源服务器独立配置在客户端应用中。说白了就是保护资源服务器，或者说客户端中的请求。
     *
     * 1.从数据库取数据
     *     clients.withClientDetails(new ClientDetailsService());
     * 2.从内存中取数据
     *     clients.inMemory();
     *     该Client允许的授权类型：authorization_code(授权码模式),password(密码模式),client_credentials(客户端模式),implicit(简化模式),refresh_token。
     *     允许的授权范围：read,write。
     *     是否跳过自动批准：false。建议不要跳过，不然怎么看见它：Do you authorize "baidu" to access your protected resources?
     *     加上验证回调地址。
     * @param clients
     * @throws Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
               .withClient("client")
               .secret(passwordEncoder.encode("secret"))
               .resourceIds(RESOURCE_ID)
               .authorizedGrantTypes("authorization_code", "password", "client_credentials", "implicit", "refresh_token")
               .scopes("read", "write")
               .authorities("ADMIN", "GUEST")
               .autoApprove(false)
               .redirectUris("http://localhost:9201/login", "http://localhost:9202/login", "http://localhost:9203/authorize/login")
               .accessTokenValiditySeconds(60 * 30)
               .refreshTokenValiditySeconds(60 * 60 * 24);
    }

    @Resource
    private RedisConnectionFactory redisConnectionFactory;
    /**
     * TODO->增加了TokenStore，将Token存储到Redis中。否则默认放在内存中的话每次重启的话token都丢了。
     * localhost:0>keys * (空格)
     * 1) "client_id_to_access:baidu"
     * 2) "auth_to_access:21894fe3228de3033cc3fdb734e29ab0"
     * 3) "auth:fe77c6bd-f25d-4ba3-843c-7b5f27d56b39"
     * 4) "uname_to_access:baidu:admin"
     * 5) "access_to_refresh:fe77c6bd-f25d-4ba3-843c-7b5f27d56b39"
     * 6) "refresh_auth:0ce707cc-bdb4-4944-af24-6b360c131584"
     * 7) "refresh_to_access:0ce707cc-bdb4-4944-af24-6b360c131584"
     * 8) "refresh:0ce707cc-bdb4-4944-af24-6b360c131584"
     * 9) "access:fe77c6bd-f25d-4ba3-843c-7b5f27d56b39"
     */
    @Bean
    public TokenStore tokenStore() {
        return new RedisTokenStore(redisConnectionFactory);
    }
    @Resource
    private AuthenticationManager authenticationManager;
    @Resource
    private UserDetailsService userDetailsService;
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.authenticationManager(authenticationManager);
        endpoints.allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST);
        endpoints.userDetailsService(userDetailsService);
        endpoints.tokenStore(tokenStore());
    }
}
