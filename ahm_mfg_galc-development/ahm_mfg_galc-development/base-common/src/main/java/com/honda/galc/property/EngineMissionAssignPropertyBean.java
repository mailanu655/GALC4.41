package com.honda.galc.property;

/**
 * 
 * @author Gangadhararao Gadde
 * @date May 04, 2016
 */
@PropertyBean(componentId ="DEFAULT_ENGINE_MISSION_ASSIGN")
public interface EngineMissionAssignPropertyBean extends IProperty{

	
	/**
	 * Engine Part Name
	 * 
	 */
	@PropertyBeanAttribute(defaultValue ="ENGINE")
	public String getEnginePartName();
	
	/**
	 * Mission Part Name
	 * 
	 */
	@PropertyBeanAttribute(defaultValue ="MISSION")
	public String getMissionPartName();
	
	/**
	 * Mission Type Part Name
	 * 
	 */
	@PropertyBeanAttribute(defaultValue ="MISSION_TYPE")
	public String getMissionTypePartName();
	
	/**
	 * Mission Product Type
	 * 
	 */
	@PropertyBeanAttribute(defaultValue ="FRAME")
	public String getEngineInstalledPartProductType();
	
	/**
	 * Mission Type Part Name
	 * 
	 */
	@PropertyBeanAttribute(defaultValue ="ENGINE")
	public String getMissionInstalledPartProductType();
	
	
	/**
	 * Validate and Update Mission Type Information
	 * 
	 * 
	 */
	@PropertyBeanAttribute(defaultValue = "true")
	public boolean isMissionTypeRequired();
	
	@PropertyBeanAttribute(defaultValue = "ENGINE_SPEC")
	public String getMissionModelSource();
	
	/**
	 * Comma separated list of valid previous lines for
	 * Engine load process point
	 * 
	 * @return
	 */
	@PropertyBeanAttribute (defaultValue = "")
	public String getValidEngineLoadPreviousLines();
	
	@PropertyBeanAttribute (defaultValue = "")
	public String getEngineTrackingPpid();
	
	/**
	 * If true, helps to update engine MTO with right expected one for Commonization 
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	boolean isEngineMtoUpdateEnabled();
	
	@PropertyBeanAttribute (defaultValue = "false")
	public Boolean isHoldVin();
	
	/**
	 * If true, put VIN on "AT SHIPPING" hold if associated engine is on hold. 
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	boolean isHoldByEngine();
	
	@PropertyBeanAttribute (defaultValue = "")
	public String getVinHoldReason();
	
	@PropertyBeanAttribute(defaultValue = "true")
	boolean isAutoMissionDeassignEnabled();
	
	/**
	 * Accepts a comma-separated list of site names. 
	 * If the value is set, the check will look for holds at the sites in the list. 
	 * If left blank, all sites will be checked. 
	 * If the property is not set, the check will be skipped.
	 */
	@PropertyBeanAttribute
	public String getExternalHoldCheckSites();
	
}
