package com.honda.galc.property;

/**
 * @author Subu Kathiresan
 * @date Feb 24, 2017
 * 
 * Using prop_LDAP as componentId to be backward compatible
 */
@PropertyBean(componentId ="prop_LDAP")
public interface LdapPropertyBean extends IProperty{
	
	/**
	 * administrator id. 
	 * @return
	 */
	@PropertyBeanAttribute()
	public String getAdminId();
	
	/**
	 * administrator password
	 * @return
	 */
	@PropertyBeanAttribute()
	public String getAdminPassword();
	
	/**
	 * Default group
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "77")
	public String getDefaultGroup();
	
	/**
	 * LDAP attributes
	 * @return
	 */
	@PropertyBeanAttribute()
	public String getAttr();
	
	/**
	 * LDAP server url
	 * @return
	 */
	@PropertyBeanAttribute()
	public String getLdapUrl();
	
	/**
	 * LDAP scope
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "0")
	public Integer getScope();
	
	/**
	 * LDAP search base
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public String getSearchBase();
	
	/**
	 * GALC application security groups filter
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "APP-GALC")
	public String[] getUserSecurityGroupFilter();
}
