package org.liuweiwei;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
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
 * @author Liuweiwei
 * @since 2021-01-07
 */
@SpringBootApplication
@EnableSwagger2
@MapperScan(basePackages = "org.liuweiwei.dao")
public class DemoMyBatisApplication extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(DemoMyBatisApplication.class, args);
    }
}
