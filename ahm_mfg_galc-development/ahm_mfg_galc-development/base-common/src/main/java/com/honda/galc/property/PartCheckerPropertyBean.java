package com.honda.galc.property;

/**
 * Common bean for all part level checker properties
 * @author anusha koritala
 *
 */

@PropertyBean(componentId = "DEFAULT_PART_CHECKER")
public interface PartCheckerPropertyBean extends IProperty{

	@PropertyBeanAttribute(defaultValue = "yyMMdd")
	public String getManufacturedDateFormat();
	
	@PropertyBeanAttribute(defaultValue = "")
	public String getFloorBoardUnit();
	
	@PropertyBeanAttribute(defaultValue = "10")
	public String getFloorBoardTemperatureRange();
	
	@PropertyBeanAttribute(defaultValue = "")
	public String[] getUniqueModelTypeYear();
}
