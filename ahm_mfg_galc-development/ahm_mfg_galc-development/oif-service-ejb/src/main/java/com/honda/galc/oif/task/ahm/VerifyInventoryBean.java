package com.honda.galc.oif.task.ahm;

import com.honda.galc.property.IProperty;
import com.honda.galc.property.PropertyBean;
import com.honda.galc.property.PropertyBeanAttribute;

@PropertyBean(componentId = "OIF_AHM_SHIPPING_LOG")
public interface VerifyInventoryBean extends IProperty {
	/**
	 * Specifies interfaceId for the task
	 */
	@PropertyBeanAttribute(defaultValue = "D--GMG#HMAGAL#AHM000")
	public String getInterfaceId();
	
	/**
	 * Specifies the FTP Server host
	 * @return
	 */
//	@PropertyBeanAttribute(defaultValue = "207.130.207.177")
	@PropertyBeanAttribute(defaultValue = "")
	public String getFtpServer();
	
	/**
	 * Specifies the FTP Port on server
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "21")
	public int getFtpPort();
	
	/**
	 * Specifies Ftp login user name 
	 * @return
	 */
//	@PropertyBeanAttribute(defaultValue = "hmaftp1")
	@PropertyBeanAttribute(defaultValue = "")
	public String getFtpUser();
	
	/**
	 * Specifies Ftp login password
	 * @return
	 */
//	@PropertyBeanAttribute(defaultValue = "ftphma")
	@PropertyBeanAttribute(defaultValue = "")
	public String getFtpPassword();
	
	@PropertyBeanAttribute(defaultValue = "01.05.00.000000")
	public String getCutoffTime();
	
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isPrintMatch();

	@PropertyBeanAttribute(defaultValue = "")
	public String getSearchPath();

	@PropertyBeanAttribute(defaultValue = "")
	public String getRunTimestamp();

	@PropertyBeanAttribute(defaultValue = "")
	public String getInventoryInterfaceId();

	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isUseFtp();

	@PropertyBeanAttribute(defaultValue = "")
	public String getFtpPath();

	@PropertyBeanAttribute(defaultValue = "")
	public String getEmailSubject();

	@PropertyBeanAttribute(defaultValue = "")
	public String getEmailDistributionList();

}
