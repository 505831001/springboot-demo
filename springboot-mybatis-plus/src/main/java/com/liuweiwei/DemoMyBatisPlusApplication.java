package com.liuweiwei;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusPropertiesCustomizer;
import com.baomidou.mybatisplus.core.incrementer.IKeyGenerator;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.baomidou.mybatisplus.extension.incrementer.H2KeyGenerator;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
//import org.apache.rocketmq.client.exception.MQClientException;
//import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.LoggerFactory;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
//import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Action:
 * org.apache.ibatis.binding.BindingException: Invalid bound statement (not found):
 * 配置：resources/mapper非<配置在pom.xml中>：java/mapper则YML中添加扫描配置。
 * mybatis:
 *   mapper-locations: classpath*:/resources/mapper/*.xml 或者相对路径方式：classpath*:mapper/*.xml
 *   type-aliases-package: org.example.dao
 *
 * Action:
 * Consider defining a bean of type 'org.liuweiwei.dao.TbUserMapper' in your configuration.
 * 1. 通过配置扫描注解：@MapperScan(basePackages = "org.example.dao")
 * 2. 通过注释：@Mapper
 *
 * TODO -> MyBatis-Plus - https://mp.baomidou.com/guide/
 * 核心功能：
 * 1. 代码生成器
 *     0. 生成文件 - new AutoGenerator();
 *     1. 全局配置 - new GlobalConfig();
 *     2. 数据库配置 - new DataSourceConfig();
 *     3. 跟包相关的配置项 - new PackageConfig();
 *     4. 策略配置项 - new StrategyConfig();
 * 2. CRUD 接口
 *     1. 顶级 IService
 *     2. 基础 BaseMapper
 * 3. 条件构造器
 *     2. LambdaQueryWrapper;
 *     1. QueryWrapper;
 *     3. Wrappers;
 * 4. 分页插件
 *     1. 旧版 - PaginationInterceptor
 *     2. 新版 - MybatisPlusInterceptor
 * 5. Sequence 主键
 *     1. IKeyGenerator
 *     2. MybatisPlusPropertiesCustomizer
 *     TODO -> 内置支持。如果内置支持不满足你的需求，可实现IKeyGenerator接口来进行扩展。
 *     1. DB2KeyGenerator
 *     2. H2KeyGenerator
 *     3. KingbaseKeyGenerator
 *     4. OracleKeyGenerator
 *     5. PostgreKeyGenerator
 * 6. 自定义ID生成器
 *     1. IdentifierGenerator
 *     2. MybatisPlusPropertiesCustomizer
 * [Tools]
 * com.alibaba.fastjson.JSONObject;
 * com.google.common.collect.Lists;
 * org.apache.commons.collections4.CollectionUtils;
 * org.apache.commons.collections4.ListUtils;
 * org.apache.commons.beanutils.BeanUtils;
 * org.apache.commons.beanutils.PropertyUtils;
 *
 * [Entity]
 * com.baomidou.mybatisplus.annotation.IdType;
 * com.baomidou.mybatisplus.annotation.TableId;
 * com.baomidou.mybatisplus.annotation.TableField;
 * [Service]
 * com.baomidou.mybatisplus.extension.service.IService;
 * com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
 * [Wrappers]
 * com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
 * com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
 * com.baomidou.mybatisplus.core.toolkit.Wrappers;
 * [Mapper]
 * com.baomidou.mybatisplus.core.mapper.BaseMapper;
 *
 * @author Liuweiwei
 * @since 2021-01-08
 */
@SpringBootApplication
//@EnableEurekaClient
@EnableSwagger2
@EnableWebMvc
@MapperScan(basePackages = "com.liuweiwei.dao")
@Log4j2
public class DemoMyBatisPlusApplication {
    /**
     * 同步日志-实现层：logback<org.slf4j>
     * private static final org.slf4j.Logger SLF4J = LoggerFactory.getLogger(DemoMyBatisPlusApplication.class);
     * 异步日志-实现层：log4j<org.apache.log4j>
     * private static final Logger LOG4J2 = LogManager.getLogger(DemoMyBatisPlusApplication.class);
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
        application = new SpringApplication(DemoMyBatisPlusApplication.class);
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

    /**
     * TODO -> 4. 分页插件：分布插件注入之前
     * {
     *   "records": [...],
     *   "total": 0,
     *   "size": 5,
     *   "current": 1,
     *   "orders": [],
     *   "optimizeCountSql": true,
     *   "hitCount": false,
     *   "countId": null,
     *   "maxLimit": null,
     *   "searchCount": true,
     *   "pages": 0
     * }
     * TODO -> 4. 分页插件：分页插件注入之后，你品，你细品
     * {
     *   "records": [...],
     *   "total": 20,
     *   "size": 5,
     *   "current": 1,
     *   "orders": [],
     *   "optimizeCountSql": true,
     *   "hitCount": false,
     *   "countId": null,
     *   "maxLimit": null,
     *   "searchCount": true,
     *   "pages": 4
     * }
     * @return
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }

    /**
     * SpringBoot Sequence 主键 - 方式一：使用配置类
     *
     * @return
     */
    @Bean
    public IKeyGenerator keyGenerator() {
        return new H2KeyGenerator();
    }

    /**
     * SpringBoot Sequence 主键 - 方式二：通过 MybatisPlusPropertiesCustomizer 自定义
     *
     * @return
     */
    @Bean
    public MybatisPlusPropertiesCustomizer plusPropertiesCustomizer() {
        return plusProperties -> plusProperties.getGlobalConfig().getDbConfig().setKeyGenerator(new H2KeyGenerator());
    }

    /** SpringBoot 自定义ID生成器 - 方式一：使用配置类
    @Bean
    public IdentifierGenerator idGenerator() {
        return new CustomIdGenerator();
    }
    */

    /**
     * SpringBoot 自定义ID生成器 - 方式二：通过 MybatisPlusPropertiesCustomizer 自定义
    @Bean
    public MybatisPlusPropertiesCustomizer plusPropertiesCustomizer() {
        return plusProperties -> plusProperties.getGlobalConfig().setIdentifierGenerator(new CustomIdGenerator());
    }
    */
}
