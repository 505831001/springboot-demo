package org.liuweiwei;

import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.slf4j.LoggerFactory;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author Liuweiwei
 * @since 2021-01-05
 */
@SpringBootApplication
@EnableSwagger2
@EnableWebMvc
@Log4j2
public class DemoSwagger2Application extends SpringBootServletInitializer {
    /**
     * 同步日志-实现层：logback<org.slf4j>
     * private static final org.slf4j.Logger SLF4J = LoggerFactory.getLogger(DemoSwagger2Application.class);
     * 异步日志-实现层：log4j<org.apache.log4j>
     * private static final Logger LOG4J2 = LogManager.getLogger(DemoSwagger2Application.class);
     */

    /**
     * TODO -> org.springframework.boot.SpringApplication - application
     * 类，该类可用于从Java主方法引导和启动Spring应用程序。默认情况下，类将执行以下步骤来引导应用程序：
     * TODO -> org.springframework.context.ConfigurableApplicationContext - application.context
     * SPI接口将由大多数（如果不是所有）应用程序上下文实现。除了{ApplicationContext}接口中的应用程序上下文客户端方法外，还提供了配置应用程序上下文的工具。
     * TODO -> org.springframework.core.env.ConfigurableEnvironment - application.context.environment
     * SPI接口要由大多数（如果不是所有的话）{@link-Environment}类型实现的配置接口。提供用于设置活动配置文件和默认配置文件以及操作基础属性源的工具。允许客户端设置和验证所需的属性，通过{ConfigurablePropertyResolver}超级界面定制转换服务等。
     */
    private static SpringApplication application;
    private static ConfigurableApplicationContext applicationContext;
    private static ConfigurableEnvironment applicationContextEnvironment;

    public static void main(String[] args) {
        application = new SpringApplication(DemoSwagger2Application.class);
        application.setBannerMode(Banner.Mode.CONSOLE);
        applicationContext = application.run(args);
        applicationContextEnvironment = applicationContext.getEnvironment();

        log.info(applicationContextEnvironment.getProperty("java.vendor.url"));
        log.info(applicationContextEnvironment.getProperty("java.vendor.url.bug"));
        log.info(applicationContextEnvironment.getProperty("sun.java.command") + " started successfully.");
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

    @Bean
    public DefaultMQProducer defaultMQProducer() throws MQClientException {
        DefaultMQProducer producer = new DefaultMQProducer("wei_producer_group");
        producer.setVipChannelEnabled(false);
        producer.setNamesrvAddr("127.0.0.1:9876");
        producer.start();
        return producer;
    }
}
