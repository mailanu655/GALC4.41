package com.honda.galc.property;

import java.util.Map;


/**
 * 
 * <h3>ProductCheckPropertyBean Class description</h3>
 * <p> ProductCheckPropertyBean description </p>
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
 *
 * </TABLE>
 *   
 * @author Jeffray Huang<br>
 * Apr 19, 2012
 *
 *
 */
/**
 * 
 * @author Gangadhararao Gadde
 * @date May 04, 2016
 */
@PropertyBean(componentId = "DEFAULT_PRODUCT_CHECK")
public interface ProductCheckPropertyBean extends IProperty{
	
	/**
	 * A list of installed part
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public Map<String,String> getInstalledParts();

	/**
	 * A list of Installed Sealants
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public Map<String, String> getInstalledSealants();

	/**
	 * A list of product check types, separated by comma
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public String[] getProductCheckTypes();
	
	/**
	 * A list of product check types, separated by comma that may stop product from processing
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public String[] getProductNotProcessableCheckTypes();
	
	/**
	 * A list of Installed Product Check Types
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public String[] getInstalledProductCheckTypes();
	
	/**
	 * Installed Product Error Code
	 */
	@PropertyBeanAttribute(defaultValue = "0")
	public String getInstalledProductErrorCode();
	
	/**
	 * Installed Product Last Check Point
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public String getInstalledProductLastCheckPoint();

	/**
	 * A list of Product Input Check Types
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "INVALID_PRODUCT_ID_CHECK")
	public String[] getProductInputCheckTypes();
	
	/**
	 * A list of History Process Point Id
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public String[] getHistoryProcessPointIdList();

	@PropertyBeanAttribute(defaultValue = "")
	public String[] getProductResultExistProcessPointIds();
	
	@PropertyBeanAttribute(defaultValue = "")
	public String[] getPrevProdResultMissingPpids();
	
	@PropertyBeanAttribute(defaultValue = "")
	public String[] getProductResultMissingProcessPointIds();
	
	@PropertyBeanAttribute(defaultValue = "")
	public String[] getProductShippedProcessPointIds();
	/**
	 * A list of InstalledParts Check Process Points
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public String[] getInstalledPartsCheckProcessPoints();
	
	/**
	 * A list of Part for Cure Time Check
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public String[] getCureTimeCheckParts();
	
	/**
	 * Map for Minimum cure time, the key is the cure time check part name
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public Map<String, String> getCureTimeMin();
	
	/**
	 * Map for Maximum cure time, the key is the cure time check part name
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public Map<String, String> getCureTimeMax();
	
	/**
	 * It is a mapped property used by DEFECTS_EXIST_CHECK.  
	 * It defines comma delimited defect names to check for.
	 * It can be define on model level(map key). 
	 * @param claz
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public Map<String, String[]> getDefectsExistCheckDefectTypes(Class<String[]> claz);
	
	/**
	 * It is a mapped property used by PARTS_EXIST_CHECK.  
	 * It defines comma delimited part names to check for.
	 * It can be define on model level (map key).  
	 * @param claz
	 * @return
	 */	
	@PropertyBeanAttribute(defaultValue = "")
	public Map<String, String[]> getPartsExistCheckPartNames(Class<String[]> claz);
	
	/*use ALT_ENGINE_MTO column for engine type verification*/
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isUseAltEngineMto();
	
	/**
	 * list of Divisions to exclude from installed part check
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "NONE")
	public String[] getInstalledPartsCheckExcludeDivisions();
	
	/**
	 * list of lines to exclude from installed part check
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "NONE")
	public String[] getInstalledPartsCheckExcludeLines();
	
	/**
	 * HMIN legacy code still uses HMIN_MEASUREMENT_ATTEMPTTBX, and
	 * will use this property to override the measurement table used
	 * in installed part check
	 * @return
	 */
	@PropertyBeanAttribute()
	public String getMeasurementTable();
	
	/**
	 * indicates whether or not a popup should appear to acknowledge a failed check(s)
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isFailedAckRequired();
	
	/**
	 * indicates whether or not to check the last
	 * measerement attempt
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isCheckLastMeasurementAttempt();
	
	/**
	 * indicates whether or not to use LotControlRule.getPartConfirmFlag()
	 * to filter the check results
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isUsePartConfirmFlag();
	
	/**
	 * indicates whether to return process sequence or Line Sequence
	 * while kickout check
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isKickoutByProcessLevel();
	
	/**
	 * Specify a process point of a sub product type to check the sub product such as required parts
	 * @return
	 */
	@PropertyBeanAttribute (defaultValue = "")
	public Map<String, String> getSubProductCheckProcessPoint();
	
	/**
	 * If true, Line Check will be enabled for NAQ
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	boolean isLineIdCheckEnabled();
	
	/**
	 * If true, product history will be verified for list of process points provided in HISTORY_PROCESS_POINT_ID_LIST
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	boolean isProductHistoryNotExistCheck();
	
	/**
	 * Last Passing Process Points
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public String getLastPassingProcessPoint();

	/**
	 * On Hold reason that when product put on hold for spec changed
	 * @return
	 */
	@PropertyBeanAttribute (defaultValue = "SPEC CHECK")
	public String getSpecCheckHoldReason();

	/**
	 * Indicate how to find the previous product when doing product spec check
	 * default to true  - from In process product; use product sequence when it's false
	 * @return
	 */
	@PropertyBeanAttribute (defaultValue = "InProcessProduct")
	public String getSpecCheckProductSequenceType();
	
	/**
	 * battery expiration in days
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue="0")
	public int getExpirationCheckInDays();
	
	/**
	 * List of valid tracking status
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public String getWarningTrackingStatus();
	
	/**
	 * List of valid station display codes
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public String getStationDisplayCodes();
	
	/**
	 * Lot Change Indicator
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public String getLotChangeIndicator();
	
	/**
	 * Validate Cert label is scanned or not.
	 * @return
	 */
	@PropertyBeanAttribute (defaultValue = "false")
	public boolean isCertLabelCheck();
	
	/**
	 * checker weather to trim special characters in LET data or not for comapring.
	 * @return
	 */
	@PropertyBeanAttribute (defaultValue = "false")
	public boolean isTrimSpecialCharacters();
	
	@PropertyBeanAttribute(defaultValue = "")
	public String getElevatedSecurityGroup();
	
	@PropertyBeanAttribute (defaultValue = "false")
	public boolean isElevatedUserPasswordRequired();
	
	@PropertyBeanAttribute(defaultValue = "")
	public String getWarningTrackingStatusElevatedSecurityGroup();
	
	@PropertyBeanAttribute (defaultValue = "false")
	public boolean getWarningTrackingStatusElevatedUserPasswordRequired();
	
	/**
	 * Determines if a NAQ defect with a kickout should be included in the check for NAQ
	 * defect or not.
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "true")
	public boolean isKickoutDefect();
	
	/**
	 * A list of product check types, separated by comma that 
	 * warn if product not mass production 
	 * else error out if mass production
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public String[] getWarnIfNotMassProductionProductCheckTypes();
		
	/**
	 * Build Result Part Name for installed engine 
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue="")
	public String getInstalledEnginePartName();
	
	/**
	 * Get tilt range
	 */
	@PropertyBeanAttribute(defaultValue = "0.5")
	public double getTiltRange();
	
	/**
	 * Hold type for Spec Check Hold
	 * @return
	 */
	@PropertyBeanAttribute (defaultValue = "HOLD_AT_SHIPPING")
	public String getSpecCheckHoldType();
	
	/**
	 * Scrap Process point id
	 * @return
	 */
	@PropertyBeanAttribute (defaultValue = "")
	public String getScrapProcessPoint();
	

	/**
	 * Part Name for Spec Check to create defect
	 * @return
	 */
	@PropertyBeanAttribute (defaultValue = "SPEC CHECK")
	public String getSpecCheckDefectPartName();

	/**
	 * alternative process point id to create defect
	 * @return
	 */
	@PropertyBeanAttribute (defaultValue = "")
	public String getSpecCheckDefectProcessPointId();
	
	/**
	 * Determines if create NAQics Defect when Spec change
	 * defect or not.
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isCreateDefectOnSpecChange();
	
	/**
	 * Determines Hold product when Spec change
	 * defect or not.
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isHoldProductOnSpecChange();
	
	/**
	 * Flag stragglers for spec check
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isIncludeStragglersForSpecCheck();
	
	/**
	 * Flag remakes for spec check
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isIncludeRemakesForSpecCheck();
	
	/**
	 * Product Spec Check YMTOCI
	 *
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public String getCheckSpec();
	
	/**
	 * External checker REST url
	 * @return
	 */
	@PropertyBeanAttribute (defaultValue = "")
	public String getExtCheckerRestUrl();
	
	/**
	 * External checker REST authorization code
	 * @return
	 */
	@PropertyBeanAttribute (defaultValue = "c2VydmljZV9tc2FfZ2FsY19xYTpCaXJkIzQxMg==")
	public String getExtCheckerRestAuthCode();
	
	@PropertyBeanAttribute(defaultValue = "true")
	public boolean isUseExtProductHoldCheck();

	/**
	 * Required Part check Map for multiple 
	 * exmaple {HEAD} = MC0HD16001
	 * @return
	 */
	@PropertyBeanAttribute (defaultValue = "")
	public Map<String, String> getRequiredPartProcessPointIdMap();

	/**
	 * Return a map of checks should be performed on the installed products such as block, heads, conrods and etc... 
	 * 
	 * Key: Product type name 
	 * Value: Common separated checks (See following example)
	 * 
	 *  PROPERTY_KEY							PROPERTY_VALUE
	 *  INSTALLED_PRODUCT_CHECK_MAP{BLOCK}		ENGINE_BLOCK_ON_HOLD_CHECK
	 *  INSTALLED_PRODUCT_CHECK_MAP{CONROD}		VALID_CONROD_ID_NUMBER_CHECK
	 */
	@PropertyBeanAttribute
	public Map<String, String> getInstalledProductCheckMap();
	
	@PropertyBeanAttribute (defaultValue = "CBU")
	public String getHoldToDefectTaskPartName();
	
	@PropertyBeanAttribute (defaultValue = "HOLD VQ")
	public String getHoldToDefectTaskDefectName();
	
	@PropertyBeanAttribute (defaultValue = "")
	public Map<String, String> getHoldToDefectTaskSuffixByDpt();
}
