package com.honda.galc.property;

import java.util.Map;

/**
 * 
 * <h3>HeadLessPropertyBean</h3> <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * HeadLessPropertyBean description
 * </p>
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
 * @author Paul Chou Mar 23, 2011
 * 
 */

@PropertyBean(componentId = "Default_HeadLess")
public interface HeadLessPropertyBean extends DataMappingPropertyBean,
		ProductCheckPropertyBean {

	/**
	 * Check if required parts are installed before this point
	 * 
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isCheckRequiredPart();

	/**
	 * Specify if the station is in_line repair station. In_Line repair station
	 * repair defect. Normally Headless in_line repair station repairs defect
	 * created by Headless data collection.
	 * 
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isInlineRepair();

	/**
	 * Pre-Scripts will be evaluated during the data collection process before
	 * build result is extracted.
	 * @return
	 */
	@PropertyBeanAttribute
	public Map<String, String> getPreScripts();

	/**
	 * Scripts will be evaluated during the data collection process.
	 * @return
	 */
	@PropertyBeanAttribute
	public Map<String, String> getScripts();

	/**
	 * Defect status for Updating Qics. By default it create outstanding defect.
	 * Default value 0 as out standing defect
	 * 
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "0")
	public int getDefectStatus();

	/**
	 * Headless data collection will save build results into database
	 * automatically. Set this to false, if no need to save build results or use
	 * script to save build results
	 * 
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "true")
	public boolean isAutoSaveBuildResult();

	/**
	 * Update Qics defect automatically. Set this to false, if no need to update
	 * Qics or to use script to update Qics
	 * 
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isAutoUpdateQics();

	/**
	 * if false, NAQics will not fix duplicate defects also
	 * 
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "true")
	public boolean isFixDuplicates();

	/**
	 * Headless data collection will track the product automatically. Set this
	 * to false, the tracking is not required or to use script call tracking.
	 * 
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isAutoTracking();

	/**
	 * Indicate if validate product id is required. For example, if used for
	 * device heat beat, then no product id is provided and no validation is
	 * required.
	 * 
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "true")
	public boolean isValidateProductId();

	/**
	 * Indicate if the required part check status will impact the installed part
	 * status. true, installedPartStatus NG when failed required part check
	 * false, required part check result will not impact installedPartResult
	 * 
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isIncludeRequiredPartCheckStatus();

	/**
	 * Indicate the In_Line repair quick fix style.
	 * 
	 * For quick fix, a separate process point will be defined and normally it
	 * is right followed by the defect process point on the assemble line. Only
	 * part status will be passed in for each part and all defect on the part
	 * created on the repair process point will be fixed.
	 * 
	 * RepairProcessPointId must be defined for InlineRepairQuickFix
	 * 
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isInlineRepairQuickFix();

	/**
	 * Process point id on which defect data was collected
	 * 
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public String getDefectProcessPointId();

	/**
	 * Products for DC/MC data collection separate by ",". for example:
	 * HEAD1,HEAD2
	 * 
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public String getProducts();

	/**
	 * Provide image name for Qics picture input if required
	 * 
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public String getImageName();
	
	/**
	 * Model and Image name mapping
	 * @return
	 */
	@PropertyBeanAttribute
	public Map<String, String> getImageNames();

	/**
	 * Specify the value data type in data container In old GALC all tag value
	 * are String. To components like OPC EI provide options to ceate all string
	 * valued data container.
	 * 
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "true")
	public boolean isStringInDataContainer();

	/**
	 * To indicate if defect will be created based on overall data collection
	 * status or based on each part location. Use only for Diecast
	 * 
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isCreatDefectOnOverallStatus();

	/**
	 * Log performance time stamp
	 * 
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isLogPerformance();

	/**
	 * Data Collector Task Common use is for on/off process point
	 * 
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public String getTask();

	/**
	 * Run the task auto automatically
	 * 
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "true")
	public boolean isAutoRunTask();

	/**
	 * Maintain product sequence - process point to remove product sequence
	 * 
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public String getOutProductSequenceId();

	/**
	 * process point to add into product sequence
	 * 
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public String getInProductSequenceId();

	/**
	 * Plant code MC/DC for example HC for HCM
	 * 
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "HC")
	public String getPlantCode();

	/**
	 * Process points for DC off process points of dc off separated by comma,
	 * for instance "DC0HD16001,DC0HD16002"
	 * 
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public String getDcOffProcessPoints();

	/**
	 * MC off process points process point ids of Mc off separated by comma, for
	 * instance "MC0HD16002,MC0HD16002"
	 * 
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public String getMcOffProcessPoints();

	/**
	 * If not defined take default e.g. same as defect creation process point if
	 * defined, only defined part will be repaired. part names in one string
	 * separated by comma, for instance: Part1,Part2
	 * 
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public String getRepairPartNames();

	/**
	 * Indicate if create defect while doing inline repair
	 * 
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isInlineRepairCreateDefect();

	/**
	 * Indicate how to handle on hold product true - warn product on hold status
	 * and continue collect data for the product false - throw exception to
	 * invalid product
	 * 
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "true")
	public boolean isProcessOnHoldProduct();

	
	/**
	 * Indicates if validate build results with in lot control rule specified
	 * range
	 * 
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "true")
	public boolean isValidateBuildResults();

	/**
	 * Indicate if it checks outstanding defect.
	 * 
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isCheckOutstandingDefect();

	/**
	 * flag to notify line side monitor
	 * 
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isNotifyLineSideMonitor();

	/**
	 * flag to save last processed product
	 * 
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	boolean isSaveLastProduct();

	/**
	 * flag to check product state
	 * 
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public String[] getProductStateChecks();

	/**
	 * get Destination process Point Id
	 * 
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public String getDestinationProcessPointId();

	/**
	 * get Home Product Number Definition
	 * 
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "DCB")
	public String getHomeProductNumberDef();

	/**
	 * MBPN products which not exist in database table separated by comma
	 * 
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public String getOriginalProducts();

	/**
	 * Valid plan code used in Weld product serial number
	 * 
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public String getValidPlantCodes();

	/**
	 * Valid department code used in Weld product serial number.
	 * 
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public String getValidDepartments();

	/**
	 * install part names of MBPN products separated by comma
	 * 
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public String getMbpnInstalledPartNames();

	/**
	 * flag to check measurement max attempts
	 * 
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isCheckMeasurementMaxAttempts();

	/**
	 * flag to save measurement hist
	 * 
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isSaveMeasurementHist();

	/**
	 * flag to return the duplicated product id
	 * 
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isReturnDuplicatedProductId();

	/**
	 * flag to indicate support of ignore parts
	 * 
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isSupportIgnoredParts();

	/**
	 * A list of Build Attributes
	 * 
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public String[] getBuildAttributes();

	/**
	 * Check if same part is scanned more than once
	 * 
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isVerifyDuplicateParts();

	/**
	 * Indicate if it requires associateId
	 * 
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isRequireAssociateId();

	/**
	 * Indicate if it requires associateId for InlineRepair
	 * 
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isRequireAssociateIdForInlineRepair();

	/**
	 * Indicate if exception out product will pass the validation check
	 * 
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isExceptionOutValidProduct();

	/**
	 * Indicate if it is a PDA gaugeflex service
	 * 
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isPdaRecipeDownload();

	/**
	 * Indicate if you want to update Repair reason
	 * 
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public String getRepairReason();
	
	/**
	 * Property for Diecast On process to indicate validate Dc Number format
	 * 
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "true")
	public boolean isValidateDcNumberFormat();

	/**
	 * Property for Diecast On process to indicate validate Mc Number format
	 * 
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "true")
	public boolean isValidateMcNumberFormat();

	/**
	 * Defines comma delimited Mask for DC Number
	 * 
	 * @see com.honda.galc.util.CommonPartUtility
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public String[] getDcNumberMask();

	/**
	 * Defines comma delimited Mask for MC Number
	 * 
	 * @see com.honda.galc.util.CommonPartUtility
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public String[] getMcNumberMask();
	
	 
     /** 
     * Property for updating GAL125TBX using Headless NAQ Service. 
     *  
     * @return 
     */ 
     @PropertyBeanAttribute(defaultValue = "false") 
	 public boolean isUpdateDefectResult(); 
	   
	 /** 
	 *Property for using QICS Service or Headless NAQ Service. 
	 *  
	 * @return 
	 */ 
     @PropertyBeanAttribute(defaultValue = "true") 
     public boolean isUseQicsService();

     /**
      * Property for Sequence Name
      */
     @PropertyBeanAttribute(defaultValue = "") 
     public String getSequenceName();

     /**
      * Property for PartMarkRequest System Name
      */
     @PropertyBeanAttribute(defaultValue = "")
     public String getPartMarkRequestSystemId();
     
     /**
      * Property for PartMarkRequest Limit
      */
     @PropertyBeanAttribute(defaultValue = "10")
     public int getPartMarkRequestLimit();
     
 	/**
 	 * If not defined take default e.g. Lot Control is required and qiDefectFlag must be st to send QICS defects
 	 * set this to false if you want to override this and send defects when there is no LC rule
 	 * @return
 	 */
 	@PropertyBeanAttribute(defaultValue = "true")
 	public boolean isQicsLcRuleRequired();
 	
	/**
	 * Property to update measurements for each build results. 
	 * 
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isAutoUpdateMeasurements();
	
	@PropertyBeanAttribute(defaultValue="4")
	public int getPartMarkMaxLength();

	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isReplicateToNaqics();
	
	@PropertyBeanAttribute(defaultValue = "-1")
	public int getNumQicsThreads();
	
	/**
	 * in profiling mode, we are collecting runtime thread usage and load,
	 * how many concurrent threads are running and lifetime maximum pool size
	 * this can be used to determine fixed pool size to use
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isNaqicsProfileMode();
	
	/**
	 * property to determine if MC number should be allowed to overwrite previously
	 * already assigned Number
	 * 
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	boolean isAllowOverwriteMc();

	/**
	 * property value to determine length of diecast id in case of 
	 * DC is greater than max allowed length
	 * 
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "17")
	public int getIdLength();
	
}
