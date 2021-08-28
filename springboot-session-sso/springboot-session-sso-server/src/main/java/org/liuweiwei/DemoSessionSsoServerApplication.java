package org.liuweiwei;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.web.client.RestTemplate;

/**
 * 一、历史演进
 * 第一阶段：http无状态协议。http无状态协议。
 * 第二阶段：Session会话机制。session会话机制。在传统的单服务架构中，一般来说，只有一个服务器，那么不存在 Session 共享问题，
 * 第二阶段：登录状态。登录状态。在传统的单服务架构中，一般来说，只有一个服务器，那么不存在 Session 共享问题，
 * 第三阶段：多系统的复杂性。共享会话机制。但是在分布式/集群项目中，Session 共享则是一个必须面对的问题，
 * 第三阶段：单点登录。sso单点登录。分布式Spring Session + Redis方式实现单点登录。Spring security + Oauth2方式单点登录技术方案。
 *
 * 一、为什么需要Spring Session
 * 1.传统单机Web应用中，一般使用Tomcat或者Jetty等等Web容器时，用户的Session都是由容器管理。
 * 2.浏览器使用Cookie中记住SessionId，容器根据SessionId判断用户是否存在会话Session。
 * 3.这里的限制是Session存储在Web容器中，被单台Web容器管理。
 * 4.随着网站的演变，分布式应用和集群是趋势（提高性能）。
 * 5.此时用户的请求可能被负载分发至不同的服务器，此时传统的Web容器管理用户会话Session的方式即行不通。
 * 6.除非集群或者分布式Web应用能够共享Session，尽管Tomcat或者Jetty等等Web容器支持这样做。但是这样存在以下两点问题：
 *     1.需要侵入Web容器，提高问题的复杂。
 *     2.Web容器之间共享Session，集群机器之间势必要交互耦合。
 * 7.基于这些因素，必须提供新的可靠的集群分布式或者集群Session的解决方案，来突破Traditional-Session单机限制。
 * 8.即Web容器Session方式，下面简称Traditional-Session，因此Spring-Session应用随之而生。
 *
 * 二、解决方案
 * 1.Spring Session使得支持集群会话变得微不足道，而不依赖于特定于应用程序容器的解决方案。
 * 2.它还提供透明集成：
 *     1.HttpSession允许以应用程序容器（即Tomcat）中立方式替换HttpSession，支持在头文件中提供会话ID以使用RESTFul API。
 *     2.WebSocket提供在接收WebSocket消息时保持HttpSession活动的能力。
 *     3.WebSession允许以应用程序容器中立方式替换Spring WebFlux的WebSession。
 * 3.一个简化的方案就是使用Spring Session来实现这一功能，
 * 4.Spring Session就是使用Spring中的代理过滤器，将所有的Session操作拦截下来，自动的将数据同步到Redis中，或者自动的从Redis中读取数据。
 *
 * 三、
 *
 * @author Liuweiwei
 * @since 2020-12-23
 */
@SpringBootApplication
@Log4j2
public class DemoSessionSsoServerApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(DemoSessionSsoServerApplication.class, args);
        ConfigurableEnvironment environment = context.getEnvironment();
        log.info(environment.getProperty("java.vendor.url"));
        log.info(environment.getProperty("java.vendor.url.bug"));
        log.info(environment.getProperty("sun.java.command") + " started successfully.");
    }

    @LoadBalanced
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        /**
         * Redis 默认序列化格式：JdkSerializationRedisSerializer();
         * Redis 指定JSON序列化格式：new GenericJackson2JsonRedisSerializer();, new Jackson2JsonRedisSerializer<>(Object.class);
         */
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.setDefaultSerializer(new GenericJackson2JsonRedisSerializer());
        template.setConnectionFactory(redisConnectionFactory);
        template.setEnableTransactionSupport(true);
        return template;
    }
}
