package com.honda.galc.client.schedule;

import com.honda.galc.property.PropertyBeanAttribute;
/**
 * <h3>Class description</h3>
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
 * <TD>Dylan Yang</TD>
 * <TD>Jan 22, 2013</TD>
 * <TD>1.0</TD>
 * <TD>GY 20130122</TD>
 * <TD>Initial Realease</TD>
 * </TR>
 */

public interface ScheduleClientProperty extends DefaultScheduleClientProperty {

	@PropertyBeanAttribute(propertyKey ="IS_MOVE_BY_KD_LOT",defaultValue = "false")
	public boolean isMoveByKdLot();
	
	public String getProcessLocation();
	
	@PropertyBeanAttribute(propertyKey ="LOCK_FIRST_LOT",defaultValue = "true")
	public boolean lockFirstLot();
	
	
	public String getOnProcessPoint();
	
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
	
	@PropertyBeanAttribute(propertyKey ="CHECK_DUPLICATE_EXPECTED_PRODUCT_ID", defaultValue = "false")
	public boolean isCheckDuplicateExpectedProductId();

	/**
	 * Check expected product ID or not
	 * @return boolean
	 */
	@PropertyBeanAttribute(propertyKey ="CHECK_EXPECTED_PRODUCT_ID", defaultValue = "false")
	public boolean isCheckExpectedProductId();
	
	// Use to property to enable/disable playing sounds on the schedule client
	
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isSoundsEnabled();

	/**
	 * Mbpn Product types to be processed
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public String getMbpnProductTypes();
	
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isProcessMultiProduct();

	/**
	 * check mbpnSpec
	 * @return boolean
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isValidateMbpnSpec();

	/**
	 * update color of MbpnSpec
	 * @return boolean
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isUpdateSpecColor();
	
	/**
	 * Plan code that will use the SEQUENCE_INTERVAL value while release
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public String getSourcePlanCode();
}
