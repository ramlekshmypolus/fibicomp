package com.polus.fibicomp.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;

@Configuration
@PropertySource("classpath:application.properties")
public class FibiRepoConfig {

	@Value("${jdbc.driverClassName}")
	private String driverClassName;

	@Value("${jdbc.url}")
	private String url;

	@Value("${jdbc.username}")
	private String username;

	@Value("${jdbc.password}")
	private String password;

	@Value("${hibernate.dialect}")
	private String hibernateDialect;

	@Value("${hibernate.show_sql}")
	private String hibernateShowSql;

	@Value("${hibernate.hbm2ddl.auto}")
	private String hibernateHbm2ddlAuto;

	@Value("${hibernate.format_sql}")
	private String hibernateFormatSql;

	@Value("${hibernate.c3p0.minPoolSize}")
	private String hibernateMinPoolSize;

	@Value("${hibernate.c3p0.maxPoolSize}")
	private String hibernateMaxPoolSize;

	@Value("${hibernate.c3p0.timeout}")
	private String hibernateTimeOut;

	@Value("${hibernate.c3p0.max_statement}")
	private String hibernateMaxStmnt;

	@Value("${hibernate.c3p0.testConnectionOnCheckout}")
	private String hibernateTestConnectionOnCheckout;

	@Value("${hibernate.temp.use_jdbc_metadata_defaults}")
	private String hibernateMetadataDefaults;

	@Bean
	public DataSource getDataSource() {
		DriverManagerDataSource ds = new DriverManagerDataSource();
		ds.setDriverClassName(driverClassName);
		ds.setUrl(url);
		ds.setUsername(username);
		ds.setPassword(password);
		return ds;
	}

	@Bean
	@Autowired
	public HibernateTransactionManager transactionManager(SessionFactory sessionFactory) {
		HibernateTransactionManager htm = new HibernateTransactionManager();
		htm.setDataSource(getDataSource());
		htm.setSessionFactory(sessionFactory);
		return htm;
	}

	@Bean
	@Autowired
	public HibernateTemplate getHibernateTemplate(SessionFactory sessionFactory) {
		HibernateTemplate hibernateTemplate = new HibernateTemplate(sessionFactory);
		hibernateTemplate.setCheckWriteOperations(false);
		return hibernateTemplate;
	}

	@Bean
	public LocalSessionFactoryBean getSessionFactory() {
		LocalSessionFactoryBean asfb = new LocalSessionFactoryBean();
		asfb.setDataSource(getDataSource());
		asfb.setHibernateProperties(getHibernateProperties());
		asfb.setPackagesToScan(new String[] { "com.polus.fibicomp.*" });
		// asfb.setAnnotatedClasses(new Class<?>[]{PrincipalBo.class});
		return asfb;
	}

	@Bean
	public Properties getHibernateProperties() {
		Properties properties = new Properties();
		properties.put("hibernate.dialect", hibernateDialect);
		properties.put("hibernate.show_sql", hibernateShowSql);
		properties.put("hibernate.hbm2ddl.auto", hibernateHbm2ddlAuto);
		properties.put("hibernate.format_sql", hibernateFormatSql);

		properties.put("hibernate.c3p0.minPoolSize", hibernateMinPoolSize);
		properties.put("hibernate.c3p0.maxPoolSize", hibernateMaxPoolSize);
		properties.put("hibernate.c3p0.timeout", hibernateTimeOut);
		properties.put("hibernate.c3p0.max_statement", hibernateMaxStmnt);
		properties.put("hibernate.c3p0.testConnectionOnCheckout", hibernateTestConnectionOnCheckout);
		properties.put("hibernate.temp.use_jdbc_metadata_defaults", hibernateMetadataDefaults);
		return properties;
	}
}
