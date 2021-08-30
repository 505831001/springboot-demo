package org.liuweiwei;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * 一、http无状态协议
 *     1.web应用采用Browser/Server架构，http作为通信协议。
 *     2.http是无状态协议，浏览器的每一次请求，服务器会独立处理，不与之前或之后的请求产生关联，这个过程用下图说明，三次请求/响应对之间没有任何联系。
 *     3.但这也同时意味着，任何用户都能通过浏览器访问服务器资源，如果想保护服务器的某些资源，必须限制浏览器请求。
 *     4.要限制浏览器请求，必须鉴别浏览器请求，响应合法请求，忽略非法请求；要鉴别浏览器请求，必须清楚浏览器请求状态。
 *     5.既然http协议无状态，那就让服务器和浏览器共同维护一个状态，这就是会话机制。
 * 二、session会话机制
 *     1.浏览器第一次请求服务器，服务器创建一个会话，并将会话id(JSESSIONID)作为响应的一部分发送给浏览器。
 *     2.浏览器存储会话id(JSESSIONID)，并在后续第二次和第三次请求中带上会话id(JSESSIONID)，服务器取得请求中的会话id(JSESSIONID)就知道是不是同一个用户了。
 *     3.这个过程用下图说明，后续请求与第一次请求产生了关联。
 *     {
 *         JSESSIONID=712238f4321ea0ea5bfa3db0ca73a25e
 *     }
 * 三、登录状态（session会话机制）
 *     1.有了会话机制，登录状态就好明白了，我们假设浏览器第一次请求服务器需要输入用户名与密码验证身份。
 *     2.服务器拿到用户名密码去数据库比对，正确的话说明当前持有这个会话的用户是合法用户。
 *     3.应该将这个会话标记为"已授权"或者"已登录"等等之类的状态，既然是会话的状态，自然要保存在会话对象中，tomcat在会话对象中设置登录状态如下。
 *     {
 *         HttpSession session = request.getSession();
 *         session.setAttribute("Authorization", "712238f4321ea0ea5bfa3db0ca73a25e");
 *     }
 *     4.用户再次访问时，tomcat在会话对象中查看登录状态。
 *     {
 *         HttpSession session = request.getSession();
 *         session.getAttribute("Authorization");
 *     }
 *     6.session 和 cookie 是保存用户状态的两种方式，session在服务端，cookie 在客户端。
 *     7.session（会话）
 *         1.粘性会话：如果某台服务器宕机，那么会话信息就没有了。
 *         2.复制会话：每台机器都复制会话，如果量太大的话，不现实。
 *         3.集中会话：使用 mongoDB，redis 等统一保持会话。
 *     5.每次请求受保护资源时都会检查会话对象中的登录状态，只有Authorization=712238f4321ea0ea5bfa3db0ca73a25e的会话才能访问，登录机制因此而实现。
 * 四、多系统的复杂性（分布式session会话机制）
 *     1.单系统登录解决方案的核心是cookie，cookie携带会话id(JSESSIONID)在浏览器与服务器之间维护会话状态。
 *     2.cookie是有限制的，这个限制就是cookie的域（通常对应网站的域名），浏览器发送http请求时会自动携带与该域匹配的cookie，而不是所有cookie。
 *     3.因此，我们需要一种全新的登录方式来实现多系统应用群的登录，这就是单点登录。
 *     一、为什么需要Spring Session
 *     1.传统单机Web应用中，一般使用Tomcat或者Jetty等等Web容器时，用户的Session都是由容器管理。
 *     2.浏览器使用Cookie中记住SessionId，容器根据SessionId判断用户是否存在会话Session。
 *     3.这里的限制是Session存储在Web容器中，被单台Web容器管理。
 *     4.随着网站的演变，分布式应用和集群是趋势（提高性能）。
 *     5.此时用户的请求可能被负载分发至不同的服务器，此时传统的Web容器管理用户会话Session的方式即行不通。
 *     6.除非集群或者分布式Web应用能够共享Session，尽管Tomcat或者Jetty等等Web容器支持这样做。但是这样存在以下两点问题：
 *         1.需要侵入Web容器，提高问题的复杂。
 *         2.Web容器之间共享Session，集群机器之间势必要交互耦合。
 *     7.基于这些因素，必须提供新的可靠的集群分布式或者集群Session的解决方案，来突破Traditional-Session单机限制。
 *     8.即Web容器Session方式，下面简称Traditional-Session，因此Spring-Session应用随之而生。
 *     二、解决方案
 *     1.Spring Session使得支持集群会话变得微不足道，而不依赖于特定于应用程序容器的解决方案。
 *     2.它还提供透明集成：
 *         1.HttpSession 允许以应用程序容器（即Tomcat）中立方式替换HttpSession，支持在头文件中提供会话ID以使用RESTFul API。
 *         2.WebSocket   提供在接收WebSocket消息时保持HttpSession活动的能力。
 *         3.WebSession  允许以应用程序容器中立方式替换Spring WebFlux的WebSession。
 *     3.一个简化的方案就是使用Spring Session来实现这一功能，
 *     4.Spring Session就是使用Spring中的代理过滤器，将所有的Session操作拦截下来，自动的将数据同步到Redis中，或者自动的从Redis中读取数据。
 * 五、单点登录（1.分布式session会话机制，2.或者Spring Security+OAuth2认证中心）
 * 方法一：利用cookie的域实现
 *     1.我们上面讲到，浏览器会设置cookie，cookie中保存中和服务器通信的session id。
 *     2.但是cookie是有限制的，限制就是域（网站的域名），
 *     3.浏览器发送http请求时会去匹配，当请求的地址和cookie的域相匹配时，就会把这个cookie携带，不匹配就不携带。
 *     4.这样就好办了。比如我们有两个系统要做单点（~~），我们我们让这两个系统的域名保持一致不就行了，
 *     5.比如A系统域名：www.myapp.A，B系统域名www.myapp.B设置的时候这是cookie的域为www.myapp，
 *     6.那么访问A，B系统时都会携带这个cookie，也实现了单点。
 *     7.以前的系统很多都是这么做单点登录的，优点就是简单方便，缺点就是不够动态，
 *     8.首先域名要统一，其次，web服务器还得统一，tomcat是JSESSIONID，php，.net服务器就不是JSESSIONID了。
 * 方法二：增加业务应用实现
 *     1.还是有两个系统要做单点（系统A，B）,我们可以不用cookie来实现单点，增加一个系统注册应用（系统C）。
 *     2.把系统A，B的功能模块全部注册到系统C中，同时关联账号，
 *     3.系统C的 user1 关联系统A的 userA1，关联系统B的 userB1，同时登录的入口唯一，即只开放系统C的登录。
 *     4.那么我们user1登录了系统C，同时要访问系统A的某个模块，我们就可以跳转到系统A，同时在系统A中创建userA1的session，
 *     5.要访问系统B的某个模块，我们就可以跳转到系统B，同时在系统B中创建userB1的session，这样也可以实现单点。
 *     6.优点就是比较适合后台管理类，权限多的应用。缺点就是相对复杂。
 * 方法三：通过认证中心实现
 * 1.sso-client
 *     1.拦截子系统未登录用户请求，跳转至sso认证中心。
 *     2.接收并存储sso认证中心发送的令牌。
 *     3.与sso-server通信，校验令牌的有效性。
 *     4.建立局部会话。
 *     5.拦截用户注销请求，向sso认证中心发送注销请求。
 *     6.接收sso认证中心发出的注销请求，销毁局部会话。
 * 2.sso-server
 *     1.验证用户的登录信息。
 *     2.创建全局会话。
 *     3.创建授权令牌。
 *     4.与sso-client通信发送令牌。
 *     5.校验sso-client令牌有效性。
 *     6.系统注册。
 *     7.接收sso-client注销请求，注销所有会话。
 * 0.java web应用拦截请求的三种方式Servlet、Filter、Listener三种方式。
 * 0.java web应用拦截请求的三种方式Filter、Interceptor、Aspect三种方式。
 *
 * 总结：
 * 开始从http无状态协议，
 * 再到Session会话机制，
 * 然后单应用登录状态（这句话描述的好：这个会话标记为"已授权"或者"已登录"等等之类的状态”），
 * 然后多应用同域名共享Session会话机制，此时已经是单点登录，
 * 最后，单点登录技术就开始有认证中心OAuth2等实现方式。
 * 完美！！！
 *
 * @author Liuweiwei
 * @since 2020-12-23
 */
@SpringBootApplication
@Log4j2
public class DemoSessionApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(DemoSessionApplication.class, args);
        ConfigurableEnvironment environment = context.getEnvironment();
        log.info(environment.getProperty("java.vendor.url"));
        log.info(environment.getProperty("java.vendor.url.bug"));
        log.info(environment.getProperty("sun.java.command") + " started successfully.");
    }
}
