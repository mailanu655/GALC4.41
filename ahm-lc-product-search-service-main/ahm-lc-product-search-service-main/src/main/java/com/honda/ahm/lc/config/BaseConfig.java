package com.honda.ahm.lc.config;

import java.util.HashMap;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

/**
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * <p>
 * <code>SiteConfig</code> is ... .
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

public abstract class BaseConfig {

	private String defaultSchema;
	private String hibernateDialect = "org.hibernate.dialect.DB2Dialect";

	private String siteId;
	
	private List<String> divList;
	private List<String> plantList;

	protected LocalContainerEntityManagerFactoryBean createEntityManagerFactory(DataSource dataSource) {
		LocalContainerEntityManagerFactoryBean em
		= new LocalContainerEntityManagerFactoryBean();
		em.setDataSource(dataSource);
		em.setPackagesToScan(
				new String[] { 
						"com.honda.ahm.lc.vdb.entity",
						"com.honda.ahm.lc.vdb.dao"
					});
		HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		em.setJpaVendorAdapter(vendorAdapter);
		HashMap<String, Object> properties = getJpaProperties();
		em.setJpaPropertyMap(properties);
		em.setPersistenceUnitName(getSiteId());

		return em;
	}

	// === get/set === //
	public String getSiteId() {
		return siteId;
	}

	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}

	public String getHibernateDialect() {
		return hibernateDialect;
	}

	public void setHibernateDialect(String hibernateDialect) {
		this.hibernateDialect = hibernateDialect;
	}

	public String getDefaultSchema() {
		return defaultSchema;
	}

	public void setDefaultSchema(String defaultSchema) {
		this.defaultSchema = defaultSchema;
	}
	
	public HashMap<String, Object> getJpaProperties() {
		HashMap<String, Object> properties = new HashMap<>();
		properties.put("hibernate.dialect", getHibernateDialect());
		properties.put("hibernate.default_schema", getDefaultSchema());
		properties.put("spring.jpa.show-sql", true);
		properties.put("hibernate.format_sql", true);
		return properties;
	}

	public List<String> getDivList() {
		return divList;
	}

	public void setDivList(List<String> divList) {
		this.divList = divList;
	}

	public List<String> getPlantList() {
		return plantList;
	}

	public void setPlantList(List<String> plantList) {
		this.plantList = plantList;
	}

}
