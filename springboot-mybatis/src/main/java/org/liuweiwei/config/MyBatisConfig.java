package org.liuweiwei.config;

import com.mysql.jdbc.jdbc2.optional.MysqlXADataSource;
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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

/**
 * @author Liuweiwei
 * @since 2021-01-07
 */
@Configuration
@Log4j2
public class MyBatisConfig {

    @Value(value = "${spring.datasource.encrypt}")
    private String encrypt;

    // --- 数据源1-Spring数据源+MyBatis框架 ---

    /**
     * 主数据源配置-MySQL数据源
     * @return
     */
    @Primary
    @Bean(name = "dataSourceProperties")
    @ConfigurationProperties(prefix = "spring.datasource")
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
        MysqlXADataSource xaDataSource = new MysqlXADataSource();
        xaDataSource.setUrl(dataSourceProperties.getUrl());
        xaDataSource.setPinGlobalTxToPhysicalConnection(true);
        xaDataSource.setUser(dataSourceProperties.getUsername());
        xaDataSource.setPassword(AESUtils.decrypt(encrypt, dataSourceProperties.getPassword()));
        xaDataSource.setAutoReconnectForPools(true);
        xaDataSource.setAutoReconnect(true);
        return xaDataSource;
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
    public SqlSessionFactory sqlSessionFactory(@Qualifier("dataSource") DataSource dataSource) throws Exception {
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
    public DataSourceTransactionManager transactionManager(@Qualifier("dataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    /**
     * 主管理会话生命周期-MySQL数据源
     * @param sqlSessionFactory
     * @return
     */
    @Primary
    @Bean(name = "sqlSessionTemplate")
    public SqlSessionTemplate sqlSessionTemplate(@Qualifier("sqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    // --- 数据源2-DataSource+JdbcTemplate ---

    /**
     * 次数据源配置-Spring数据源＋JDBC模板
     * @return
     */
    @Bean(name = "jdbcDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.jdbc")
    public DataSource jdbcDataSource() {
        DataSource dataSource = DataSourceBuilder.create().build();
        return dataSource;
    }

    /**
     * 次事务管理器-Spring数据源＋JDBC模板
     * @param jdbcDataSource
     * @return
     */
    @Bean(name = "jdbcTransactionManager")
    public DataSourceTransactionManager jdbcTransactionManager(@Qualifier("jdbcDataSource") DataSource jdbcDataSource) {
        return new DataSourceTransactionManager(jdbcDataSource);
    }

    /**
     * 创建会话模板-Spring数据源＋JDBC模板
     * @param jdbcDataSource
     * @return
     */
    @Bean(name = "jdbcTemplate")
    public JdbcTemplate jdbcTemplate(@Qualifier("jdbcDataSource") DataSource jdbcDataSource) {
        return new JdbcTemplate(jdbcDataSource);
    }
}
