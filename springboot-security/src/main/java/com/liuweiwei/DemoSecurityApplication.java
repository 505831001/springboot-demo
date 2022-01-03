package com.liuweiwei;

import com.liuweiwei.web.VerifyCodeServlet;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author Liuweiwei
 * @since 2020-12-21
 */
@SpringBootApplication
@EnableSwagger2
@EnableWebMvc
@MapperScan(basePackages = {"com.liuweiwei.mapper"})
@EnableWebSecurity
@Slf4j
public class DemoSecurityApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(DemoSecurityApplication.class);
        application.setBannerMode(Banner.Mode.CONSOLE);
        ConfigurableApplicationContext context = application.run(args);
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
        template.setConnectionFactory(redisConnectionFactory);
        template.setDefaultSerializer(new GenericJackson2JsonRedisSerializer());
        return template;
    }

    /*
    @Bean
    public DefaultMQProducer defaultMQProducer() throws MQClientException {
        DefaultMQProducer producer = new DefaultMQProducer("wei_producer_group");
        producer.setVipChannelEnabled(false);
        producer.setNamesrvAddr("127.0.0.1:9876");
        producer.start();
        return producer;
    }
    */

    /**注入验证码Servlet*/
    @Bean
    public ServletRegistrationBean indexServletRegistration() {
        ServletRegistrationBean registration = new ServletRegistrationBean(new VerifyCodeServlet());
        registration.addUrlMappings("/getVerifyCode");
        return registration;
    }
}
