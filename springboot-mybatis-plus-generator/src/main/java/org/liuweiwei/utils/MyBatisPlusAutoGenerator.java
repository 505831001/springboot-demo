package org.liuweiwei.utils;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.rules.DbType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

/**
 * @author Liuweiwei
 * @since 2021-01-06
 */
public class MyBatisPlusAutoGenerator {
    public static void main(String[] args) {
        /**
         * 1. 全局配置
         * 1.1 是否支持AR模式
         * 1.2 作者
         * 1.3 生成路径
         * 1.4 文件覆盖
         * 1.5 主键策略
         * 1.6 生成Service首字母是否为"I"
         */
        GlobalConfig global = new GlobalConfig();
        global.setActiveRecord(true); 
        global.setAuthor("liuweiwei");
        global.setOutputDir(System.getProperty("user.dir") + "/springboot-mybatis-plus-generator/src/main/java");
        global.setFileOverride(true);
        global.setIdType(IdType.AUTO);
        global.setServiceName("%sService");
        global.setBaseResultMap(true);
        global.setBaseColumnList(true);

        /**
         * 2. 数据源配置。
         * 2.1 设置数据库类型：Oracle 数据库。
         */
        /*
        DataSourceConfig source = new DataSourceConfig();
        source.setDbType(DbType.ORACLE);
        source.setDriverName("oracle.jdbc.OracleDriver");
        source.setUrl("jdbc:oracle:thin:@localhost:1521:orcl");
        source.setUsername("system");
        source.setPassword("oracle123");
        */
        /**
         * 2.2 设置数据库类型：MySQL 数据库。
         */
        DataSourceConfig source = new DataSourceConfig();
        source.setDbType(DbType.MYSQL);
        source.setDriverName("com.mysql.jdbc.Driver");
        source.setUrl("jdbc:mysql://127.0.0.1:3306/ego?useUnicode=true&characterEncoding=utf8&useSSL=false");
        source.setUsername("root");
        source.setPassword("123456");

        /**
         * 3. 包名策略配置
         */
        PackageConfig packages = new PackageConfig();
        packages.setParent("com.example");
        /*
        packages.setEntity("bean");
        packages.setService("service");
        packages.setController("controller");
        packages.setMapper("dao");
        packages.setXml("mapper");
        */

        /**
         * 4. 策略配置
         * 4.1 全局大写
         * 4.2 指定表名和字段名是否使用下划线
         * 4.3 数据库表名映射到实体的命名策略
         * 4.4 生成的表Oracle
         * 4.5 生成的表Mysql
         */
        StrategyConfig strategy = new StrategyConfig();
        strategy.setCapitalMode(true);
        strategy.setDbColumnUnderline(true);
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setInclude("tb_user", "tb_item", "tb_order");

        /**
         * 5. 整合配置
         * 5.1 全局配置
         * 5.2 数据源配置
         * 5.3 包名策略配置
         * 5.4 策略配置
         */
        AutoGenerator auto = new AutoGenerator();
        auto.setGlobalConfig(global);
        auto.setDataSource(source);
        auto.setPackageInfo(packages);
        auto.setStrategy(strategy);

        /**
         * 6. 执行代码生成器
         */
        auto.execute();
    }
}