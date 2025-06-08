package com.honda.galc.client.teamleader.property;

import java.util.Map;

import com.honda.galc.property.PropertyBean;
import com.honda.galc.property.PropertyBeanAttribute;
import com.honda.galc.property.SystemPropertyBean;

/**
 * 
 * @author Gangadhararao Gadde
 * @date feb 13, 2017
 */
@PropertyBean
public interface InProcessProductMaintPanelPropertyBean extends  SystemPropertyBean{

	/**
	 * Division and Line mapping property for the InProcess Product Maintenance Panel
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public Map<String, String> getLineDivisionMapping();
	
	/**
	 *  Flag to indicate to update product Carrier table
	 * 
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isUpdateProductCarrier();
	
		
}
