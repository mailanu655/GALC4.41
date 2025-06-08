package com.honda.galc.oif.property;

import java.util.Map;

import com.honda.galc.entity.EntityBuildPropertyBean;
import com.honda.galc.property.PropertyBean;
import com.honda.galc.property.PropertyBeanAttribute;

/**
 * <h3>OifTaskPropertyBean</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> OifTaskPropertyBean description </p>
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
 * <TD>P.Chou</TD>
 * <TD>Dec 2, 2011</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD> 
 * </TR>  
 * </TABLE>
 *    
 * @see
 * @version 0.1
 * @author Paul Chou
 * @since Jun. 13, 2014
 */

@PropertyBean(componentId ="Default_OifTask")
public interface OifTaskPropertyBean extends EntityBuildPropertyBean {
	
	
	/**
	 * Specifies the file location on server
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public String getScheduleDir();
	
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
	@PropertyBeanAttribute(defaultValue = "0")
	public int getFtpPort();
	
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
	 * Define a list of schedule files to be processed 
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public String getFileList();
	
	/**
	 * Specifies field in the schedule file mapping to column in database
	 * Example:
	 * PO1.txt,PO2.txt=MBPN_TBX,GAL212TBX,GAL217TBX
	 * Each line in PO1.tbx or PO2.tbx will create a row in table MBPN, GAL212TBX and GAL217TBX
	 * @return
	 */
	@PropertyBeanAttribute    
	public Map<String, String> getFileTableMapping();
	
	/**
	 * Specifies the Valid Check Digits
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "0")
	public String[] getValidCheckDigits();
	
	@PropertyBeanAttribute    
	public Map<String, String> getNameMapping();

	/**
	 * Tracking process point id 
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public String getTrackingProcessPointId();
	
	/**
	 * Ipp tag scanned map
	 * @return
	 */
	@PropertyBeanAttribute    
	public Map<String, String> getIppScannedOverride();
	
}
