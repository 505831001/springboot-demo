package com.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.mybatis.spring.annotation.MapperScan;
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
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.web.client.RestTemplate;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * No qualifying bean of type 'com.example.dao.TbUserMapper' available: expected at least 1 bean which qualifies as autowire candidate.
 * 没有"com.example.dao.TbUserMapper"类型的合格bean可用：至少需要1个符合autowire候选条件的bean。
 *
 * @author Liuweiwei
 * @since 2021-01-10
 */
@SpringBootApplication
@EnableSwagger2
@MapperScan(basePackages = {"com.example.dao"})
public class DemoJdbcApplication extends SpringBootServletInitializer {

    /**
     * 日志-实现层：logback<org.slf4j>
     */
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(DemoJdbcApplication.class);

    /**
     * 日志-实现层：log4j<org.apache.log4j>
     */
    private static final Logger logger = LogManager.getLogger(DemoJdbcApplication.class);

    /**
     * 类，可以用来从Java主方法引导和启动Spring应用程序。
     * 默认情况下，class将执行以下步骤来引导应用程序：
     */
    private static SpringApplication application;

    /**
     * 大部分(如果不是全部)应用程序上下文都要实现的SPI接口。
     * 除了{ApplicationContext}接口中的应用程序上下文客户端方法之外，还提供了配置应用程序上下文的工具。
     */
    private static ConfigurableApplicationContext applicationContext;

    /**
     * 大多数(如果不是全部){Environment}类型都要实现的配置接口。
     * 提供用于设置活动和默认概要文件以及操作底层属性源的工具。
     * 允许客户端通过{ConfigurablePropertyResolver}超接口设置和验证所需的属性，自定义转换服务和更多。
     */
    private static ConfigurableEnvironment applicationContextEnvironment;

    public static void main(String[] args) {
        application = new SpringApplication(DemoJdbcApplication.class);
        application.setBannerMode(Banner.Mode.CONSOLE);
        applicationContext = application.run(args);
        applicationContextEnvironment = applicationContext.getEnvironment();

        logger.info(applicationContextEnvironment.getProperty("java.vendor.url"));
        logger.info(applicationContextEnvironment.getProperty("java.vendor.url.bug"));
        logger.info(applicationContextEnvironment.getProperty("sun.java.command") + " started successfully.");
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
