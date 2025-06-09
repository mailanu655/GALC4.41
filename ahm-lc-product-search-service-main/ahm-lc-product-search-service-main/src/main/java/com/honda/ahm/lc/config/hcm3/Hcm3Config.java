package com.honda.ahm.lc.config.hcm3;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import com.honda.ahm.lc.config.BaseConfig;

/**
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * <p>
 * <code>Hcm2Config</code> is ... .
 * </p>
 * <h4>Usage and Example</h4>
 * <h4>Special Notes</h4>
 * 
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Update by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * <TR>
 * <TD>&nbsp;</TD>
 * <TD>&nbsp;</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Release</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author Hemant Rajput
 * @created Apr 12, 2022
 */

@Configuration(value = "hcm3Config")
@ConfigurationProperties(prefix = "hcm3")
public class Hcm3Config extends BaseConfig {

	@Bean(name = "hcm3EntityManagerFactory")
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(@Qualifier("hcm3DataSource") DataSource datasource) {
		return createEntityManagerFactory(datasource);
	}

	@Bean(name = "hcm3DataSource")
	@ConfigurationProperties(prefix = "hcm3.spring.datasource")
	public DataSource dataSource() {
		return DataSourceBuilder.create().build();
	}

	@Bean(name = "hcm3TransactionManager")
	public PlatformTransactionManager transactionManager(@Qualifier("hcm3EntityManagerFactory") LocalContainerEntityManagerFactoryBean entityManager) {
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(
				entityManager.getObject());
		return transactionManager;
	}

}