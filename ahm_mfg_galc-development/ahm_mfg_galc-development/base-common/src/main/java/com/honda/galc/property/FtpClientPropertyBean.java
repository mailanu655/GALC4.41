package com.honda.galc.property;

import java.util.List;

public interface FtpClientPropertyBean extends IProperty{
	
	/**
	 * Specifies Ftp login user name 
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public String getFtpUser();
	
	/**
	 * Specifies Ftp login password
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public String getFtpPassword();
	
	/**
	 * Specifies Ftp output file name
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public String getOutputFile();
	
	/**
	 * Specifies Ftp input file name list
	 * @return
	 */
	public List<String> getInputFileList();
	
	/**
	 * data element delimiter 
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = ",")
	public String getDelimiter();
	
	/**
	 * Specifies the FTP Server host
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public String getFtpServer();
	
	/**
	 * Specifies the FTP Port on server
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "21")
	public int getFtpPort();
	
	@PropertyBeanAttribute(defaultValue = "")
	public String getFtpPath();

	@PropertyBeanAttribute(defaultValue = "FTP")
	public String getFtpType();
	
	@PropertyBeanAttribute(defaultValue = "")
	public String getFilenamePrefix();
	
	@PropertyBeanAttribute(defaultValue = "")
	public String getProcessLocation();

	@PropertyBeanAttribute(defaultValue = "")
	public String getPlantCode();
	
}
