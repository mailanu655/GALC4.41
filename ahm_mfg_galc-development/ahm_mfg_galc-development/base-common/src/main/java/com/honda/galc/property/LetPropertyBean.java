package com.honda.galc.property;

import java.util.Map;

/**
 * 
 * @author Gangadhararao Gadde
 * @date Aug 26, 2013
 */
@PropertyBean
public interface LetPropertyBean extends IProperty{

	/**
	 * List of Program Categories
	 */
	@PropertyBeanAttribute(defaultValue ="")
	public String[] getLetPgmCategories();
		
	/**
	 * Data Encoding type
	 */
	@PropertyBeanAttribute(defaultValue ="UTF-8")
	public String getDbEncoding();
	
	/**
	 * Download date range in days
	 */
	@PropertyBeanAttribute(defaultValue ="31")
	public String getDownloadDateRange();
	
	/**
	 * Pre-operational date Range in days
	 */
	@PropertyBeanAttribute(defaultValue ="31")
	public String getPreoperationalDateRange();
	
	/**
	 * Diagnosis Result search range in days
	 */
	@PropertyBeanAttribute(defaultValue ="7")
	public String getDiagnosisResultSearchPeriod();
	
	/**
	 * LET socket connection timeout in milliseconds
	 */
	@PropertyBeanAttribute(defaultValue="3000")
	int getConnectionTimeout();
	
	/**
	 * Line name to be used in the xml file save path
	 */
	@PropertyBeanAttribute(defaultValue="1LINE")
	String getLineName();
	
	/**
	 * Process location
	 */
	@PropertyBeanAttribute(defaultValue ="VQ")
	public String getProcessLocation();
	
	/**
	 * Fail display
	 */
	@PropertyBeanAttribute(defaultValue ="X")
	public String getLetFailDisplay();
	
	/**
	 * Fail sign
	 */
	@PropertyBeanAttribute(defaultValue ="Fail")
	public String getLetFailSign();
	
	/**
	 * Fault code
	 */
	@PropertyBeanAttribute(defaultValue ="X")
	public String getLetFaultCode();
	
	/**
	 * Fault result output period days
	 */
	@PropertyBeanAttribute(defaultValue ="31")
	public String getFaultResultOutputDtPeriod();
	
	/**
	 * Fault result max vin count
	 */
	@PropertyBeanAttribute(defaultValue ="100")
	public String getFaultResultMaxVinCount();
	
	/**
	 * Fault result report size
	 */
	@PropertyBeanAttribute(defaultValue ="letter")
	public String getFaultResultReportSize();
	
	/**
	 * Printer template path
	 */
	@PropertyBeanAttribute(defaultValue ="resource/let_printer_fop_templates/")
	public String getPrinterTemplatePath();
	
	/**
	 * LET Check type
	 */
	@PropertyBeanAttribute(defaultValue ="")
	public String getLetCheckType();
	
	/**
	 * LET Alert mail recipients
	 * comma separated sequence of addresses. Addresses must follow RFC822 syntax.
	 */
	@PropertyBeanAttribute(defaultValue ="")
	public String getAlertMailRecipients();
	
	/**
	 * Is send VIN in response
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isSendVinInResponse();
	
	/**
	 * perform LET checks when XML is received
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isPerformLetChecks();

	/**
	 * LET environment name 
	 */
	@PropertyBeanAttribute(defaultValue = "MassPro")
	public String getEnvName();

	/**
	 * LETsocket server default server address
	 */
	@PropertyBeanAttribute(defaultValue = "localhost")
	public String getHostName();

	/**
	 * LET spool manager's maximum number of records to be shown at a time
	 */
	@PropertyBeanAttribute(defaultValue = "1000")
	public int getMaxRecords();
	
	/**
	 * LET length check
	 */
	@PropertyBeanAttribute (defaultValue = "17")
	int getFrameLength();
	
	/**
	 * LET length check
	 */
	@PropertyBeanAttribute (defaultValue = "11")
	int getFrameJpnLength();
	
	/**
	 * LET length check
	 */
	@PropertyBeanAttribute (defaultValue = "17")
	int getMbpnLength();
	
	/**
	 * LET length check
	 */
	@PropertyBeanAttribute (defaultValue = "8")
	int getIpuLength();
	
	/**
	 * LET server default mfg ID
	 */
	@PropertyBeanAttribute(defaultValue = "A01")
	public String getDefaultMfgId();

	/**
	 * LET server default upload target
	 */
	@PropertyBeanAttribute(defaultValue = "A55")
	public String getDefaultUploadTarget();

	/**
	 * LET server default upload terminal
	 */
	@PropertyBeanAttribute(defaultValue = "T01")
	public String getDefaultUploadTerminal();

	/**
	 * LET server default xml directory
	 */
	@PropertyBeanAttribute(defaultValue = "d:\\xml")
	public String getDefaultUploadXmlLocation();

	/**
	 * Default auto refresh time(in milliseconds) for LET data
	 */
	@PropertyBeanAttribute(defaultValue = "10000")
	public String getDefaultAutoRefreshTime();
	
	/**
	 *  LET terminal to installed part name mapping
	 *  key   = INSTALLED_PART_NAME{A21}
	 *  value = IMMOBILIZER,A000
 	 */
	@PropertyBeanAttribute
	public Map<String,String> getInstalledPartName();
	
	/**
	 * LET terminal to process point mapping 
	 * key    = LET_TERMINAL_PROCESS_POINT{T01}
	 * value  = PP1001
	 */
	@PropertyBeanAttribute
	public Map<String,String> getLetTerminalProcessPoint();
	
	/**
	 *  Property map for broadcasts
	 *  key   = SEND_BROADCAST{T01,0}
	 *  value = TRUE
	 */
	@PropertyBeanAttribute
	public Map<String,String> getSendBroadcast();
	
    /**
     * Page sizes for results paginated table
     * 
     * @return
     */
    @PropertyBeanAttribute(defaultValue = "25,50,100,500,1000")
    public Integer[] getResultPageSizes();

    /**
     * Default page size for result paginated table, This value should be included
     * in property getResultPageSizes().
     * 
     * @return
     */
    @PropertyBeanAttribute(defaultValue = "500")
    public int getDefaultResultPageSize();
    
    /**
     * Start and cut off time/hour for let result download day
     * 
     * @return
     */
    @PropertyBeanAttribute(defaultValue = "2")
    public int getLetDownloadStartTime();
    
    
    /**
     * Controls if created files should be put into zone subfolders.
     * @return
     */
    @PropertyBeanAttribute(defaultValue = "false")
    public boolean isGroupFilesByZone();    

	/**
	 * LET microservice URL
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public String getLetMicroserviceUrl();
}
