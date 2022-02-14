/**
 * Copyright 2020 portal.mocomsys.com All Rights Reserved.
 */
package rose.mary.trace.database;
 

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import org.springframework.boot.jdbc.DataSourceBuilder;
/**
 * <pre>
 * rose.mary.trace.database
 * Datasource01Config.java
 * </pre>
 * @author whoana
 * @date Aug 27, 2019
 */
@Configuration
@ConfigurationProperties(prefix="spring.datasources.datasource01")
//@MapperScan(value="reose.mary.trace.database.mapper.m01", sqlSessionFactoryRef="sqlSessionFactory01")
@MapperScan(value="reose.mary.trace.database.mapper", sqlSessionFactoryRef="sqlSessionFactory01")
public class DataSource01Config {
	
	@Bean(name="datasource01")
	@Primary
	@ConfigurationProperties(prefix="spring.datasources.datasource01")
	public DataSource dataSource() {
		return DataSourceBuilder.create().build();
	}
	 
	
	@Bean(name="sqlSessionFactory01")
	@Primary
	public SqlSessionFactory sqlSessionFactory(
			@Qualifier("datasource01") DataSource dataSource, ApplicationContext ac) throws Exception {
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        sqlSessionFactoryBean.setMapperLocations(ac.getResources("classpath:rose/mary/trace/database/mapper/*/*.xml"));
        return sqlSessionFactoryBean.getObject();
	}
//	public SqlSessionFactory sqlSessionFactory(
//			@Qualifier("datasource01") DataSource dataSource, ApplicationContext ac) throws Exception {
//		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
//        sqlSessionFactoryBean.setDataSource(dataSource);
//        sqlSessionFactoryBean.setMapperLocations(ac.getResources("classpath:rose/mary/trace/database/mapper/m01/*.xml"));
//        return sqlSessionFactoryBean.getObject();
//	}

	@Primary
    @Bean(name="sqlSessionTemplate01")
    public SqlSessionTemplate sqlSession(@Autowired @Qualifier("sqlSessionFactory01") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
 
    @Primary
    @Bean(name="transactionManager01")
    public DataSourceTransactionManager transactionManager(@Autowired @Qualifier("datasource01") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
    
	
}
