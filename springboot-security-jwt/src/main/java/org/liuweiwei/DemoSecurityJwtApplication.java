package org.liuweiwei;

import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.liuweiwei.web.VerifyCodeServlet;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * A JWT Claims Set - 索赔集。
 * 1. 这最终是一个JSON映射，可以向其中添加任何值，但为了方便起见，JWT标准名称作为类型安全的getter和setter提供。
 * 2. 因为这个接口扩展了{Map<String, Object>}，如果要添加自己的属性，只需使用映射方法，
 * 3. 例如：claims.{Map(Object, Object); map.put("someKey", "someValue");
 * 4. 通过调用 Jwts.Claims() 工厂方法之一，最容易创建 {@code Claims} 实例。
 *
 * JWT包含了三部分：1.头部<Header/>。2.载荷<Payload/>。3.签证<Signature/>。
 * 1. Header    - 头部。标题包含了令牌的元数据，并且包含签名或加密算法的类型。JWT的头部承载两部分信息：token类型和采用的加密算法。
 *     {
 *        "alg": "HS256",
 *        "typ": "JWT"
 *     }
 *     1.声明类型这里是：jwt。
 *     2.声明加密的算法通常直接使用：HMAC SHA256。
 * 2. Payload   - 有效负载。载荷就是存放有效信息的地方。基本上填两种类型的数据：
 *     1.标准中注册的声明的数据。
 *     2.自定义数据。
 *         iss - jwt签发者。
 *         sub - 面向的用户(jwt所面向的用户)。
 *         aud - 接收jwt的一方。
 *         exp - 过期时间戳(jwt的过期时间，这个过期时间必须要大于签发时间)。
 *         nbf - 定义在什么时间之前，该jwt都是不可用的。
 *         iat - jwt的签发时间。
 *         jti - jwt的唯一身份标识，主要用来作为一次性token,从而回避重放攻击。
 * 3. Signature - 签名，签证。jwt的第三部分是一个签证信息："secret".Base64Utils("Header").Base64Utils("Payload")
 *     1.base64 加密后的 "Header"。
 *     2.base64 加密后的 "Payload"。连接组成的字符串。
 *     3.然后通过 header 中声明的加密方式进行加盐 "secret" 组合加密，然后就构成了jwt的第三部分。
 *     4.密钥 secret 是保存在服务端的，服务端会根据这个密钥进行生成 token 和进行验证，所以需要保护好。
 *
 * public interface Claims extends Map<String, Object>, ClaimsMutator<Claims> {
 *     public String     ISSUER = "iss"; //jwt签发者
 *     public String    SUBJECT = "sub"; //面向的用户(jwt所面向的用户)
 *     public String   AUDIENCE = "aud"; //接收jwt的一方
 *     public String EXPIRATION = "exp"; //过期时间戳(jwt的过期时间，这个过期时间必须要大于签发时间)
 *     public String NOT_BEFORE = "nbf"; //定义在什么时间之前，该jwt都是不可用的
 *     public String  ISSUED_AT = "iat"; //jwt的签发时间
 *     public String         ID = "jti"; //jwt的唯一身份标识，主要用来作为一次性token,从而回避重放攻击
 * }
 *
 * @author Liuweiwei
 * @since 2020-12-31
 */
@SpringBootApplication
@EnableSwagger2
@EnableWebMvc
@MapperScan(basePackages = {"org.liuweiwei.dao"})
@EnableWebSecurity
@Log4j2
public class DemoSecurityJwtApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(DemoSecurityJwtApplication.class, args);
        ConfigurableEnvironment environment = context.getEnvironment();
        log.info(environment.getProperty("java.vendor.url"));
        log.info(environment.getProperty("java.vendor.url.bug"));
        log.info(environment.getProperty("sun.java.command") + " started successfully.");
    }

    /**注入验证码Servlet*/
    @Bean
    public ServletRegistrationBean indexServletRegistration() {
        ServletRegistrationBean registration = new ServletRegistrationBean(new VerifyCodeServlet());
        registration.addUrlMappings("/getVerifyCode");
        return registration;
    }
}
