package com.honda.galc.property;

import java.util.Map;

/**
 * 
 * <h3>SystemPropertyBean</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> SystemPropertyBean description </p>
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
 * <TD>Nov 28, 2011</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD> 
 * </TR>  
 *
 * </TABLE>
 *    
 * @see
 * @version 0.1
 * @author Paul Chou
 * @since Nov 28, 2011
 */

/**
 * 
 * @author Gangadhararao Gadde
 * @date Aug 26, 2013
 */
/**
 * 
 * @author Gangadhararao Gadde
 * @date June 15, 2016
 */

@PropertyBean(componentId ="System_Info")
public interface SystemPropertyBean extends IProperty{
	
	/**
	 * Honda product site name
	 * @return
	 */
	@PropertyBeanAttribute
	public String getSiteName();
	
	/**
	 * Honda product site location code
	 * @return
	 */
	@PropertyBeanAttribute
	public String getPlantLocCode();
	
	/**
	 * Identification of Assembly line
	 * @return
	 */
	@PropertyBeanAttribute
	public String getAssemblyLineId();
	
	/**
	 * Indicate the product type 
	 * possible values are: ENGINE OR FRAME
	 */
	@PropertyBeanAttribute
	public String getProductType();
	
	
	/**
	 * Sales model type codes for VINs have no check digit.
	 * @return
	 */
	@PropertyBeanAttribute( defaultValue = "")
	public String[] getNoCheckDigitTypeCodes();
	
	/**
	 * If the system is enabled to change password
	 * @return
	 */
	@PropertyBeanAttribute( defaultValue = "false")
	public boolean isChangePasswordEnabled();
	
	/**
	 * get Locale Language
	 * @return
	 */
	@PropertyBeanAttribute( defaultValue = "")
	public String getLanguage();
	
	/**
	 * get Locale County
	 * @return
	 */
	@PropertyBeanAttribute( defaultValue = "")
	public String getCountry();
	
	/**
	 * servers URLs 
	 * in the format of: http://xxx.xxx.xxx.xxx/
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public Map<String,String> getServerUrls();
	
	@PropertyBeanAttribute(propertyKey= "NEW_LOG_SUFFIX",defaultValue = "")
	public String getNewLogSuffix();
	
	@PropertyBeanAttribute(propertyKey= "ENABLE_CLIENT_MONITOR",defaultValue = "TRUE")
	public boolean isClientMonitorEnabled();
	
	@PropertyBeanAttribute(defaultValue = "DEFAULT")
	public String getPartMaskWildcardFormat();
	
	@PropertyBeanAttribute(defaultValue = "true")
	public boolean isOutstandingFlagChangable();
	
	/**
	 *  If true the client will update the ip_address in the gal234tbx with the client ip addr 
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isAutoRegisterIpaddr();
	
	/**
	 *  When set to true the Mass Awareness Message widget will be displayed on at the bottom of the screen. (VIOS)
	 */
	@PropertyBeanAttribute(propertyKey="MASS_AWARENESS_MESSAGE_ENABLED",defaultValue = "false")
	public boolean isMassAwarenessMessageEnabled();
	
	/**
	 * When true GALC will authenticate the user id at login against LDAP
	 */
	@PropertyBeanAttribute(propertyKey="LDAP_FLAG", defaultValue = "false")
	public boolean isLdap();
	
	/**
	 * When true GALC will add non-exist the user to gal105tbx
	 */
	@PropertyBeanAttribute(defaultValue = "true")
	public boolean isCreateLocalLdapRecord();

	/**
	 * If set to true the associates training recored is checked 
	 */
	@PropertyBeanAttribute(propertyKey="TRAINING_VALIDITY_CHECK_ENABLED", defaultValue="false")
	public boolean isTrainingValidityCheckEnabled();
	
	/**
	 * If set to true MDRS Assignment check is enabled
	 */
	@PropertyBeanAttribute(propertyKey="MDRS_ASSIGNMENT_CHECK_ENABLED", defaultValue="false")
	public boolean isMdrsAssignmentCheckEnabled();
	
	/**
	 * Specifies the Team Manager and TeamCoordinator LDAP User group, used to validate training. (VIOS)
	 */
	@PropertyBeanAttribute(propertyKey="TMTC_USER_GROUP")
	public String getTmtcUserGroup();
	
	/**
	 * When true an alarm will sound if the PPE screen is not acknowledged by clicking the 'OK' button
	 * (VIOS)   
	 */
	@PropertyBeanAttribute(propertyKey="PPE_SCREEN_ALARM_ENABLED", defaultValue="false")
	public boolean isPpeScreenAlarmEnabled();
	
	/**
	 * Mass Awareness Message scrolling duration can be set in property table. Scrolling duration unit is seconds 
	 */
	@PropertyBeanAttribute(propertyKey="MAM_SCROLLING_DURATION", defaultValue="25")
	public int getMamScrollingDuration();
	
	/**
	 * Specifies no. of days up to which process changes will be fetched
	 */
	@PropertyBeanAttribute(defaultValue = "7")
	public int getProcessChangeHistoryDays();
	
	/**
	 * Specifies total no. of rows to be displayed for process changes
	 */
	@PropertyBeanAttribute(defaultValue = "10")
	public int getProcessChangeDisplayRows();

	/**
	 * Flag to indicate what is populated build result associate when the associate_no is absent
	 * true: using user Id;  otherwise: process point Id. default to user Id;
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "true")
	boolean isInstalledPartAssociateUsingUserId();
	
	/**
	 * Specifies number of milliseconds between
	 * push timer status updates
	 */
	@PropertyBeanAttribute(defaultValue = "30000")
	public int getPushTimerStatusUpdateInterval();
	
	@PropertyBeanAttribute(defaultValue="")
	String autoLoginUser();
	
	@PropertyBeanAttribute(defaultValue="false")
	boolean isAutoLogin();
	
	@PropertyBeanAttribute(defaultValue="true")
	boolean isClearRecentUser();

	/**
	 * Defines extra days to add for lot control clients(Headed and Headless) where we need to compare 
	 * the current date with scanned date. For Example; if the ExpirationDays is 10 days then the date 
	 * scan should not be more than 10 days old then the current date.
	 * 
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "0")
	int getExpirationDays();
	
	/**
	 * Specifies the Time Zone Id for the terminal. It does not have a default value.If the property is not set, we will use the timezone of client location.
	 * Please specify the TimeZone Id and not the timezone display name for this property.
	 */
	@PropertyBeanAttribute
	public String getDefaultTimeZoneId();
	
	/**
	 * Specifies characters to ignore from exterior color code while replacing part mask constants with actual value at run time
	 * see CommonPartUtility.replacePartMaskConstants
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public String getExtColorExceptionChars();

	
	/**
	 * Specifies characters to be removed from the beginning of a VIN scan
	 * Default will be I 
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "I")
	public String getLeadingVinCharsToRemove();

	
	/**
	 * Base URL to connect to MDRS Rest service
	 */
	@PropertyBeanAttribute(defaultValue = "http://mdrsqfsqa/MDRSQFSService/VIOS.svc/")
	public String getMdrsRestUrl();
	
	/**
	 * Timeout period for REST request before switching to manual override mode.
	 */
	@PropertyBeanAttribute(defaultValue = "5")
	public int getMdrsRestRequestTimeoutinSec();
	
	/**
	 * Number of days training data will be cached locally
	 */
	@PropertyBeanAttribute(defaultValue = "5")
	public int getTrainingDataChachedInDays();
	
	/**
	 * Max number of inactive days before the associate requires re-training
	 */
	@PropertyBeanAttribute(defaultValue = "5")
	public int getTrainingDataCachedDays();
	
	/**
	 * Is MDRS webservice available
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isMdrsServiceAvailable();


	/**
	 * Crystal report base url like http://naebofarm01qa:8080/CrystalReports/viewrpt.cwr?
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public String getProcessChangeReportUrl();
	

	/**
	 * Id of the process change report like 26052029
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public String getReportId();


	/**
	 * Environment to connect to like PDAUAT
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public String getCrystalReportSchema();


	/**
	 * user id to connect to crystal report enterprise server
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public String getCrystalReportUserid();


	/**
	 * passwordto connect to crystal report enterprise server
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public String getCrystalReportPassword();


	/**
	 * Crystal report schema name like HDCDEV29
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public String getCrystalReportCmsName();


	/**
	 * Authentication like windows or LDAP or Enterprise
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public String getCrystalReportAuthenticationType();
	
	/**
	 * The number of digit used for line Reference Number
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "0")
	public int getLineRefNumberOfDigits();
	
	/**
	 * Indicate if use input part sn or the parser result of the input part sn to 
	 * check against part mask
	 * default: true  
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "true")
	public boolean isUseParsedDataCheckPartMask();
	
	/**
	 * Regex expression that could be used to validate if specified product id on client inputs is valid or not  
	 * @return
	 */	
	@PropertyBeanAttribute(defaultValue = "^[\\-|\\s|A-Z|a-z|0-9]+$")
	public String getProductIdRegexValidator();
	
	/**
	 * tracking area
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public String getTrackingArea();
	
	/**
	 * Specifies characters to be removed from the beginning of a VIN scan
	 * Default will be I 
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "I")
	public String getLeadingCertLabelChars();
	
	/**
	 * Determines if a kickout is only for the process point which it was assigned or for the
	 * process point which is was assigned and any process point after. 
	 * 
	 * Note: This is a system level property and cannot be assigned on a process point level.
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isRecursiveKickout();
	
	
	
	/**
	 * Specifies Sales Model Type Code Which don't have Certlabel to scan 
	 * @return
	 */
	
	@PropertyBeanAttribute(defaultValue = "")
	public String getExcludeSalesModelType();
	
	@PropertyBeanAttribute(propertyKey="PRODUCT_SEARCH_SHIPPED_LINE_IDS", defaultValue = "")
	public String[] getProductSearchShippedLineIds();

	@PropertyBeanAttribute(propertyKey="AFONSEQ_MAX_LENGTH", defaultValue = "")
	public String getAFNoSequenceNumber();
	
	@PropertyBeanAttribute(propertyKey="HTTP_CONNECTION_TIMEOUT", defaultValue = "0")
	public int getHttpConnectionTimeout();
	
}
