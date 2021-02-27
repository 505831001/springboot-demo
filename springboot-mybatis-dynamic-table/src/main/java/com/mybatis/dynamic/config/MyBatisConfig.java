package com.mybatis.dynamic.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

/**
 * 1. 基于@Configuration配置文档形式：
 *     session.setTypeAliasesPackage("com.mybatis.dynamic.model")
 *     session.setMapperLocations(resolver.getResources("classpath*:mapper/*.xml"));
 * 2. 基于yml配置文档形式：
 *     mapper-locations: classpath*:mapper/*.xml
 *     type-aliases-package: com.mybatis.dynamic.model
 *
 * @author Liuweiwei
 * @since 2021-01-07
 */
@Configuration
public class MyBatisConfig {

    @Autowired
    private DataSource dataSource;

    /**
     * mybatis:
     *   mapper-locations: classpath*:mapper/*.xml
     *   type-aliases-package: com.mybatis.dynamic.model
     * 说白了，下面这些配置直接在yml文档中两句话搞定。如上。
     *
     * locationPattern: "classpath*:** /mapper/*.xml"
     * org.xml.sax.SAXParseException: 文档根元素 "mbeans-descriptors" 必须匹配 DOCTYPE 根 "null"。
     * Mapper在Resources目录就是顶层了，所以前面的**通配是多余的，去掉即可。
     * locationPattern: "classpath*:mapper/*.xml"
     * 或者绝对目录：
     * locationPattern: "classpath*:/resources/mapper/*.xml"
     *
     * @return
     * @throws Exception
     */
    @Bean
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        SqlSessionFactoryBean session = new SqlSessionFactoryBean();
        session.setDataSource(dataSource);
        session.setTypeAliasesPackage("com.mybatis.dynamic.model");

        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        session.setMapperLocations(resolver.getResources("classpath*:mapper/*.xml"));
        return session.getObject();
    }
}
