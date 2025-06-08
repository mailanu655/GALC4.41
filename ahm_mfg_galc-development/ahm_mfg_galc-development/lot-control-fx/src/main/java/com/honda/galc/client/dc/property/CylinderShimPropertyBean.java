package com.honda.galc.client.dc.property;

import java.util.Map;

import com.honda.galc.property.IProperty;
import com.honda.galc.property.PropertyBean;
import com.honda.galc.property.PropertyBeanAttribute;

/**
 * 
 * @author Wade Pei <br>
 * @date Jul 8, 2014
 */
@PropertyBean
public interface CylinderShimPropertyBean extends IProperty {

	@PropertyBeanAttribute(defaultValue="3")
	public int getCylinderNum();

	@PropertyBeanAttribute
	public Map<String, String> getCylinderStartNos();
	
	@PropertyBeanAttribute
	public Map<String, String> getCylinderShimMeasMin();
	
	@PropertyBeanAttribute
	public Map<String, String> getCylinderShimMeasMax();
	
	@PropertyBeanAttribute
	public Map<String, String> getDefaultBaseShimIds();

	@PropertyBeanAttribute(defaultValue="Shim_ID_Size_Mapping")
	public String getFormIdForShimIdMapping();
	
	@PropertyBeanAttribute(defaultValue="15")
	public int getFontSize();
	
	/*
	 * Base Shim Title
	 */
	@PropertyBeanAttribute(defaultValue="Base Shim Id")
	public String getBaseShimTitle();
	
	/*
	 * Base Gap Title
	 */
	@PropertyBeanAttribute(defaultValue="Measurements")
	public String getBaseGapTitle();
	
	/*
	 * Actual Shim Title
	 */
	@PropertyBeanAttribute(defaultValue="Actual Shim Id")
	public String getActualShimTitle();
	
	/*
	 * Final Gap Title
	 */
	@PropertyBeanAttribute(defaultValue="Final Measurements")
	public String getFinalGapTitle();
	
	/*
	 * Title for each shim type
	 */
	@PropertyBeanAttribute
	public Map<String, String> getShimTypeTitles();
	
	/*
	 * Operation names for Base Shims per Shim type
	 */
	@PropertyBeanAttribute
	public Map<String, String> getBaseShimIds();
	
	/*
	 * Operation names for Base Gaps per Shim type
	 */
	@PropertyBeanAttribute
	public Map<String, String> getBaseGaps();
	
	/*
	 * Operation names for Actual Shims per Shim type
	 */
	@PropertyBeanAttribute
	public Map<String, String> getActualShimIds();
	
	/*
	 * Operation names for Final gaps per Shim type
	 */
	@PropertyBeanAttribute
	public Map<String, String> getFinalGaps();
	
	
}

