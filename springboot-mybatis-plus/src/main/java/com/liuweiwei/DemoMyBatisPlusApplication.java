package com.liuweiwei;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author Liuweiwei
 * @since 2021-01-08
 */
@SpringBootApplication
@EnableSwagger2
@MapperScan(basePackages = "com.liuweiwei.dao")
public class DemoMyBatisPlusApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoMyBatisPlusApplication.class, args);
    }
}
