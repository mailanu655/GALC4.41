package com.honda.galc.handheld.data;

import java.util.Map;
import com.honda.galc.property.IProperty;
import com.honda.galc.property.PropertyBean;
import com.honda.galc.property.PropertyBeanAttribute;

@PropertyBean(componentId = "HANDHELD")
public interface HandheldWebPropertyBean extends IProperty {
	@PropertyBeanAttribute
	String[] getAssociatePrefixes();

	@PropertyBeanAttribute
	String[] getRepairScreenIds();
	
	@PropertyBeanAttribute(defaultValue= "0")
	int getProxCardBarCodePrefixLength();

	@PropertyBeanAttribute(defaultValue= "0")
	int getProxCardBarCodeSuffixLength();
	
	/**
	 * @return Map<String, String> where the Key is ProductType name and value is a comma delimited list of Divisions
	 * This is used to decide if <InstalledPart> changes can be made to a <BaseProduct>  
	 */
	@PropertyBeanAttribute
	Map<String, String> getProductTypeInstalledPartUpdateDivisionsMap();

	@PropertyBeanAttribute(defaultValue="")
	String[] getProductTypesToIgnoreForUpdateDivisionChecks();

	@PropertyBeanAttribute(defaultValue = "false")
	boolean isProxCardValidationRequired();

	@PropertyBeanAttribute(defaultValue = "120")
	int getSessionTimeoutDuration();

	/**
	 * Flag indicates If check product Id validation should come from
	 * Build Attributes
	 */
	@PropertyBeanAttribute(defaultValue = "true")
	public boolean isProductIdValidationFromBuildAttributes();	
	
	@PropertyBeanAttribute
	public Map<String,String[]>  getMbpnMainNumberMap(Class<String[]> claz);
}
