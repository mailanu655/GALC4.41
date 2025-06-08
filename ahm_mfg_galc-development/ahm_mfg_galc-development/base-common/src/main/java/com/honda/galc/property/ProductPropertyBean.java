package com.honda.galc.property;

import java.awt.Color;
import java.util.Map;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>ProductPropertyBean</code> is ... .
 * </p>
 * <h4>Usage and Example</h4> <h4>Special Notes</h4>
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
 * <TD>&nbsp;</TD>
 * <TD>&nbsp;</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Realse</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author Jeffray Huang
 */
@PropertyBean(componentId ="DEFAULT_PRODUCT_CLIENT")
public interface ProductPropertyBean extends ExpectedProductPropertyBean {

	/**
	 * Flag indicates AF ON SEQ NO exist and checked
	 * @return
	 */
	@PropertyBeanAttribute (defaultValue = "false")
	boolean isAfOnSeqNumExist();
	
	@PropertyBeanAttribute(defaultValue = "com.honda.galc.client.product.ProductIdProcessor")
	String getProductIdProcessor();
	
	@PropertyBeanAttribute(defaultValue = "com.honda.galc.client.product.pane.ProductInputPane")
	String getProductInputPane();
	
	@PropertyBeanAttribute(defaultValue = "com.honda.galc.client.product.pane.ProductInfoPane")
	String getProductInfoPane();
	
	@PropertyBeanAttribute(defaultValue = "com.honda.galc.client.product.pane.ProductIdlePane")
	String[] getProductIdlePane();
	
	@PropertyBeanAttribute(defaultValue = "")
	String[] getWidgets();
	
	@PropertyBeanAttribute(defaultValue = "CANCEL,SKIP,SUBMIT")
	String[] getProductButtons();
	
	@PropertyBeanAttribute(defaultValue = "")
	String[] getProcessViews();
	
	@PropertyBeanAttribute(defaultValue = "com.honda.galc.client.product.pane.ProcessChangeAndPpe")
	String getProcessInfoDialog();
	
	// in case of no dialog to show, set the value to "false" in GAL489TBX
	@PropertyBeanAttribute(defaultValue = "com.honda.galc.client.ui.menu.MainMenuDialog")
	String getMainMenuDialog();
	/**
	 * Flag indicates if supporting Product Id sent from PLC
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	boolean isProductIdFromPlc();
	
	/** This controls the presence of a CCPImage popup view **/
	@PropertyBeanAttribute(defaultValue="false")
	boolean isDisplayCcp();
	
	/**
	 * Running tracking service in background
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	boolean isAsyncInvokeTracking();

	
	/**
	 * Flag indicates if supporting Product Id auto refresh signal sent from PLC
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	boolean isProductIdRefreshFromPlc();
	
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isSendDataCollectionComplete();
	
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isShowPcnPpeWhenSwitchUser();

	/* if true the client will send a "4" to PLC to request PLC send the EIN back */
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isBroadcastAfterLogin();

	/* if true, invoke Straggler Service*/
	@PropertyBeanAttribute(defaultValue = "true")
	public boolean isInvokeStragglerService();

	/* if true, Show Straggler as Model Change*/
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isStragglerAsModelChange();

	/**
	 * Returns a map of CSV ProductEntryRadioButtonType to a product type.<br>
	 * Valid types are PROD_ID, SHORT_VIN, TRACK_STS, PROD_LOT, SEQ_NO and SEQ_RANGE.
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public Map<String, String> getProductSearchRadioButtons();
	
	/**
	 * If true, filter out products that are not valid for
	 * the application from the manual product search dialog.
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isProductSearchFilterEnabled();
	
	/**
	 * The background color to be used for rows that are filtered from the product search dialog.
	 */
	@PropertyBeanAttribute(defaultValue = "255,255,0")
	public Color getProductSearchFilterBackgroundColor();
	
	/**
	 * If true, display Manual Product Entry Dialog instead of Product Id Label
	 * on VIOS Input Pane
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isManualProductEntryEnabled();
	
	/**
	 * If true, display Schedule Client
	 * on VIOS Idle Pane
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isScheduleClientDisplayed();
	
	/**
	 * If true, product id will be auto entered from 
	 * Manual Product Input
	 */
	@PropertyBeanAttribute(defaultValue = "true")
	public boolean isAutoEnteredManualProductInput();

	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isRemoveIEnabled();
	
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isAllowProductForPartScan();
	
	/**
	 * If true, Operation efficiency history will be created
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isCreateOpEfficiencyHistory();
	
	
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isUseEffectiveDateForParts();
	/*
	 * If true, shows KD lot number in VIOS 
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isShowKdLotNumberForFrame();
	
	/**
	 * If true, Multi-Manufacturing Parts will be validated 
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isCheckMultiMfgPart();

	/**
	 * If true, partition will be shown in Idle screen 
	 */
	@PropertyBeanAttribute(defaultValue = "true")
	boolean isProductWidgetDisplayed();

	/**
	 * If true, the tracking service will get called after VIN scan
	 */
	@PropertyBeanAttribute(defaultValue = "true")
	boolean isAutoTracking();

	/**
	 * If true, display a Keyboard button to show virtual keyboard on Input Pane
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isKeyboardButtonVisible();
	
	/**
	 * Set minimum text length typed on Product Entry to trigger search
	 */
	@PropertyBeanAttribute(defaultValue = "4")
	int getProductSearchMinLength();
	
	/**
	 * Set the maximum number of records to display on a page of the product search
	 */
	@PropertyBeanAttribute(defaultValue = "100")
	public int getProductSearchPageSize();
	
	/**
	 * Gets the product history process point.
	 */
	@PropertyBeanAttribute(defaultValue = "")
	String getProductHistoryProcessPoint();
	
	/**
	 * Gets the product history row count.
	 */
	@PropertyBeanAttribute(defaultValue = "25")
	int getProductHistoryRowCount();
	
	/**
	 * Gets the part ShippingSites.
	 */
	@PropertyBeanAttribute(defaultValue = "ELP,HMIN")
	String getShippingSites();
	
	/**
	 * Gets the part ShippingSite count.
	 */
	@PropertyBeanAttribute
	public Map<String, String> getSitePartCount();
	
	/**
	 * Gets the Site Allow PartialShipment
	 */
	@PropertyBeanAttribute
	public Map<String, String> getSiteAllowPartialShipment();
	
	/**
	 * Gets the Site By TrailerMask
	 */
	@PropertyBeanAttribute
	public Map<String, String> getSiteTrailerMask();
	

	/**
	 * Gets the scanTrailerFirst
	 */
	@PropertyBeanAttribute(defaultValue = "true")
	public boolean isScanTrailerFirst();
	

	/**
	 * Gets the TrailerLabel
	 */
	@PropertyBeanAttribute(defaultValue = "Trailer Number")
	public String getTrailerNumberLabel();

	/**
	 * Gets the Site Allow PartialBuiltProductShipment
	 */
	@PropertyBeanAttribute
	public Map<String, String> getAllowPartialBuild();
	
	/**
	 * Gets the Processpoint to check for PartialBuiltProducts
	 */
	@PropertyBeanAttribute
	public Map<String, String> getPartialCheckPp();
	
	/**
	 * Gets the Site ShippingValidation
	 */
	public Map<String, String> getShippingValidation();
	
	/**
	 * Gets the Processpoint to check for FinalProducts
	 */
	@PropertyBeanAttribute
	public String getFinalCheckPp();
	

	/**
	* property to show Special units in the Unit navigator. i.e Units that are new to the process point, 
	* Units that are not done by logged user in the specified timeframe.
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isHighlightSpecialOpInUnitNavigator();
	
	/**
	 * Only need to set up if the isHighlightSpecialOpInUnitNavigator()=true.
	 * Property to check no.of days that this unit was last installed b19UNC1201JY400007y the 
	 * logged user at that Process point. 
	 */
	@PropertyBeanAttribute(defaultValue = "30")
	public int getSpecialOpCheckInDays();
	/**
	 * 
	 * Property to check when was the last time particular model (Ex:-JT6NAB6)
	 * The result gets displayed in the Structure_details_Widget
	 */
	@PropertyBeanAttribute(defaultValue = "30")
	public int getFirstTimeRunningModelInDays();

	/**
	 * Check Inspection Sampling
	 * 
	 */
	@PropertyBeanAttribute(defaultValue = "true")
	public boolean isInspectionSamplingEnabled();

    /**
     * Defines maximum number of items in memory for persistent cache. 
     */
    @PropertyBeanAttribute(defaultValue = "30")
    public int getCacheSizeMem();
    
    /**
     * Defines maximum number of items on disk for persistent cache. 
     */
    @PropertyBeanAttribute(defaultValue = "0")
    public int getCacheSizeDisk();
    
    /**
     * Time to live on cache in seconds.
     * @return
     */
    @PropertyBeanAttribute(defaultValue = "0")
    public int getCacheTimeToLive();
    
    /**
     * Time to idle on cache in seconds.
     * @return
     */
    @PropertyBeanAttribute(defaultValue = "0")
    public int getCacheTimeToIdle();	    
    
	
	/**
	 * List of Search Pane to display in Bulk NAQics process
	 * Available pane values: 
	 * PRODUCT_SCAN_PANE,SEARCH_BY_PROCESS_PANE,SEARCH_BY_DUNNAGE_PANE,SEARCH_BY_TRANSACTION_PANE
	 * 
	 */
	@PropertyBeanAttribute(defaultValue = "PRODUCT_SCAN_PANE")
	String[] getSearchByPanes();
	
	/**
	 * It is a mapped property used by displayed Search pane 
	 * to define access to current user. If not define, will give access to tab to everyone.
	 * example1: ELEVATED_SECURITY_GROUP_FOR{SEARCH_BY_PROCESS_PANE} : <LDAP security group>
	 * example2: ELEVATED_SECURITY_GROUP_FOR{SEARCH_BY_DUNNAGE_PANE} : <LDAP security group>
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public Map<String, String> getElevatedSecurityGroupFor();
	
	/**
     * Increases product input pane fields' and labels' font size.
     * @return
     */
    @PropertyBeanAttribute(defaultValue = "false")
	public boolean isShowLargeProductIdField();
}
