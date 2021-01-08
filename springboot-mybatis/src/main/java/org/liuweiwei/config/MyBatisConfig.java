package org.liuweiwei.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

/**
 * @author Liuweiwei
 * @since 2021-01-07
 */
//@Configuration
public class MyBatisConfig {

    @Autowired
    private DataSource dataSource;

    /**
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
//    @Bean
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(dataSource);
        factoryBean.setTypeAliasesPackage("org.**.model");

        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        factoryBean.setMapperLocations(resolver.getResources("classpath*:/resources/mapper/*.xml"));
        return factoryBean.getObject();
    }
}
