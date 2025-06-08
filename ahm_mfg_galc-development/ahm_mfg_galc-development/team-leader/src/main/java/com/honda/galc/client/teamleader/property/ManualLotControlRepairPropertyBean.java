package com.honda.galc.client.teamleader.property;

import java.util.Map;

import com.honda.galc.client.property.CommonTlPropertyBean;
import com.honda.galc.property.PropertyBean;
import com.honda.galc.property.PropertyBeanAttribute;
/**
 * 
 * @author Gangadhararao Gadde
 * @date Aug 03, 2016
 */

@PropertyBean(componentId ="prop_ManualLotControlRepair")
public interface ManualLotControlRepairPropertyBean extends CommonTlPropertyBean {
	/**
	 * Max length of the product id 12 for Engine and 17 for Frame
	 * @return
	 */
	@PropertyBeanAttribute (defaultValue = "12")
	public int getMaxProductSnLength();

	/**
	 * Product Id label - different for product type. for example:
	 * Engine SN or VIN
	 * @return
	 */
	@PropertyBeanAttribute (defaultValue = "EIN:")
	public String getProductIdLabel();

	/**
	 * Product Spec label - based on product type. for example:
	 * MTOC or YMTO
	 * @return
	 */
	@PropertyBeanAttribute (defaultValue = "YMTO:")
	public String getProductSpecLabel();

	/**
	 * A list of all clients need authenticate before save
	 * @return
	 */
	public String getClientsNeedAuthenticateUserToSave();

	/**
	 * Security group name for authorize user
	 * @return
	 */
	@PropertyBeanAttribute (defaultValue = "DT")
	public String[] getAuthorizationGroup();
	
	
	/**
	 * Security group name for TL confirmation
	 * @return
	 */
	@PropertyBeanAttribute (defaultValue = "")
	public String[] getTlConfirmAuthorizationGroup();
	
	
	/**
	 * 	Defined a selected line 
	 * @return
	 */
	@PropertyBeanAttribute (defaultValue = "")
	public String getLineId();
	
	/**
	 * Define a selected Division
	 * @return
	 */
	@PropertyBeanAttribute (defaultValue = "")
	public String getDivisionId();
	
	/**
	 * Define a selected Zone
	 * @return
	 */
	@PropertyBeanAttribute (defaultValue = "")
	public String getZoneId();
	
	
	//----------  Engine specific properties --------------- 
	
	// The Mission Install, Head Marriage, Block Load repair function will be
	// turned on only if its property map is provided. for example, to support 
	// Mission Install, but does not need Head Marriage then, provide properties 
	// for Mission Install only.
	
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
	 * Properties for repair Mission Install
	 * - ProcessPointId
	 * - MissionPartName
	 * - MissionTypePartName
	 * @return
	 */
	@PropertyBeanAttribute
	public Map<String, String> getMissionInstallPropertyMap();
	
	/**
	 * Properties for repair Engine Load
	 * - ProcessPointId
	 * - EnginePartName
	 * @return
	 */
	@PropertyBeanAttribute
	public Map<String, String> getEngineInstallPropertyMap();
	
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
	 * Default value for Quick Fix head less process point
	 * true for HCM
	 * @return
	 */
	
	@PropertyBeanAttribute (defaultValue = "true")
	public boolean isQuickFixHeadLess();
	
	/**
	 * For head less process point, quick fix only change the installed part status
	 * Headless Quick fix for per process point default value is true
	 * 
	 * @return
	 */
	@PropertyBeanAttribute
	public Map<String, String> getQuickFixHeadLessMap();

	
	
	@PropertyBeanAttribute
	public Map<String, String> getPartCheckTypes();
	
	
	/* If the DEFAULT_YES_BUTTON_TO_SAVE value is true, 
	 * the "Yes" button would be selected after clicking "Save" button.
	 */
	@PropertyBeanAttribute (defaultValue = "false")
	public boolean getDefaultYesButtonToSave();
	
	@PropertyBeanAttribute (defaultValue = "")
	public String getProcessPointForRequiredParts();
	
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
	 * Indicate that the client repair device driven data
	 * Turn this on only when needed to get better performance
	 * @return
	 */
	@PropertyBeanAttribute (defaultValue = "false")
	public boolean isRepairDeviceDrivenData();

	/**
	 * Indicate to confirm before save into database - dialog confirmation
	 * default to true - as it works today
	 * @return
	 */

	@PropertyBeanAttribute (defaultValue = "true")
	public boolean isSaveConfirmation();
	
	/**
	 * Indicate if the repair client enabled to remove/insert build result
	 * Default to true
	 * @return
	 */
	@PropertyBeanAttribute (defaultValue = "true")
	public boolean isRepairEnabled();
	
	@PropertyBeanAttribute (defaultValue = "false")
	public boolean isLcRepairFilterEnabled();
	
	
	/**
	 * Indicate if multiple repair allowed for headless quick repair
	 * Default to false
	 * @return
	 */
	@PropertyBeanAttribute (defaultValue = "false")
	public boolean isMultipleRepairEnabled();

	
	//Indicates the columns to be displayed on Manual LotControl Repair Screen 
	@PropertyBeanAttribute (defaultValue = "STATUS,STATUS_MEASURE,PROCESS_POINT_NAME,PART_NAME,PART_SERIAL_NUMBER,MEASUREMENT_RESULT,TIME_STAMP,PROCESS_POINT,REPAIRED")
	public String[] getMlcrColumns();
	
	/**
	 * invoke on process when the product is not exist in database
	 */
	@PropertyBeanAttribute (defaultValue = "false")
	public boolean isInvokeOnProcess();
	

	/**
	 * Recovery Manual On/Off application Id map
	 * @return
	 */
	public Map<String, String> getOnRecoveryApplicationIds();

	/**
	 * Enable to set result NG - GaugeFlex specific TL function
	 * @return
	 */
	@PropertyBeanAttribute (defaultValue = "false")
	public boolean isEnableSetResultNg();
	
	/**
	 * 	Defines list of  process points for which required part verification is required
	 * 
	 */
	@PropertyBeanAttribute (defaultValue = "")
	public String[] getRequiredPartsShipProcess();
	
	/**
	 * Defines list of security groups which are authorized to override the duplicate part alarm.
	 */
	@PropertyBeanAttribute (defaultValue = "")
	public String[] getDupPartOverrideAuthGroup();
		
	/**
	 * Enable to set result NG - GaugeFlex specific TL function
	 * @return
	 */
	@PropertyBeanAttribute (defaultValue = "false")
	public boolean isEnableSubProductView();
	
	/**
	 * Return Bearing Select process point id
	 * @return
	 */
	@PropertyBeanAttribute (defaultValue = "")
	public String getBearingSelectProcessPointId();
	
	/**
	 * Flag to enable Manual Lot Repair client to update Bearing Select Result
	 * @return
	 */
	@PropertyBeanAttribute (defaultValue = "false")
	public boolean isManualRepairUpdateBearingSelectResult();
	
	/**
	 * Indicator to skip product Id length check
	 * @return
	 */

	@PropertyBeanAttribute (defaultValue = "false")
	public boolean isProductIdCheckDisabled();
	
	/**
	 * Flag to enable/disable the Default Torque Value Button in the Enter Result Dialog window
	 * @return
	 */
	@PropertyBeanAttribute (defaultValue = "false")
	public boolean isDefaultTorqueValueButtonDisabled();
	
	/**
	 * Defines list of security groups which are authorized to override the Battery Expiration.
	 */
	@PropertyBeanAttribute (defaultValue = "")
	public String[] getBattExpOverrideAuthGroup();
	
}
