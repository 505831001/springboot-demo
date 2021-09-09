package com.example;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import lombok.extern.log4j.Log4j2;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.ConfigurableEnvironment;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author Liuweiwei
 * @since 2021-01-10
 */
@SpringBootApplication
@EnableSwagger2
@MapperScan(basePackages = {"com.example.dao"})
@Log4j2
public class DemoQuartzVisualApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(DemoQuartzVisualApplication.class, args);
        ConfigurableEnvironment environment = context.getEnvironment();
        log.info(environment.getProperty("java.vendor.url"));
        log.info(environment.getProperty("java.vendor.url.bug"));
        log.info(environment.getProperty("sun.java.command") + " started successfully.");
    }

    /**
     * TODO -> 4. 分页插件：分布插件注入之前("total": 0,"pages": 0)
     * TODO -> 4. 分页插件：分页插件注入之后
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
}
