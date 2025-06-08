package com.honda.galc.property;

@PropertyBean(componentId = "DEFAULT_PDDA_PLATFORM")
public interface PddaPlatformPropertyBean extends IProperty  {

	/**
	 * Specifies Active Plant Location Code
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public String getPlantLocCode();
	
	/**
	 * Specifies Active Department Code
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public String getDeptCode();
	
	/**
	 * Specifies Active Model Year
	 */
	@PropertyBeanAttribute(defaultValue = "0.0")
	public Float getModelYearDate();
	
	/**
	 * Specifies Active Production Schedule Quantity
	 */
	@PropertyBeanAttribute(defaultValue = "0.0")
	public Float getProdSchQty();
	
	/**
	 * Specifies Active ASM Line No
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public String getAsmLineNo();
	
	/**
	 * Specifies Active Vehicle Model Code
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public String getVehicleModelCode();
}
