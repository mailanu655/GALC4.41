/**
 * 
 */
package com.honda.galc.property;


/**
 * @author Subu Kathiresan
 * @date Oct 31, 2013
 * 
 * *@author gangadhararao gadde<br>
 * Jan 29,2014
 * Added  VQ Off and VQ Ship process points 
 */
@PropertyBean(componentId ="Default_FrameLineProperties")
public interface FrameLinePropertyBean extends BaseLinePropertyBean {

	/**
	 * provides the AF ON process point id, which may vary
	 * for different plants
	 */
	@PropertyBeanAttribute
	public String getAfOnProcessPointId();
	
	/**
	 * provides the AF OFF process point id, which may vary
	 * for different plants
	 */
	@PropertyBeanAttribute
	public String getAfOffProcessPointId();
	
	/**
	 * provides the VQ Confirmation process point id, which may vary
	 * for different plants
	 */
	@PropertyBeanAttribute
	public String getVqConfirmationProcessPointId();
	
	/**
	 * provides the Scrap process point id, which may vary
	 * for different plants
	 */
	@PropertyBeanAttribute
	public String getScrapProcessPointId();
	
	/**
	 * provides the Exception line id, which may vary
	 * for different plants
	 */
	@PropertyBeanAttribute
	public String getExceptionLineId();
	
	/**
	 * provides the Exception process point id, which may vary
	 * for different plants
	 */
	@PropertyBeanAttribute
	public String getExceptionProcessPointId();
	
	/**
	 * provides the VQ OFF process point id, which may vary
	 * for different plants
	 */
	@PropertyBeanAttribute
	public String getVqOffProcessPointId();
	
	/**
	 * provides the VQ Ship process point id, which may vary
	 * for different plants
	 */
	@PropertyBeanAttribute
	public String getVqShipProcessPointId();
	
	/**
	 * provides the Straggler Service to identify based on which lot 
	 * Production Lot or KD Lot
	 */
	@PropertyBeanAttribute
	public String getStragglerLotType();
	
	/**
	 * provides the Paint Off process point id, which may vary
	 * for different plants
	 */
	@PropertyBeanAttribute
	public String getPaintOffProcessPointId();
	
	/**
	 * The value of the MODEL_GROUPING_TBX's SYSTEM column under which build sheet configuration is being done.
	 */
	@PropertyBeanAttribute
	public String getBuildSheetModelGroupingSystem();
	
	/**
	 * Part Serial number used for Fills - mapped by engine firing flag
	 * Fill firing flag Part Sn format
	 *     <PART SERIAL NUMBER for Not Fired>,<PART SERIAL NUMBER for fired>[, PREFIX]
	 *     for example: NOT_FIRED,FIRED  OR NOT_FIRED,FIRED,1
	 */
	@PropertyBeanAttribute(defaultValue="NOT FIRED ENGINE,FIRED ENGINE,1")
	public String getEngineFiringFillPartSn();
	
	/**
	 * Key No Part Names - same as in oif interface
	 * separated by comma;
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public String getKeyNoPartName();
	
	
	/**
	 * Indicate to update purchase contract when ship car
	 * separated by comma;
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isUpdatePurchaseContract();
	
	/**
	 * Indicate to invoke shipping transaction oif service
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isInvokeShippingTransactionService();
	
	/**
	 * VIN scan type
	 * 1. CERT_LABEL, 2. FIC , 3. ALL
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "ALL")
	public String getVinScanType();
	
	/**
	 * Pull over process point id
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public String getPulloverProcessPointId();

	/**
	 * allow to assign engine at AF Off
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isAllowedAssignEngineAtAfOff();
	
	/**
	 * SendLocation
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public String getSendLocation();

	/**
	 * Indicate to invoke all hold checks on the VIN and/or Engine
	 * @return 
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isHoldCheck();
	
}

