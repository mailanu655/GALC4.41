package com.honda.galc.property;

import java.awt.Color;
import java.util.Map;

import com.honda.galc.property.IProperty;
import com.honda.galc.property.PropertyBeanAttribute;

/**
 * 
 * 
 * <h3>EngineShippingPropertyBean Class description</h3>
 * <p> EngineShippingPropertyBean description </p>
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
 * Sep 22, 2014
 *
 *
 */
public interface EngineShippingPropertyBean extends IProperty{
	
	/**
	 * Shipping Printer Process Point ID
	 * @return
	 */
	@PropertyBeanAttribute(propertyKey ="PRINTER_PPID")
	public String getPrinterPPID();
	
	/**
	 * Default Emergency Quorum Size
	 * @return
	 */
	@PropertyBeanAttribute(propertyKey ="EMERGENCY_QUORUM_SIZE",defaultValue = "6")
	public int getEmergencyQuorumSize();
	
	/**
	 * Defalut Repair Quorum Size
	 * @return
	 */
	@PropertyBeanAttribute(propertyKey ="REPAIR_QUORUM_SIZE",defaultValue = "3")
	public int getRepairQuorumSize();
	
	/**
	 * Shipping Plant Map ex. SHIPPING_PLANT{HCM 01}=1, SHIPPING_PLANT{HMI 01} = 9
	 * @return
	 */
	@PropertyBeanAttribute(propertyKey ="SHIPPING_PLANT")
	public Map<String,String> getShippingPlants();
	
	/**
	 * Shipping Plant highlight color map
	 * @return
	 */
	@PropertyBeanAttribute(propertyKey ="SHIPPING_PLANT_COLOR")
	public Map<String,Color> getColors(Class<?> clazz);
	
	/**
	 * Default Shipping Trailer Size Map SHIPPING_TRAILER_SIZE{HMI 01} = 75
	 * @param clazz
	 * @return
	 */
	@PropertyBeanAttribute(propertyKey ="SHIPPING_TRAILER_SIZE")
	public Map<String,Integer> getTrailerSizes(Class<?> clazz);
	
	/**
	 * Quorum Loading sequence Map LOADING_SEQUENCE_MAP{HCM 01} = 3,2,1,6,5,4
	 * @param clazz
	 * @return
	 */
	@PropertyBeanAttribute(propertyKey ="LOADING_SEQUENCE_MAP")
	public Map<String, Integer[]> getLoadingSequenceMap(Class<?> clazz);
	
	/**
	 * Shipping Tracking Error name
	 * @return
	 */
	@PropertyBeanAttribute(propertyKey ="TRACKING_AREA",defaultValue ="ESTS")
	public String getTrackingArea();
	
	/**
	 * product check type
	 * @return
	 */
	@PropertyBeanAttribute(propertyKey ="PRODUCT_CHECK_TYPES",defaultValue ="")
	public String[] getProductCheckTypes();
	
	/**  
	 * authorization group 
	 */
	public String getAuthorizationGroup();
	
	/**
	 *  TL override enable flag
	 * @return
	 */
	@PropertyBeanAttribute(propertyKey ="TL_OVERRIDE_ENABLED",defaultValue ="TRUE")
	public boolean isOverrideEnabled();
	
	/**
	 * shipping pallet map
	 * @return
	 */
	@PropertyBeanAttribute(propertyKey ="PALLET_MAP",defaultValue ="")
	public Map<String,String> getPalletMap();
	
	@PropertyBeanAttribute(propertyKey ="SUPPORT_LEGACY_REQUEST", defaultValue ="TRUE")
	public boolean isSupportLegacyRequest();
	
	/**
	 * Tracking status when  unship Engine
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue ="AE1REP1")
	public String getUnshipTrackingStatus();
	
	/**
	 * Unship Process Point Id
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue ="AE0EN16211")
	public String getUnshipProcessPoint();
	
	/**
	 * Configure only authorized user to unship
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue ="true")
	public boolean isNeedAuthorizedUserToUnship();
	
	public Map<String,Integer> getBroadcastSeq(Class<?> clazz);
	
}
