package com.honda.galc.service.lcvinbom.property;

import java.util.Map;

import com.honda.galc.property.IProperty;
import com.honda.galc.property.PropertyBean;
import com.honda.galc.property.PropertyBeanAttribute;

@PropertyBean(componentId ="VIN_BOM")
public interface VinBomPropertyBean extends IProperty {
	
	@PropertyBeanAttribute(defaultValue = "AF")
	String getProcessLocation();
	
	@PropertyBeanAttribute(defaultValue = "")
	String getAppUrl();
	
	@PropertyBeanAttribute(defaultValue = "")
	String getDcmsUrl();
	
	@PropertyBeanAttribute
	public Map<String, String> getDcmsRequestHeader();
	
	@PropertyBeanAttribute(defaultValue = "")
	String getPmqaUrl();
	
	@PropertyBeanAttribute(defaultValue = "")
	String getGmqaUrl();
	
	@PropertyBeanAttribute(defaultValue = "5000")
	Integer getGmqaConnectionTimeOut();
	
	@PropertyBeanAttribute(defaultValue = "5000")
	Integer getGmqaReadTimeOut();

	@PropertyBeanAttribute(defaultValue = "1000")
	public Integer getMassManualOverrideBatchSize();
	
	@PropertyBeanAttribute (defaultValue = " ")
	public String getShippedTrackingStatuses();
	
	@PropertyBeanAttribute
	public Map<String, String> getProcessPointMap();
	
	@PropertyBeanAttribute
	public Map<String, String> getGmqaRequestHeader();
	
	@PropertyBeanAttribute(defaultValue = "")
	String getDcmsPlantCodes();
	
	@PropertyBeanAttribute (defaultValue = "")
	public String getSystemExclude();
	
	@PropertyBeanAttribute(defaultValue = "1")
	public String getLineNumber();
}
