package com.honda.galc.property;

@PropertyBean(componentId ="Default_ProductionLotBackout")
public interface ProductionLotBackoutPropertyBean extends IProperty {

	/**
	 * CSV list of process point ids at which a VIN may be tracked when it is created.
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public String[] getInitialProcessPointIds();

}
