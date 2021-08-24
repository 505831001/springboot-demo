package org.liuweiwei.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;

import javax.annotation.Resource;

/**
 * OAuth2.0 授权服务器配置
 * 1.接口参数
 *     1.http://localhost:9201/ 这里是我服务的地址以及端口。
 *     2.oauth/authorize?       这个是Spring Security OAuth2默认提供的接口。
 *     3.response_type=code     表示授权类型，必选项，此处的值固定为”code”。
 *     4.client_id=baidu        表示客户端的ID，必选项。这里使用的是项目启动时，控制台输出的security.oauth2.client.clientId可自定义。
 *     5.redirect_uri=https://www.baidu.com 表示重定向URI，可选项。即用户授权成功后，会跳转的地方，通常是第三方应用自己的地址。
 *     6.scope=read,write       表示申请的权限范围，可选项。这一项用于服务提供商区分提供哪些服务数据。
 *     7.state=?                表示客户端的当前状态，可以指定任意值，认证服务器会原封不动地返回这个值。这里没有使用到该值。
 *     {
 *         http://localhost:9201/oauth/authorize?response_type=code&client_id=baidu
 *     }
 * 2.请求Header参数，获取Token
 *     1.grant_type=authorization_code(授权码模式), 表示使用的授权模式，必选项。
 *     1.grant_type=password(密码模式),             表示使用的授权模式，必选项。
 *     1.grant_type=client_credentials(客户端模式), 表示使用的授权模式，必选项。
 *     1.grant_type=implicit(简化模式),             表示使用的授权模式，必选项。
 *     2.client_id=baidu        表示客户端ID，必选项。
 *     3.client_secret=123456   表示客户端密码，必选项。
 *     4.username=admin         表示授权用户账号，可选项。
 *     5.password=123456        表示授权用户密码，可选项。
 *     6.code=M8MDDv            表示授权码。
 *     7.redirect_uri=?         表示重定向URI，可选项，且必须与A步骤中的该参数值保持一致。
 *     {
 *         grant_type:authorization_code
 *         client_id:baidu
 *         client_secret:123456
 *         username:admin
 *         password:123456
 *         code=M8MDDv
 *     }
 * 3.获取Token响应参数
 *     1.access_token           表示访问令牌，必选项。
 *     2.token_type             表示令牌类型，该值大小写不敏感，必选项，可以是bearer类型或mac类型。
 *     3.refresh_token          表示更新令牌，用来获取下一次的访问令牌，可选项。
 *     4.expires_in             表示过期时间，单位为秒。如果省略该参数，必须其他方式设置过期时间。
 *     5.scope                  表示权限范围，如果与客户端申请的范围一致，此项可省略。
 *     {
 *         "access_token": "56164038-42ed-4351-8ba5-e634f5ed3e14",
 *         "token_type": "bearer",
 *         "refresh_token": "d59559d1-74f4-4912-85ef-c6baf573e507",
 *         "expires_in": 20,
 *         "scope": "read write"
 *     }
 * 4.利用Token获取资源
 *     {
 *         http://localhost:9201/echo?access_token=e1eb928f-646b-47f5-b9c4-0901543b597b
 *     }
 *     TODO->待处理：Access Denied 拒绝访问。握草。
 *     {
 *         "timestamp": "2021-08-24 08:30:10",
 *         "status": 403,
 *         "error": "Forbidden",
 *         "message": "Access Denied",
 *         "path": "/echo"
 *     }
 * OAuth2.0 官方SQL语句
 *
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
    @Resource
    private AuthenticationManager authenticationManager;

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.tokenKeyAccess("permitAll()").checkTokenAccess("isAuthenticated()").allowFormAuthenticationForClients();
    }

    /**
     * TODO->[GET请求]
     * TODO->http://localhost:9201/oauth/authorize?response_type=code&client_id=baidu
     * response_type 表示授权类型，必选项，此处的值固定为"code"。
     * client_id     表示客户端的ID，必选项。
     * client_secret 表示客户端的密码，可选项。
     * redirect_uri  表示重定向URI，可选项。
     * scope         表示申请的权限范围，可选项。
     * state         表示客户端的当前状态，可以指定任意值，认证服务器会原封不动地返回这个值。
     * 跳转到Spring Security安全框架登录页面。http://localhost:8881/login
     * 然后使用安全框架.configure(AuthenticationManagerBuilder auth)登录。
     * 跳转到Oauth2授权页面。http://localhost:8881/oauth/authorize?response_type=code&client_id=baidu
     * {
     *   OAuth Approval
     *   Do you authorize "baidu" to access your protected resources?
     *     scope.read:   Approve Deny
     *     scope.write:  Approve Deny
     *   [Authorize]
     * }
     * 勾选同意Approve，点击授权Approve。跳转到已授权.redirectUris("https://www.baidu.com")
     * 响应地址：https://www.baidu.com/?code=M8MDDv
     * TODO->[POST请求][Body=form-data]
     * TODO->http://localhost:9201/oauth/token
     * 1.授权码模式：grant_type=authorization_code,密码模式：grant_type=password,客户端模式：client_credentials(客户端模式),implicit(简化模式)
     *     grant_type:authorization_code //授权模式
     *     client_id:baidu               //客户端账号
     *     client_secret:123456          //客户端密钥
     *     username:admin                //授权用户账号
     *     password:123456               //授权用户密码
     *     code=M8MDDv                   //授权码
     * 2.密码模式：grant_type=password,客户端模式：client_credentials(客户端模式),implicit(简化模式)
     *     grant_type:password           //授权模式
     *     client_id:baidu               //客户端账号
     *     client_secret:123456          //客户端密钥
     *     username:admin                //授权用户账号
     *     password:123456               //授权用户密码
     * 3.客户端模式：client_credentials(客户端模式),implicit(简化模式)
     *     grant_type:client_credentials //授权模式
     *     client_id:baidu               //客户端账号
     *     client_secret:123456          //客户端密钥
     * 4.简化模式：implicit(简化模式)
     * 响应结果：
     * {
     *     "access_token": "e1eb928f-646b-47f5-b9c4-0901543b597b",
     *     "token_type": "bearer",
     *     "refresh_token": "100ab29f-8de3-4fa1-a42e-d4aef6d747da",
     *     "expires_in": 43199,
     *     "scope": "read write"
     * }
     * TODO->[GET请求]
     * TODO->http://localhost:9201/echo?access_token=e1eb928f-646b-47f5-b9c4-0901543b597b
     * 提示：总感觉此处少带了某些验证权限的东西。
     * {
     *     "timestamp": "2021-08-24 08:30:10",
     *     "status": 403,
     *     "error": "Forbidden",
     *     "message": "Access Denied",
     *     "path": "/echo"
     * }
     * TODO->待处理：Access Denied 拒绝访问。握草。
     * 1.从数据库取数据
     *     clients.withClientDetails(new ClientDetailsService());
     * 2.从内存中取数据
     *     clients.inMemory();
     * @param clients
     * @throws Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        /**
         * 2.从内存中取数据
         * 该Client允许的授权类型：authorization_code(授权码模式),password(密码模式),client_credentials(客户端模式),implicit(简化模式),refresh_token。
         * 允许的授权范围：read,write。
         * 是否跳过自动批准：false。建议不要跳过，不然怎么看见它：Do you authorize "baidu" to access your protected resources?
         * 加上验证回调地址。
         */
        clients.inMemory()
               .withClient("baidu")
               .secret(passwordEncoder.encode("123456"))
               .resourceIds(RESOURCE_ID)
               .authorizedGrantTypes("authorization_code", "password", "client_credentials", "implicit", "refresh_token")
               .scopes("read", "write")
               .authorities("ADMIN", "GUEST")
               .autoApprove(false)
               .redirectUris("https://www.baidu.com")
               .accessTokenValiditySeconds(60 * 30)
               .refreshTokenValiditySeconds(60 * 60 * 24);
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.authenticationManager(authenticationManager);
    }
}
