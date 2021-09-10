package com.excel.poi.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.excel.poi.utils.AESUtils;
import com.mysql.jdbc.jdbc2.optional.MysqlXADataSource;
import lombok.extern.log4j.Log4j2;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

/**
 * @author Liuweiwei
 * @since 2021-08-12
 */
@Configuration
@Log4j2
public class MyBatisPlusConfig {

    @Value(value = "${spring.datasource.encrypt}")
    private String encrypt;

    // --- 数据源1-Spring数据源+MyBatisPlus框架 ---

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
     * 主数据源会话-MySQL数据源
     * @param dataSource
     * @return
     * @throws Exception
     */
    @Primary
    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(@Qualifier("dataSource") DataSource dataSource) throws Exception {
        MybatisSqlSessionFactoryBean sqlSessionFactoryBean = new MybatisSqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        //MyBatisPlus分页插件旧版
        //PaginationInterceptor interceptor = new PaginationInterceptor();
        //interceptor.setDialectType(DbType.MYSQL.getDb());
        //interceptor.setLimit(-1);
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        sqlSessionFactoryBean.setPlugins(new Interceptor[]{interceptor});
        sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:mapper/**/*.xml"));
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

    // --- 数据源2-Spring-jta-Atomikos数据源+JDBC模板 ---

}
