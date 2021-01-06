package org.liuweiwei.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * Druid Monitor 德鲁伊监控器地址：http://localhost:8080/druid/login.html
 *
 * @author Liuweiwei
 * @since 2021-01-06
 */
@Configuration
public class DruidConfig {

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource druidDataSource() {
        return new DruidDataSource();
    }

    /**
     * 1. 主要实现web监控的配置处理
     * 1.1 表示进行druid监控的配置处理操作："/druid/*"
     * 1.2 用户名："loginUsername"
     * 1.3 密码："loginPassword"
     * 1.4 是否可以重置数据源："resetEnable"
     * 1.5 白名单："allow"
     * 1.6 黑名单："deny"
     *
     * @return
     */
    @Bean
    public ServletRegistrationBean druidServlet() {
        ServletRegistrationBean servlet = new ServletRegistrationBean(new StatViewServlet(), "/druid/*");
        servlet.addInitParameter("allow", "127.0.0.1,192.168.1.121");
        servlet.addInitParameter("deny", "192.168.1.200");
        servlet.addInitParameter("loginUsername", "root");
        servlet.addInitParameter("loginPassword", "123456");
        servlet.addInitParameter("resetEnable", "false");
        return servlet;
    }

    /**
     * 2. 监控
     * 2.1 所有请求进行监控处理："/*"
     * 2.2 排除："exclusions", "/static/*,*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*"
     *
     * @return
     */
    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean filter = new FilterRegistrationBean();
        filter.setFilter(new WebStatFilter());
        filter.addUrlPatterns("/*");
        filter.addInitParameter("exclusions", "/static/*,*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
        return filter;
    }
}
