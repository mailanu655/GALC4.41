package com.honda.galc.client.qics.property;

import java.util.Map;

import com.honda.galc.property.IProperty;
import com.honda.galc.property.PropertyBean;
import com.honda.galc.property.PropertyBeanAttribute;


/**
 * 
 * <h3>DefaultQicsPropertyBean Class description</h3>
 * <p> DefaultQicsPropertyBean description </p>
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
 * Apr 18, 2012
 *
 *
 */
 /**
 * added isRepairTracking property for RepairIn Panel
 * @author Gangadhararao Gadde
 * @date Apr 17, 2014
 */
@PropertyBean(componentId = "DEFAULT_QICS")
public interface DefaultQicsPropertyBean extends IProperty{

	@PropertyBeanAttribute(defaultValue = "5")
	public int getAssociateNumberCacheSize();

	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isDunnage();
	
	@PropertyBeanAttribute(defaultValue = "AAA")
	public String getMachineId();
	
	public Map<String, String> getDefectStatusColors();

	@PropertyBeanAttribute(defaultValue = "0,0,255")
	public int[] getIdleColorRgb();

	@PropertyBeanAttribute(defaultValue = "0,255,0")
	public int[] getReadOnlyColorRgb();

	@PropertyBeanAttribute(defaultValue = "255,0,0")
	public int[] getErrorColorRgb();

	@PropertyBeanAttribute(defaultValue = "TEXT_INPUT")
	public String getDefaultInputViewId();

	@PropertyBeanAttribute(defaultValue = "")
	public Map<String, String> getProductNotProcessableCheckTypes();
	
	@PropertyBeanAttribute(defaultValue = "")
	public Map<String, String> getProductCheckTypes();

	@PropertyBeanAttribute(defaultValue = "")
	public Map<String, String> getProductWarnCheckTypes();

	@PropertyBeanAttribute(defaultValue = "")
	public Map<String, String> getProductPreCheckTypes();
	
	@Deprecated
	@PropertyBeanAttribute(defaultValue = "")
	public String getForwardProcessPointId();
	
	@Deprecated
	@PropertyBeanAttribute(defaultValue = "")
	public String getAltForwardProcessPointId();

	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isOverrideRepairAssociateEnabled();
	
	@PropertyBeanAttribute(propertyKey = "CHECK_1_OF",defaultValue = "-1")
	public int getCheckOneOffLimit();
	
	@PropertyBeanAttribute(propertyKey = "CHECK_1_OF_MESSAGE",defaultValue = "")
	public String getCheckOneOffMessage();

	@PropertyBeanAttribute(defaultValue = "")
	public String[] getIdlePanelCalculations();

	@PropertyBeanAttribute
	public Map<String, String> getDefectRepairMethods();

	@PropertyBeanAttribute(defaultValue = "")
	public String getSendInputNumberToDeviceId();

	@PropertyBeanAttribute(defaultValue = "")
	public String[] getDefectList();

	@PropertyBeanAttribute(defaultValue = "")
	public String[] getLocationList();

	@PropertyBeanAttribute(defaultValue = "")
	public String[] getInputPanels();

	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isTouchScreen();
	
	@PropertyBeanAttribute(defaultValue = "0")
	public int getTimeout();
	
	/* indicate if logout user when timeout occurs */
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isLogoutOnTimeout();
	
	/**
	 * indicate if time out in Qics when processing a product 
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isTimeoutInQicsProcess();
	
	@PropertyBeanAttribute(defaultValue="false")
    public boolean isRespDeptLineZoneEnabled();
	
	/**
	 * Checks if is change responsible department disabled. The change
	 * responsible department option is disabled if set as true.
	 * If this property is not defined, by default change responsible department option
	 * is enabled.
	 */
    @PropertyBeanAttribute(defaultValue="false")
    public boolean isChangeResponsibleDeptDisabled();
    
    @PropertyBeanAttribute(defaultValue="false")
    public boolean isProductButtonEnabled();
    
    @PropertyBeanAttribute(defaultValue="false")
    public boolean isGlobalDirectPass();
	
	@PropertyBeanAttribute(defaultValue="")
	public String[] getGlobalDirectPassProcessPoint();
	
	@PropertyBeanAttribute(defaultValue="false")
    public boolean isProductCheckPlaySound();
	
	@PropertyBeanAttribute(defaultValue="true")
    public boolean isSubmitButtonPanelEnabled();

	@PropertyBeanAttribute(defaultValue = "Station Info")
	public String[] getIdlePanelTabs();
	
	@PropertyBeanAttribute(defaultValue = "")
	public String getDefaultWriteUpDept();
	
	@PropertyBeanAttribute(defaultValue="true")
    public boolean isDefectActualProbRepairMethodEnabled();
	
	@PropertyBeanAttribute(propertyKey= "OUTSTANDING_FLAG_CHANGABLE",defaultValue = "true")
	public boolean isOutstandingFlagChangable();
	
	/**
	 * It defines product properties that will be displayed on product info panel.
	 * Format:
	 * PRODUCT_INFO_PROPERTIES = propertyName:propertyLabel[,propertyName2:propertyLabel2,...]
	 * propertyName - name of the property on product object
	 * propertyLabel - label that will be displayed, it is optional
	 * If this property is not defined, default set of properties for different product types will be used.
	 * Defaults are defined in ProductInfoPanel.
	 */
	@PropertyBeanAttribute(defaultValue="")
    public String[] getProductInfoProperties();	
	
	@PropertyBeanAttribute(defaultValue="")
	public Map<String, String> getConditionalBroadcastCheckMapping();
	
	@PropertyBeanAttribute(defaultValue="false")
    public boolean isRepairTracking();
	
	@PropertyBeanAttribute(propertyKey= "SUB_PRODUCT_TYPES",defaultValue = "")
	public String[] getSubProductTypes();
	
	@PropertyBeanAttribute(propertyKey= "SUB_PRODUCT_PART_NAMES",defaultValue = "")
	public Map<String,String[]> getSubProductPartNames(Class<?> clazz);
	
	/**
	 * Gets the change responsible department group. It defines the security group id
	 * to which access to Change Responsible Department functionality is
	 * granted.
	 * If this property is not defined, by default access to Change Responsible Department functionality is
	 * granted.
	 */
	@PropertyBeanAttribute(defaultValue="*")
	public String getChangeResponsibleDeptGroup();
	
	/**
	 * Defines if the comment field in Defect repair panel is mandatory .If set to true, user will have to enter 
	 * comments while repairing the defect using defect repair panel.
	 */
	@PropertyBeanAttribute(defaultValue="false")
    public boolean isRepairCommentRequired();
	
	@PropertyBeanAttribute(defaultValue="false")
	public boolean isSoundEnabled();
	
	// If the property is true, perform the conditional broadcast as soon as precheck is performed 
	@PropertyBeanAttribute(defaultValue="false")
    public boolean isConditionalBroadcastPreCheck();
	
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
	 * Comma separated authorized security groups, mapped by Qics ActionId 
	 * <br />
	 * <PRE>
	 * property : AUTHORIZATION_GROUP{ACTION_ID}:GROUP_ID
	 * sample   : AUTHORIZATION_GROUP{CANCEL}:SA
	 *            AUTHORIZATION_GROUP{ACCEPT_REPAIR}:RU
	 * </PRE>
	 * @return
	 */
	@PropertyBeanAttribute
	public Map<String, String> getAuthorizationGroup();
	
	@PropertyBeanAttribute(defaultValue = "ENGINE,FRAME,MISSION")
	public String[] getNonScrappableProductTypes();
	
	@PropertyBeanAttribute(defaultValue = "true")
	public boolean isDuplicateDefectAllowed();
	
	@PropertyBeanAttribute(defaultValue = "true")
	public boolean isDuplicateDefectCheckOn();
	
	@PropertyBeanAttribute(propertyKey = "MISSION_50A_SHIPPING_TRANSACTION_ENABLED" ,defaultValue = "false")
	public boolean isMissionShippingTransactionEnabled();
	
	@PropertyBeanAttribute(propertyKey = "SHIPPING_TRANSACTION_URL", defaultValue="")
	public String getShippingTransactionUrl();
	
	@PropertyBeanAttribute(propertyKey="SHIPPING_TRANSACTION_PROCESS_POINT")
	public String getShippingTransactionProcessPoint();
	
	@PropertyBeanAttribute(defaultValue = "true")
	public boolean isLpdcDirectPassAllButtonEnabled();
}
