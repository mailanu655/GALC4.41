package com.honda.galc.client.schedule;

import com.honda.galc.property.PropertyBeanAttribute;

/**
 * <h3>Class description</h3>
 * Schedule ClientProperty Class Description
 * Properties used by the client. 
 * <h4>Description</h4>
 * <h4>Special Notes</h4>
 * <h4>Change History</h4>
 * <TABLE BORDER="1" CELLPADDING="3" CELLSPACING="1" WIDTH="100%">
 * <TR BGCOLOR="#EEEEFF" CLASS="TableSubHeadingColor">
 * <TH>Updated by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * <TR>
 * <TD>Janak Bhalla & Alok Ghode</TD>
 * <TD>March 05, 2015</TD>
 * <TD>1.0</TD>
 * <TD>GY 20150305</TD>
 * <TD>Initial Release</TD>
 * </TR>
 * </TABLE>
 */

public interface ScheduleClientProperty extends DefaultScheduleClientProperty {

	@PropertyBeanAttribute(propertyKey ="IS_MOVE_BY_KD_LOT",defaultValue = "false")
	public boolean isMoveByKdLot();
	
	@PropertyBeanAttribute(propertyKey ="LOCK_FIRST_LOT",defaultValue = "true")
	public boolean lockFirstLot();
	
	
	public String getOnProcessPoint();
	
	@PropertyBeanAttribute(defaultValue = "")
	public String getExceptionOnProcessPoint();
	
	@PropertyBeanAttribute(defaultValue = "orange")
	public String getExceptionOnProcessPointColor();
	
	//public String[] getExceptionOnProcessPointModels();
	
	/**
	 * Device Id specify the Device to write last lot to PLC
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public String getLastLotDeviceId();

	/**
	 * Authorization group
	 * @return
	 */
	
	public String getAuthorizationGroup();
	
	/**
	 * Flag indicating whether authentication is required for each Change to Sent task in the LastProductSelectionDialog
	 */
	@PropertyBeanAttribute(defaultValue = "true")
	public boolean isRequireLoginForChangeToSent();
	
	/**
	 * Flag indicating whether authentication is required for each Send to On task in the LastProductSelectionDialog
	 */
	@PropertyBeanAttribute(defaultValue = "true")
	public boolean isRequireLoginForSendToOn();
	
	@PropertyBeanAttribute(propertyKey ="MS_LINE_ID",defaultValue = "")
	public String getMSLineId();
	
	@PropertyBeanAttribute(propertyKey ="MS_PROCESS_POINT_ID",defaultValue = "")
	public String getMSProcessPointId();
	
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isProcessProduct();
	
	@PropertyBeanAttribute(defaultValue = "true")
	public boolean isMoveCurrentByKdLot();
	
	/**
	 * Reset interval the screen product Id field after hand scan
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "0")
	public int getReset();
	
	/**
	 * true, if the screen should automatically load expected product id
	 *
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isAutoLoadExpectedProductId();
	
	@PropertyBeanAttribute(defaultValue = "30")
	public int getMaxLotSize();
	
	@PropertyBeanAttribute(defaultValue = "5")
	public int getLotSizeInterval();
		
	@PropertyBeanAttribute(propertyKey ="CHECK_DUPLICATE_EXPECTED_PRODUCT_ID", defaultValue = "false")
	public boolean isCheckDuplicateExpectedProductId();
	
	/**
	 * Should broadcast when a production lot been set Complete.
	 *
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isBroadcastWhenComplete();
	
	/**
	 * Should process when a production lot been set Complete.
	 *
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isProcessProductOnWhenComplete();
	
	/**
	 * If true, should always refresh the screen. If false, wait for next filled production lot to refresh
	 *
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isAlwaysRefresh();	
	
	/**
	 * If true, check production lot send_status, disable or enable Move Up/Down menu item. 
	 * @return
	 */
	@PropertyBeanAttribute(propertyKey ="IS_CHECK_STATUS",defaultValue = "false")
	public boolean isCheckStatus();
	
	/**
	 * If true, the Send to Plc button will show in the LastProductSelectionDialog box.
	 * @return
	 */
	@PropertyBeanAttribute(propertyKey ="ALLOW_SEND_TO_PLC",defaultValue = "true")
	public boolean isAllowSendToPlc();

	/**
	 * Plan code that will use the SEQUENCE_INTERVAL value while release otherwise increment by 0.01 on maxValue
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public String getSourcePlanCode();
	
	
}