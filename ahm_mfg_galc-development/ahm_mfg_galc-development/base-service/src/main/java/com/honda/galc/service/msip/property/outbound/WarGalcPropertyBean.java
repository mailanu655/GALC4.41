package com.honda.galc.service.msip.property.outbound;

import java.util.Map;

import com.honda.galc.property.PropertyBeanAttribute;

public interface WarGalcPropertyBean extends MsipOutboundPropertyBean {
	
	@PropertyBeanAttribute(defaultValue = "")
	public String[] getSelectingProcessPointIds();
	
	@PropertyBeanAttribute(defaultValue = "")
	public String[] getNotSellableTrackingStatus();
	
	@PropertyBeanAttribute(defaultValue = "")
	public Map<String, String> getProcessPointIds();
	
	@PropertyBeanAttribute(defaultValue = "")
	public String getEngineServiceUrl();
	
	@PropertyBeanAttribute(defaultValue = "")
	public String getAeOffProcessPointId();
	
	@PropertyBeanAttribute(defaultValue = "BLOCK")
	public String getBlockPartName();
	
	@PropertyBeanAttribute(defaultValue = "HEAD")
	public String getHeadPartName();
	
	@PropertyBeanAttribute(defaultValue = "TRANSMISSION")
	public String getTransmissionPartName();
	
	@PropertyBeanAttribute(defaultValue = "-1")
	public int getBlockNumberMachineIx();
	
	@PropertyBeanAttribute(defaultValue = "-1")
	public int getHeadNumberMachineIx();
	
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean getIsExcludeListedPlantCodes();
	
	@PropertyBeanAttribute(defaultValue = "")
	public String[] getPlantCodesToExclude();
	
	@PropertyBeanAttribute(defaultValue = "1950-01-01 00:00:00")
	public String getTimeStampValue();

	@PropertyBeanAttribute(defaultValue = "")
	public Map<String, String> getProcessPointLocations();
}
