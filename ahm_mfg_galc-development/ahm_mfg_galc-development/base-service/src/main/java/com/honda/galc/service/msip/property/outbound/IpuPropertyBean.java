package com.honda.galc.service.msip.property.outbound;

import com.honda.galc.property.PropertyBeanAttribute;

public interface IpuPropertyBean extends MsipOutboundPropertyBean {

	/**
	 * Specifies interfaceId for the task
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public String getInterfaceId();

	@PropertyBeanAttribute(defaultValue = "")
	public String getXmlFileName();

	/**
	 * Specifies the FTP Server host
	 * 
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public String getFtpServer();

	/**
	 * Specifies the FTP Port on server
	 * 
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "21")
	public int getFtpPort();

	/**
	 * Specifies Ftp login user name
	 * 
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public String getFtpUser();

	/**
	 * Specifies Ftp login password
	 * 
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public String getFtpPassword();

	@PropertyBeanAttribute(defaultValue = "")
	public String getFtpPath();

	@PropertyBeanAttribute(defaultValue = "")
	public String getFtpFileName();

	@PropertyBeanAttribute(defaultValue = "")
	public String getKeySetPartName();

	@PropertyBeanAttribute(defaultValue = "")
	public String getProcessPoint();

	@PropertyBeanAttribute(defaultValue = "")
	public String getLastProcessTimestamp();

	@PropertyBeanAttribute(defaultValue = "")
	public String getEndTimestamp();

	@PropertyBeanAttribute(defaultValue = "")
	public String getIpuPartName();

	@PropertyBeanAttribute(defaultValue = "")
	public String getBatteryPartName();

	@PropertyBeanAttribute(defaultValue = "")
	public String getIpuqaFilenamePrefix();

	@PropertyBeanAttribute(defaultValue = "")
	public String getIpuletFilenamePrefix();

	@PropertyBeanAttribute(defaultValue = "")
	public String getPlantCode();

	@PropertyBeanAttribute(defaultValue = "true")
	public boolean isSendLet();

	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isVinRequired();

}
