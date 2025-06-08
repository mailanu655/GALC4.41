package com.honda.galc.client.teamleader.property;

import java.util.Map;

import com.honda.galc.client.property.CommonTlPropertyBean;
import com.honda.galc.property.PropertyBean;
import com.honda.galc.property.PropertyBeanAttribute;

@PropertyBean(componentId ="ManualLotControlRepair")
public interface ManualLotControlRepairPropertyBean extends CommonTlPropertyBean{

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
	public String getAuthorizationGroup();
	
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
	
	@PropertyBeanAttribute(defaultValue = "com.honda.galc.client.product.pane.ProductInputPane")
	String getProductInputPane();
	
	/**
	 * Indicate if Remove All Installed Part Data button is visible or not
	 * Default to false
	 * @return
	 */
	@PropertyBeanAttribute (defaultValue = "false")
	public boolean isRemoveAllButtonVisible();
	
	/**
	 * 	Defines list of  process points for which required part verification is required
	 * 
	 */
	@PropertyBeanAttribute (defaultValue = "")
	public String[] getRequiredPartsShipProcess();
	
}
