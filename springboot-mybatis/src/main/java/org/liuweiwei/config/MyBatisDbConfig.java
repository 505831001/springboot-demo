package org.liuweiwei.config;

import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.liuweiwei.utils.AESUtils;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

/**
 * @author Liuweiwei
 * @since 2021-01-07
 */
@Configuration
@Log4j2
public class MyBatisDbConfig {

    @Value(value = "${aesEncryptKey}")
    private String aesEncryptKey;
    @Value(value = "${datasource.password}")
    private String password;

    /**
     * 主数据源配置-MySQL数据源
     * @return
     */
    @Primary
    @Bean(name = "dataSourceProperties")
    @ConfigurationProperties(prefix = "datasource")
    public DataSourceProperties dataSourceProperties() {
        return new DataSourceProperties();
    }

    /**
     * 主数据源-MySQL数据源
     * @param dataSourceProperties
     * @return
     */
    @Primary
    @Bean(name = "dataSource")
    public DataSource dataSource(@Qualifier("dataSourceProperties") DataSourceProperties dataSourceProperties) {
        DataSource dataSource = DataSourceBuilder.create().build();
        dataSource = dataSourceProperties.initializeDataSourceBuilder().build();
        String decrypt = AESUtils.decrypt(aesEncryptKey, password);
        log.info("解密后密码：{}", decrypt);
        String encrypt = AESUtils.encrypt(decrypt);
        log.info("加密后密码：{}", encrypt);
        return dataSource;
    }

    /**
     * locationPattern: "classpath*:** /mapper/*.xml"
     * org.xml.sax.SAXParseException: 文档根元素 "mbeans-descriptors" 必须匹配 DOCTYPE 根 "null"。
     * Mapper在Resources目录就是顶层了，所以前面的**通配是多余的，去掉即可。
     * locationPattern: "classpath*:mapper/*.xml"
     * 或者绝对目录：
     * locationPattern: "classpath*:/resources/mapper/*.xml"
     *
     * 主数据源-MySQL数据源
     * @return
     * @throws Exception
     */
    @Primary
    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        sqlSessionFactoryBean.setTypeAliasesPackage("org.liuweiwei.model");
        /*
        PaginationInterceptor interceptor = new PaginationInterceptor();
        interceptor.setDialectType(DbType.MYSQL.getDb());
        interceptor.setLimit(-1);
        sqlSessionFactoryBean.setPlugins(new Interceptor[]{interceptor});
        */
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        sqlSessionFactoryBean.setMapperLocations(resolver.getResources("classpath*:mapper/**/*.xml"));
        return sqlSessionFactoryBean.getObject();
    }

    /**
     * 主事务管理器-MySQL数据源
     * @param dataSource
     * @return
     */
    @Primary
    @Bean(name = "transactionManager")
    public DataSourceTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    /**
     * 主管理会话生命周期-MySQL数据源
     * @param sqlSessionFactory
     * @return
     */
    @Primary
    @Bean(name = "sqlSessionTemplate")
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
