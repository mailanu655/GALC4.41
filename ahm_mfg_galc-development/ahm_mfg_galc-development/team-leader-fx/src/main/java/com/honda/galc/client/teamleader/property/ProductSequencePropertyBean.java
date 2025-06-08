package com.honda.galc.client.teamleader.property;

import com.honda.galc.property.IProperty;
import com.honda.galc.property.PropertyBean;
import com.honda.galc.property.PropertyBeanAttribute;

@PropertyBean(componentId ="Default_ProductSequence")
public interface ProductSequencePropertyBean extends IProperty{
	
	@PropertyBeanAttribute
	public String getInProductSequenceId();
	
	@PropertyBeanAttribute
	public String getOutProductSequenceId();
	
	
}
