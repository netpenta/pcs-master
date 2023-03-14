package th.co.pentasol.pcs.master.configuration;

import lombok.Data;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import javax.sql.DataSource;

@SuppressWarnings("all")
@Data
@Configuration
@ComponentScan
@EnableTransactionManagement
@ConfigurationProperties(prefix = "spring.datasource")
public class SpringJdbcConfig {
	private String driverClassName;
	private String url;
	private String username;
	private String password;

    @Bean
	public DataSource getDataSource() {
	    BasicDataSource dataSource = new BasicDataSource();
		dataSource.setDriverClassName(driverClassName);
	    dataSource.setUrl(url);
	    dataSource.setUsername(username);
	    dataSource.setPassword(password);
	    return dataSource;
	}

	@Bean(name="jdbcTemplate")
	public JdbcTemplate jdbcTemplate() {
    	return new JdbcTemplate(getDataSource());
	}

	@Bean(name="namedParameterJdbcTemplate")
	public NamedParameterJdbcTemplate namedParameterJdbcTemplate() {
		return new NamedParameterJdbcTemplate(getDataSource());
	}

	@Bean(name="transactionManager")
	public PlatformTransactionManager txManager() {
		DataSourceTransactionManager transactionManager = new DataSourceTransactionManager(getDataSource());
	    return transactionManager;
	}	
}
