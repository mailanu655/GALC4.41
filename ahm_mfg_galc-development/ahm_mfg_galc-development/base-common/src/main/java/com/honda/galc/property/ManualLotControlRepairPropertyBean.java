package com.honda.galc.property;

import java.util.Map;

/**
 * 
 * <h3>ManualLotControlRepairPropertyBean</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> ManualLotControlRepairPropertyBean description </p>
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
 * <TD>LnT Infotech</TD>
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


@PropertyBean(componentId ="MANUAL_LOT_CONTROL_REPAIR_INFO")
public interface ManualLotControlRepairPropertyBean extends IProperty{
	
	
	/**
	 * Enable to set result NG - GaugeFlex specific TL function
	 * @return
	 */
	@PropertyBeanAttribute (defaultValue = "false")
	public boolean isEnableSetResultNg();
	

	/**
	 * Indicate if the repair client enabled to remove/insert build result
	 * Default to true
	 * @return
	 */
	@PropertyBeanAttribute (defaultValue = "true")
	public boolean isRepairEnabled();
	
	/**
	 * Properties for repair Mission Install
	 * - ProcessPointId
	 * - MissionPartName
	 * - MissionTypePartName
	 * @return
	 */
	@PropertyBeanAttribute
	public Map<String, String> getMissionInstallPropertyMap();
	
	/**
	 * Properties for repair Engine Head Marriage
	 * - ProcessPointId
	 * - LastPassingPpid
	 * - HeadPartName
	 * - McOffProcessPointId
	 */
	@PropertyBeanAttribute
	public Map<String, String> getHeadMarriagePropertyMap();
	
	
	
	/**
	 * Properties for Block Load repair
	 * - ProcessPointId
	 * - LastPassingPpid
	 * - BlockPartName
	 * - BlockPartNameSnType (MC | DC | IN)
	 * - McOffProcessPointId
	 * @return
	 */
	@PropertyBeanAttribute
	public Map<String, String> getBlockLoadPropertyMap();


	
	/**
	 * Properties for Crankshaft Load repair
	 * - ProcessPointId
	 * - LastPassingPpid
	 * - CrankshaftPartName
	 * - McOffProcessPointId
	 * @return
	 */

	@PropertyBeanAttribute
	public Map<String, String> getCrankshaftLoadPropertyMap();
	
	/**
	 * Properties for Conrod Load repair
	 * - ProcessPointId
	 * - LastPassingPpid
	 * - ConrodPartName
	 * - McOffProcessPointId
	 * @return
	 */
	@PropertyBeanAttribute
	public Map<String, String> getConrodLoadPropertyMap();

	
	/**
	 * Get the product type. 
	 */
	@PropertyBeanAttribute
	public String getProductType();
	
	/**
	 * Get the Line id. 
	 */
	@PropertyBeanAttribute (defaultValue = "")
	public String getLineId();
	
	/**
	 * Get the Division id. 
	 */
	@PropertyBeanAttribute (defaultValue = "")
	public String getDivisionId();
	
	/**
	 * 	Defined a selected processpoints
	 * @return
	 */
	@PropertyBeanAttribute (defaultValue = "")
	public String[] getFilterByRequiredParts();
	
	/**
	 * 	Defined a selected processpoints
	 * @return
	 */
	@PropertyBeanAttribute (defaultValue = "")
	public String getFilterByInstalledParts();
	
	

	/**
	 * For head less process point, quick fix only change the installed part status
	 * Headless Quick fix for per process point default value is true
	 * 
	 * @return
	 */
	@PropertyBeanAttribute
	public Map<String, String> getQuickFixHeadLessMap();
	
	/**
	 * Default value for Quick Fix head less process point
	 * true for HCM
	 * @return
	 */

	@PropertyBeanAttribute (defaultValue = "true")
	public boolean isQuickFixHeadLess();
	
	/**
	 * 	Defines list of  process points for which required part verification is required
	 * 
	 */
	@PropertyBeanAttribute (defaultValue = "")
	public String[] getRequiredPartsShipProcess();

	@PropertyBeanAttribute
	public Map<String, String> getPartCheckTypes();
	
	//Indicates the columns to be displayed on Manual LotControl Repair Screen 
	@PropertyBeanAttribute (defaultValue = "STATUS,STATUS_MEASURE,PROCESS_POINT_NAME,PART_NAME,PART_SERIAL_NUMBER,MEASUREMENT_RESULT,TIME_STAMP,PROCESS_POINT,REPAIRED")
	public String[] getMlcrColumns();
	
	/**
	 * A list of all clients need authenticate before save
	 * @return
	 */
	@PropertyBeanAttribute (defaultValue = "false")
	public String getClientsNeedAuthenticateUserToSave();
	
	
	/**
	 * Indicate to confirm before save into database - dialog confirmation
	 * default to true - as it works today
	 * @return
	 */
	@PropertyBeanAttribute (defaultValue = "true")
	public boolean isSaveConfirmation();
	
	/**
	 * Security group name for authorize user
	 * @return
	 */
	@PropertyBeanAttribute (defaultValue = "DT")
	public String getAuthorizationGroup();
	
	/**
	 * Indicate that the client repair device driven data
	 * Turn this on only when needed to get better performance
	 * @return
	 */
	@PropertyBeanAttribute (defaultValue = "false")
	public boolean isRepairDeviceDrivenData();
	
	/* If the DEFAULT_YES_BUTTON_TO_SAVE value is true, 
	 * the "Yes" button would be selected after clicking "Save" button.
	 */
	@PropertyBeanAttribute (defaultValue = "false")
	public boolean getDefaultYesButtonToSave();
	
	/**
	 * Recovery Manul On/Off application Id map
	 * @return
	 */
	public Map<String, String> getOnRecoveryApplicationIds();
	
	/**
	 * Security group name for TL confirmation
	 * @return
	 */
	@PropertyBeanAttribute (defaultValue = "")
	public String getTlConfirmAuthorizationGroup();

	/**
	 * Flag to enable/disable the Default Torque Value Button in the Enter Result Dialog window
	 * @return
	 */
	@PropertyBeanAttribute (defaultValue = "false")
	public boolean isDefaultTorqueValueButtonDisabled();
	
}
