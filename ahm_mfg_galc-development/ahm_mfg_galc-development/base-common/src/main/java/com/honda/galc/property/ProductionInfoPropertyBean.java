package com.honda.galc.property;

import java.util.Map;

/**
 * The Interface ProductionInfoPropertyBean.
 * @author Sachin Kudikala
 * @date May 15, 2015
 */
@PropertyBean(componentId ="PRODUCTION_INFO")
public interface ProductionInfoPropertyBean extends IProperty{

	/**
	 * JIRA Ticket: RGALCDEV-2801
	 * Description: The purpose of this property is, if we setup this property, then in MLCR screen the system will not allow 
	 * 				to repair a product. Example: SHIPPED_PP_IDS{FRAME}=PP_XYZ	SHIPPED_PP_IDS{ENGINE}=PP_ABC  SHIPPED_PP_IDS{BLOCK}=PP_PQR 
	 * 				If the LAST_PASSING_PROCESS_POINT_ID of the product is any of these process points (of that product type), 
	 * 				then throw warning message.
	 * @param clazz
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public Map<String, String[]> getShippedPpIds(Class<?> clazz);
	
}
