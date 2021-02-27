package com.mybatis.dynamic;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * 扫描DAO
 * org.xml.sax.SAXParseException: 文档根元素 "mbeans-descriptors" 必须匹配 DOCTYPE 根 "null"。
 *
 * @author Liuweiwei
 * @since 2021-01-17
 */
@SpringBootApplication
@MapperScan(basePackages = "com.mybatis.dynamic.dao")
public class DemoMybatisDynamicTableApplication extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(DemoMybatisDynamicTableApplication.class, args);
    }
}
