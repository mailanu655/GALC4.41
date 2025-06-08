package com.honda.galc.client.datacollection.property;

import com.honda.galc.property.IProperty;
import com.honda.galc.property.PropertyBean;
import com.honda.galc.property.PropertyBeanAttribute;

@PropertyBean(componentId ="Default_BlockBoreMeasure")
public interface BlockBoreMeasurePropertyBean extends IProperty{
	/**
	 * define just in use with BlockBoreMeasureProcessor strategy
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "BORE MEASURE")
	public String getBoreMeasurePartName();
	
	
	/**
	 * define the bore measure position in the S/N 
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "0")
	public int getBoreMeasureSnPosition();
}