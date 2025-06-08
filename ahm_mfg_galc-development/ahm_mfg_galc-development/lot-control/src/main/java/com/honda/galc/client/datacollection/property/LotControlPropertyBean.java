package com.honda.galc.client.datacollection.property;

import java.util.Map;

import com.honda.galc.property.EngineMissionAssignPropertyBean;
import com.honda.galc.property.PropertyBean;
import com.honda.galc.property.PropertyBeanAttribute;
/**
 *
 * <h3>LotControlPropertyBean</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> LotControlPropertyBean description </p>
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
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>YX</TH>
 * <TH>2014.05.2</TH>
 * <TH>1.14</TH>
 * <TH>SR30947</TH>
 * <TH>add property of timeout observer</TH>
 * </TR>
 *
 * </TABLE>
 *
 * @author Paul Chou
 * Apr 23, 2010
 *
 */
 /** * *
 * @version 0.2
 * @author Gangadhararao Gadde
 * @since Aug 09, 2012
 */
@PropertyBean(componentId ="Default_LotControl")
public interface LotControlPropertyBean extends CommonPropertyBean, EngineMissionAssignPropertyBean {

	/**
	 * flag indicates whether the LotControl station is headed or headless
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	boolean isHeadless();

	/**
	 * Flag indicates If check product Id is already process when validate
	 * product id
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	boolean isCheckProcessedProduct();

	/**
	 * Flag to indicates if to check all required parts have been installed
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	boolean isCheckRequiredPart();
	/**
	 * Flag indicates if need to send data collection status to PLC
	 * @return
	 */
	boolean isSendStatusToPlc();

	/**
	 * Flag to send data collection complete as 1 to plc to release product
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	boolean isSendReleaseCode();

	/**
	 * State behavor on missing required part
	 * True: Product Id validation fail - need another product Id
	 * False: Product Id validation complete - show warning message to user
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	boolean isFailOnMissingRequiredPart();

	/**
	 * Flag indicates if save measurement attempt history into data base
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	boolean isSaveMeasurementHistory();

	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isSavePartHistory();
	//get default processors
	String getProductIdProcessor();
	String getPartSnProcessor();
	String getTorqueProcessor();

	@PropertyBeanAttribute(defaultValue = "com.honda.galc.client.datacollection.processor.RefreshProcessor")
	String getRefreshProcessor();

	String getPrintsProcessor();

	@PropertyBeanAttribute
	public Map<String, String> getObservers();

	@PropertyBeanAttribute
	public Map<String, String> getPartMarkCheck();

	/**
	 * Flag indicate skip multiple products is allowed when skip product
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "true")
	boolean isSkipMultipleProducts();
	
	/**
	 * Skip to next expected product from error dialog
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	boolean isSkipToNextFromErrorDialog();

	/**
	 * Flag indicates if supporting Product Id sent from PLC
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "true")
	boolean isProductIdFromPlc();
	
	/**
	 * Flag indicates if supporting Product Id sent from PLC
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	boolean isQueueProductId();
	
	/**
	 * Flag indicates if supporting Product Id auto refresh signal sent from PLC
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	boolean isProductIdRefreshFromPlc();

	/**
	 * Flag indicates if supporting Part Serial Number sent from PLC
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	boolean isPartSerialNumberFromPlc();

	/**
	 * Flag to indicate the Torque Data is sent from Open protocol device driver.
	 * true  - torque data send from open protocol device driver
	 * false - torque data from EI interface
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "true")
	boolean isTorqueDataFromDeviceDriver();

	 /**
	  * Flag indicate if save next product id into the expected product table;
	  * false means current product is saved into the expected product table
	  *
	  * @return
	  */
	@PropertyBeanAttribute(defaultValue = "true")
	boolean isSaveNextProductAsExpectedProduct();

	@PropertyBeanAttribute(defaultValue = "false")
	boolean isExpectedProductSequenceResetAllowed();

	@PropertyBeanAttribute(defaultValue = "true")
	boolean isCheckTighteningId();

	@PropertyBeanAttribute(defaultValue = "true")
	boolean isCheckTorqueProductId();


	@PropertyBeanAttribute(defaultValue = "")
	String getExcludePartsToSave();
	@PropertyBeanAttribute(defaultValue = "")
	String getSideCheckingParts();

	@PropertyBeanAttribute(defaultValue = "")
	String getOnProcessPointId();

	/**
	 * save skipped product to database table
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	boolean isSaveSkippedProduct();

	@PropertyBeanAttribute(defaultValue = "false")
	boolean isSaveSkippedExpectedProductOnly();

	/**
	 * Save the last passing product id into expected product table to provide
	 * data for line side monitor.
	 * Notes, this attribute should NOT turn on if next expected product is enabled.
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	boolean isSaveLastProduct();

	/**
	 * Filter for contents to be cached from build attribute table
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "KNUCKLE_PART_NUMBER_")
	String getBuildAttributePrefix();

	@PropertyBeanAttribute(defaultValue = "001") //WED 20170629 set default test torque to "001"
	String getTestTorquePset();

	@PropertyBeanAttribute(defaultValue = "KN")
	String getProcessLocation();

	@PropertyBeanAttribute(defaultValue = "6") //6 for knuckles
	int getSnDigits();

	@PropertyBeanAttribute(defaultValue = "skipPart")
	String getSkipPartActionCommand();
	/**
	 * flag to indicate this client is combined with Qics client
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	boolean isLotctrAndQics();

	/**
	 * flag indicate if call Qics Service to create Qics defect
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	boolean isAutoUpdateQics();

	/**
	 * flag to indicate what is populated into installed part associate
	 * Two styles: 1. using user Id;
	 *             2. using process point Id.
	 * by default user Id
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "true")
	boolean isInstalledPartAssociateUsingUserId();

	@PropertyBeanAttribute(defaultValue = "false")
	boolean isLimitRulesByDivision();


	/**
	 * flag to indicate if save uncompleted build results for skipped product
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "true")
	boolean isSaveBuildResultsForSkippedProduct();

	/**
	 * Flag to reset next expected product when processing a previously processed product
	 */
	@PropertyBeanAttribute(defaultValue = "true")
	boolean isResetNextExpectedProduct();

	/**
	 * SR30947 Timeout for Lot control Process
	 */
	@PropertyBeanAttribute(defaultValue="-1")
	int getLotProcessingTimeout();

	/**
	 * Flag to check out of sequence
	 */
	@PropertyBeanAttribute (defaultValue = "false")
	boolean isCheckOutOfSequence();

	@PropertyBeanAttribute(defaultValue = "")
	String getPartNames();

	@PropertyBeanAttribute(defaultValue = "")
	String getKeyScanOps();

	@PropertyBeanAttribute(defaultValue = "PANA*1")
	String getFirstKeyPartMask();

	@PropertyBeanAttribute(defaultValue = "PANA*2")
	String getSecondKeyPartMask();

	@PropertyBeanAttribute(defaultValue="false")
	boolean isShowScanOnlyErrorDialog();

	@PropertyBeanAttribute(defaultValue="false")
	boolean isShowTestTorque();

	@PropertyBeanAttribute(defaultValue="false")
	boolean isShowErrorDialog();

	@PropertyBeanAttribute(defaultValue="false")
	boolean isAlreadyAssignedAllowReassign();

	@PropertyBeanAttribute(defaultValue="false")
	boolean isProductPartSerialNumber();

	/**
	 * The type of the product built at the process point which will later be tied to a product of type PRODUCT_TYPE
	 */
	@PropertyBeanAttribute(defaultValue="")
	String getSubAssyProductType();

	@PropertyBeanAttribute(defaultValue="false")
	boolean isAutoAdvancePart();
	/**
	 * flag to indicate if broadcast of failed product check results is required
	 * product id
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	boolean isBroadcastFailedProductCheckResults();

	/**
	 * Timeout for Rfid Process
	 */
	@PropertyBeanAttribute(defaultValue="10")
	int getRfidTimeout();


	/**
	 * Should be set to true when Lot Control is run in QICS client as QICS will execute checks.
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isSkipProductChecks();


	/**
	 * sequence Name
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public String getSequenceName();


	/**
	 * Should be set to true to skip dataCollection
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isSkipDataCollection();

	/**
	 * Should be set to false to execute Product spec check.
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "true")
	public boolean isSkipProductSpecCheck();

	
	/**
	 * Flag to check for a frame destination (sale model code) change
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isDestinationChangeAlarm();
	
	/**
	 * A list of product check types, separated by comma that could show some alert for user but do not stop process
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public String[] getProductWarnCheckTypes();


	@PropertyBeanAttribute
	public Map<String, String> getNgtParseInfo();
	
	/**
	 * Flag to indicate how the check model change works
	 * default value: true - check the current product spec code against last processed product spec code
	 *                false - check the current product spec code against product spec code of the previous product in sequence.
	 */
	@PropertyBeanAttribute(defaultValue = "true")
	public boolean isCheckModelChangeWithPreviousProcessed();

	/**
	 * Timeout for NextExpectedProduct 
	 */
	@PropertyBeanAttribute(defaultValue="60000")
	long getNextExpectedProductTimeOut();
	
	/**
	 * Cell action flag for processing cell PLC signal
	 * Values: IGNORE/CANCEL/SKIP 
	 * Default: IGNORE
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "IGNORE")
	public String getCellAction();
	
	/**
	 * Flag to indicate if force associate manual hand scan after product was skipped by cell out
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "true")
	public boolean isForceManualScanAfterSkippedByCellOut();

	
	/**
	 * sequence Name
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public String getRelinkAttributeName();
	
	/**
	 * empty carrier Name
	 */
	@PropertyBeanAttribute(defaultValue = "EMPTY")
	public String getEmptyCarrierName();
	
	
	/**
	 * Flag to indicate if get expected product from previous line or not
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "true")
	public boolean isCheckExpectedProductFromPreviousLine();
	
	/**
	 * value for installed part reason
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public String getInstalledPartReason();
	
	/**
	 * Flag to indicate if skipProduct on Refresh/Repair button click
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "true")
	public boolean isAllowSkipProduct();
	
	/**
	 * A flag to use subassembly product type for product id 
	 * in DB update 
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isUseSubAssyProductType();
	
	/**
	 * Get process point ID for part prod spec rule
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public String getProdSpecProcessPointId();
	
	
	 /** 
	 *Property for using QICS Service or Headless NAQ Service. 
	 *  
	 * @return 
	 */ 
   @PropertyBeanAttribute(defaultValue = "true") 
	boolean isUseQicsService();
   
   /**
	 *  flag indicates whether the LotControl station is Scan less enabled 
	 * 
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isScanlessEnabled();
	
	/**
	 * 
	 * Scan Tool Id
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "SCAN-TOOL")
	public String getScanToolId();
	
	/**
	 * 
	 * ErrorTypePopUpAllowed
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public String getScanlessErrorTypePopupAllowed();
	
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isPopupRemoveRepairScanlessEnabled();
}
