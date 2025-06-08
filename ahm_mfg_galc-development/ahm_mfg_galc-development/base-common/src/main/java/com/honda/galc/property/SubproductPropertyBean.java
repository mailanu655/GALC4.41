package com.honda.galc.property;

import java.util.Map;

@PropertyBean(componentId ="prop_Subproducts")
public interface SubproductPropertyBean extends IProperty {

	/**
	 * boolean value to determine if spec code check is required or not
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "TRUE")
	public boolean doesRequireSpecCode();
	
	/**
	 * Product check types for the subproduct
	 * @return
	 */
	@PropertyBeanAttribute (propertyKey ="SUBPRODUCT_CHECK_TYPES_MAP")
	public Map<String, String> getSubproductCheckTypesMap();
	/**
	 * Process point of subproduct line to track the subproduct
	 * @return
	 */
	@PropertyBeanAttribute
	public Map<String, String> getInstallProcessPointMap();
	
	/**
	 * Build Atrribute value of the product to check the expected spec code of subproduct
	 * @return
	 */
	@PropertyBeanAttribute
	public Map<String, String> getSpecCodeFromBuildAttrMap();
	
	/**
	 * Process points separated by comma to check if product has passed them or not
	 * @return
	 */
	@PropertyBeanAttribute
	public Map<String, String> getCheckPreviousProcessMap();
	
	/**
	 * boolean value to determine if mainNo of Mbpn is used or subproduct type
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "FALSE")
	public boolean isUseMainNoFromPartSpec();
	
	/**
	 * Boolean value to dtermine if use BOM to validate MBPN
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "FALSE")
	public boolean isValidateFromBom();
	
	
}
