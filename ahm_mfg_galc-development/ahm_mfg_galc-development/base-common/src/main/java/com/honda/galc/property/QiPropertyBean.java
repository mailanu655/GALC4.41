package com.honda.galc.property;

import java.util.Map;

/**
 * 
 * <h3>QicsPropertyBean</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> QicsPropertyBean description </p>
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
 * <TD>Aug 26, 2016</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD> 
 * </TR>  
 *
 * </TABLE>
 *    
 * @see
 * @version 0.1
 * @author LnT Infotech
 * @since Aug 26, 2016
 */


@PropertyBean(componentId ="DEFAULT_QI_PROPERTY_INFO")
public interface QiPropertyBean extends IProperty{

	/**
	 * Specifies the width of the line being drawn for use in image section screen
	 * Please specify the width. By default it will be 2.
	 */
	@PropertyBeanAttribute(propertyKey="LINE_WIDTH_IMAGE_SECTION", defaultValue="2")
	public int getLineWidth();
	
	/**
	 * Specifies the defect entry screen  thumbnail entryscreenname hover popup text font size
	 */
	@PropertyBeanAttribute(defaultValue="18")
	public int getThumbnailHoverFontSize();
	
	
	/**
	 * Specifies the Color of the line being drawn for image section screen.
	 * Please specify the Color. By default it will be red.
	 */
	@PropertyBeanAttribute(propertyKey="LINE_COLOR_IMAGE_SECTION", defaultValue="RED")
	public String getLineColor();
	/**
	 * Specifies the Zoom Factor by which the image in the image section screen can be zoomed in.
	 * Please specify the Zoom Factor. By default it will be 1.2.
	 */
	@PropertyBeanAttribute(propertyKey="ZOOM_FACTOR_IMAGE_SECTION", defaultValue="1.2")
	public double getZoomFactor();

	/**
	 * Smtp server to send email for Home screen
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public String getSmtpServer();
	
	/**
	 * Sender email for Home screen
	 */
	@PropertyBeanAttribute(defaultValue = "NAQICS_Comment@Honda.com")
	public String getSenderEmail();

	/**
	 * Specifies the Zoom Factor by which the image in the image section screen can be zoomed in.
	 */
	@PropertyBeanAttribute(defaultValue="")
	public String getToEmailGroup();

	/**
	 * Specifies Product pre check for Defect pre check screen
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public Map<String, String> getProductPreCheckTypes();

	/**
	 * Specifies if the station is UPC station 
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isUpcStation();

	/**
	 * Supplier number 
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public String getSupplierNo();

	/**
	 * Supplier number 
	 */
	@PropertyBeanAttribute(defaultValue = "16")
	public int getHomeScreenFontSize();

	/**
	 * Get the site names for validating data 
	 */ 
	@PropertyBeanAttribute(defaultValue ="") 
	public Map<String,String> getSitesForValidation();

	/**
	 * Specifies if the Sound is enabled
	 */
	@PropertyBeanAttribute(defaultValue="false")
	public boolean isSoundEnabled();

	/**
	 * Specifies if the station requires Dunnage
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isDunnage();

	/**
	 * Get the MachineId to generate the dunnage number
	 */
	@PropertyBeanAttribute(defaultValue = "AAA")
	public String getMachineId();
	
	/**
	 * Get the machine Ids for a process point
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public String[] getMachineIds();
	
	/**
	 * Get the front Bumper part name.
	 */
	@PropertyBeanAttribute (defaultValue = "")
	public String getFrontBumperPartName();
	/**
	 * Get the rear Bumper part name.
	 */
	@PropertyBeanAttribute (defaultValue = "")
	public String getRearBumperPartName();
	/**
	 * Get the left Knuckle part name.
	 */
	@PropertyBeanAttribute (defaultValue = "")
	public String getLeftKnucklePartName();
	/**
	 *Get the right Knuckle part name.
	 */
	@PropertyBeanAttribute (defaultValue = "")
	public String getRightKnucklePartName();
	
	/**
	 * Enables Ipp Tag edit function on Qics/Ipp Tag panel. <br />
	 * Default : false (Edit Function not enabled)
	 */
	@PropertyBeanAttribute(defaultValue="false")
	public boolean isIppTagEditEnabled();
	
	/**
	 * Name of the Oif Task that processes Ipp tags.
	 */
	@PropertyBeanAttribute(defaultValue="")
	public String getIppInfoOifTaskName();
	
	/**
	 * URL of the GALC Server that is running Oif Ipp Tag task. <br />
	 * Format : http://SERVER_NAME:PORT <br />
	 * If the value is blank then local GALC Server is assumed. <br />
	 * Default : ""
	 */
	@PropertyBeanAttribute(defaultValue="")
	public String getIppInfoOifTaskServerUrl();
	
	/**
	 * Input number for Ipp Tag  <br />
	 * Default : false 
	 */
	@PropertyBeanAttribute (defaultValue = "false")
	public boolean isIppTagNumberNumeric();
	
	/**
	 * Get the product warning check types configured for station. 
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public Map<String, String> getProductWarnCheckTypes();
	
	/**
	 * Get the product check types configured for station. 
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public Map<String, String> getProductCheckTypes();	
	
	/**
	 * Get the Dunnage printer address
	 */
	@PropertyBeanAttribute(propertyKey = "DUNNAGE_PRINTER", defaultValue = "")
	public String getDunnagePrinter();

	/**
	 * Get the Dunnage Form Id
	 */
	@PropertyBeanAttribute(propertyKey = "DUNNAGE_FORM", defaultValue = "")
	public String getDunnageForm();
	
	/**
	 * Get the no. of days for which data can be corrected from Data Correction Screen.
	 */
	@PropertyBeanAttribute(defaultValue = "10")
	public int getDataCorrectionChangeDays();
	
	/**
	 * Limit no of records of search Result Result data on data Correction Screen.
	 */
	@PropertyBeanAttribute(defaultValue = "100")
	public int getDataCorrectionSearchListLimit();
	
	/**
	 * Limit in time range when searching by process for bulk processing.
	 * Value is specified in hours
	 */
	@PropertyBeanAttribute(defaultValue = "24")
	public int getSearchTimeRangeLimit();
	
	/**
	 * Limit the number of products that can be processed at a time for bulk processing
	 */
	@PropertyBeanAttribute(defaultValue = "100")
	public int getProductProcessLimit();
	
	/**
	 * If set to true, Switch user pop up will show after capturing defect(Done) and Direct passed
	 */
	@PropertyBeanAttribute (defaultValue = "false")
	boolean switchUserAfterScan();
	
	/**
	 * Property to configure replicate direct pass result to GAL125TBX (true for Legacy GALC QICS only)
	 */
	@PropertyBeanAttribute(propertyKey = "REPLICATE_DIRECT_PASS_TO_125", defaultValue = "false")
	public boolean isReplicateDirectPassTo125();
	
	/**
	 * Property to configure replicate defect and repair result to GAL125TBX and GAL222TBX
	 */
	@PropertyBeanAttribute(propertyKey = "REPLICATE_DEFECT_REPAIR_RESULT", defaultValue = "false")
	public boolean isReplicateDefectRepairResult();
	
	/**
	 * Property to configure replicate repair area result to GAL125TBX and GAL222TBX
	 */
	@PropertyBeanAttribute(propertyKey = "REPLICATE_REPAIR_AREA_RESULT", defaultValue = "false")
	public boolean isReplicateRepairAreaResult();
	
	/**
	 * Property to configure init DEFECTREUSLTID for replicate of defect result to GAL125TBX
	 */
	@PropertyBeanAttribute(propertyKey = "INIT_REPLICATE_DEFECT_RESULT_ID", defaultValue = "2000000000")
	public int getInitReplicateDefectResultId();
	
	/**
	 * Property to configure init REPAIR_ID for replicate of repair result to GAL222TBX
	 */
	@PropertyBeanAttribute(propertyKey = "INIT_REPLICATE_REPAIR_ID", defaultValue = "2000000000")
	public int getInitReplicateRepairId();	
	
	/**
	 * Property to configure whether replicate a repaired defect from defect entry to GAL222TBX
	 */
	@PropertyBeanAttribute(propertyKey = "CREATE_REPAIRED_DEFECT_TO_222", defaultValue = "TRUE")
	public boolean isCreateRepairedDefectTo222();	
	
	/**
	 * Property to configure whether "SENT TO FINAL" button is enabled on product check screen
	 */
	@PropertyBeanAttribute(propertyKey = "SENT_TO_FINAL", defaultValue = "FALSE")
	public boolean isSentToFinal();
	
	/**
	 * Property to configure whether display "Update Repair Area" button is enabled on product check screen
	 */
	@PropertyBeanAttribute(defaultValue = "FALSE")
	public boolean isUpdateRepairArea();
	
	/**
	 * Property to configure maximal number of Part Defect Combinations to be assigned or deassigned to an entry screen at a time
	 */
	@PropertyBeanAttribute(propertyKey = "MAX_PDC_TO_ENTRY_SCREEN", defaultValue = "100")
	public int getMaxPdcToEntryScreen();
	
	/**
	 * Property to configure default repair time
	 */
	@PropertyBeanAttribute(propertyKey = "DEFAULT_REPAIR_TIME", defaultValue = "0")
	public int getDefaultRepairTime();
	
	
	/**
      * Specifies Product warning check for Defect pre check screen
      */
     @PropertyBeanAttribute(defaultValue = "")
     public Map<String, String> getProductPreWarnCheckTypes();

     /**
      * Defines the image section list size to be displayed on the dialog box. 
      */
     @PropertyBeanAttribute(defaultValue = "5")
     public int getImageSectionListSize();

     /**
 	 * Property to configure whether allow new Lot to add
 	 */
 	@PropertyBeanAttribute(defaultValue = "FALSE")
 	public boolean isAllowToAddNewLot();
 	
 	/**
	 * Get the Process Location if allowing new Lot to add
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public String getProcessLocation();

	/**
	 * Get the Plan Code if allowing new Lot to add
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public String getPlanCode();
	
	 /**
 	 * Property to configure whether the station is diecast station
 	 */
 	@PropertyBeanAttribute(defaultValue = "FALSE")
 	public boolean isDcStation();
 	
   	/**
   	 * Specifies engine source based on engine's plant code (in GAL131TBX) 
   	 * for QICs
   	 */
   	@PropertyBeanAttribute(defaultValue = "false")
   	public boolean isQicsEngineSource();
   	
   	/**
   	 * Indicates if it is 102 KI. Some settings will be changed after 102KI. 
	 * Change this value to true as soon as 102KI starts.
   	 */
   	@PropertyBeanAttribute(propertyKey = "102_KI", defaultValue = "false")
   	public boolean is102Ki();
   	
   	/**
	 * Property to configure whether to update Product tracking to next when
	 * No Problem Found is performed
	*/
	 @PropertyBeanAttribute(defaultValue = "false")
	 public boolean isUpdateNextProcess();
	 
	 /**
	 * Get the product part name
	 */
	 @PropertyBeanAttribute(defaultValue = "")
	 public String getLotControlPartName();
	 	
	 /**
	 * Get the process point ID for data collection process
	 */
	 @PropertyBeanAttribute(defaultValue = "")
	 public String getLotControlProcessPointId(); 
	 
	 /**
	  * Get all check required the field names before doing multi-select
	  */
	 @PropertyBeanAttribute(propertyKey = "MULTI_SELECT_REPAIRS_FIELDS_PRE_CHECK", defaultValue = "")
	 public String getMultiSelectRepairsFieldsPreCheck();
	 
	/** 
	 * Allow defect entry for shipped VINs 
	 */ 
	@PropertyBeanAttribute(defaultValue="false") 
	public boolean isAllowDefectForShippedVin();
	
	/** 
	 * Indicate if GDP is determined by Global GDP. If false, use NA GDP. 
	 */ 
	@PropertyBeanAttribute(propertyKey = "GLOBAL_GDP", defaultValue="false") 
	public boolean isGlobalGdp();
	
	 /**
	 * Get write up departments that trigger Global GDP
	 */
	 @PropertyBeanAttribute(propertyKey = "GLOBAL_GDP_WRITE_UP_DEPTS", defaultValue = "VQ,VQ PRE SHIP")
	 public String[] getGlobalGdpWriteUpDepts(); 
	 
	 /**
	 * Get the process point ID for VQ OFF
	 */
	 @PropertyBeanAttribute(propertyKey = "VQ_GDP_PROCESS_POINT_ID", defaultValue = "")
	 public String[] getVqGdpProcessPointId(); 
	 
	 /**
	 * Get the keyword to validate document link
	 */
	 @PropertyBeanAttribute(propertyKey = "DOCUMENT_LINK_KEYWORD", defaultValue = "")
	 public String[] getDocumentLinkKeyword(); 
	 
	/** 
	 * Flag to decide whether to delete processed product only or all before products 
	 * from product sequence tab after product processed
	 */ 
	@PropertyBeanAttribute(defaultValue="false") 
	public boolean isDeleteOnlyProcessedProduct();
	
	@PropertyBeanAttribute(propertyKey = "DATA_CRTN_LIMITED_GP", defaultValue = "")
	public String getDataCorrectionLimited();
	
	 /**
	 * Get Microservice URL
	 */
	 @PropertyBeanAttribute(propertyKey = "FILE_UPLOAD_URL", defaultValue = "")
	 public String getFileUploadUrl(); 
	 
	@PropertyBeanAttribute(propertyKey = "TRACKING_AUTH_GROUP", defaultValue = "")
	public String getTrackingAuthGroup();
		
	/**
	 * Target Maintenance. 
	 * When parents' targets get updated, the children's targets shall be automatically updated or not
	 */
	@PropertyBeanAttribute(propertyKey = "AUTO_UPDATE_CHILD_TARGET", defaultValue = "false")
	public boolean isAutoUpdateChildTarget();
	
	 /**
	 * Get the minimum slider value
	 */
	 @PropertyBeanAttribute(defaultValue = "-5")
	 public double getMinSliderValue();

	 /**
	 * Get the maximum slider value
	 */
	 @PropertyBeanAttribute(defaultValue = "5")
	 public double getMaxSliderValue();

	 /**
	 * Get the slider increment value
	 */
	 @PropertyBeanAttribute(defaultValue = "0.25")
	 public double getIncrementSliderValue();

	 /**
	 * Get the major tick unit value to display
	 */
	 @PropertyBeanAttribute(defaultValue = "1")
	 public double getMajorTickSliderUnitValue();

	 /**
	 * Get the minor tick unit value to display
	 */
	 @PropertyBeanAttribute(defaultValue = "3")
	 public int getMinorTickSliderUnitValue();

	 @PropertyBeanAttribute(propertyKey="REQUEST_PER_MINUTE", defaultValue="1")
	 public int getRequestPerMinute();
}
