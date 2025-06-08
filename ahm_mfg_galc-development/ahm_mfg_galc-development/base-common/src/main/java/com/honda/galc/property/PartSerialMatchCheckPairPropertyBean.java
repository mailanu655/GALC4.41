package com.honda.galc.property;

import java.util.Map;
/**
 * 
 * @author Gangadhararao Gadde
 * @date Aug 30, 2016
 */

@PropertyBean
public interface PartSerialMatchCheckPairPropertyBean extends IProperty{
/*Define the Part Serial Match check Pair
 * 
 */
	@PropertyBeanAttribute(defaultValue = "")
	public Map<String, String> getPartSerialMatchCheckPair();

	

}
