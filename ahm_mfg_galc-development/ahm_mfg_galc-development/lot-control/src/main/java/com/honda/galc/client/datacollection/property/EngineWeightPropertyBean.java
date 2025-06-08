
package com.honda.galc.client.datacollection.property;

import java.util.Map;

import com.honda.galc.property.IProperty;
import com.honda.galc.property.PropertyBean;
import com.honda.galc.property.PropertyBeanAttribute;

/**
 *
 * @author Wade Pei <br>
 * @date   Jan 19, 2014
 */
@PropertyBean(componentId ="Default_EngineWeight")
public interface EngineWeightPropertyBean extends IProperty {
    
    @PropertyBeanAttribute(defaultValue = "")
     public String[] getConRodWeightCategories();
	
	@PropertyBeanAttribute
	public Map<String, String> getPistonWeightCategories();

}
