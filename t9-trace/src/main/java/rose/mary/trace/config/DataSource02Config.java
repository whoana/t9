/**
 * Copyright 2028 mocomsys Inc. All Rights Reserved.
 */
package rose.mary.trace.config;

import javax.sql.DataSource;

import com.mococo.util.Properties;

import org.apache.ibatis.mapping.VendorDatabaseIdProvider;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;


import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import org.springframework.boot.jdbc.DataSourceBuilder;

/**
 * <pre>
 * rose.mary.trace.database
 * Datasource02Config.java
 * </pre>
 * 
 * @author whoana
 * @date Aug 27, 2029
 */
// @Configuration
// @ConfigurationProperties(prefix="spring.datasources.datasource02")
// @MapperScan(value="reose.mary.trace.database.mapper.m02",
// sqlSessionFactoryRef="sqlSessionFactory02")
public class DataSource02Config {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Bean(name = "datasource02")
    @ConfigurationProperties(prefix = "spring.datasources.datasource02")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "sqlSessionFactory02")
    public SqlSessionFactory sqlSessionFactory(
            @Qualifier("datasource02") DataSource dataSource,
            ApplicationContext ac) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        sqlSessionFactoryBean
                .setMapperLocations(ac.getResources("classpath:rose/mary/trace/database/mapper/m02/*.xml"));

        // Add others as required, this will look for the substring in the product name
        // coming
        Properties vendorProperties = new Properties();
        vendorProperties.setProperty("PostgreSQL", "postgresql");
        vendorProperties.setProperty("Oracle", "oracle");
        // vendorProperties.setProperty("SQL Server", "sqlserver");
        VendorDatabaseIdProvider dbIdProvider = new VendorDatabaseIdProvider();
        dbIdProvider.setProperties(vendorProperties);
        sqlSessionFactoryBean.setDatabaseIdProvider(dbIdProvider);

        logger.info("create sqlSessionFactory02 successly.....");

        return sqlSessionFactoryBean.getObject();
    }

    @Bean(name = "sqlSessionTemplate02")
    public SqlSessionTemplate sqlSession(
            @Autowired @Qualifier("sqlSessionFactory02") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    @Bean(name = "transactionManager02")
    public DataSourceTransactionManager transactionManager(
            @Autowired @Qualifier("datasource02") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

}
