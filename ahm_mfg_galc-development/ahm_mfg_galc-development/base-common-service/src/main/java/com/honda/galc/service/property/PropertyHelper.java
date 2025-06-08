package com.honda.galc.service.property;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.conf.ComponentProperty;
/**
 * 
 * Regional-HMIN Merged Version 
 *    
 * @version 0.2
 * @author Gangadhararao Gadde
 * @since Aug 09, 2012
 */

public class PropertyHelper {
	
	protected String componentId;
	
	private String DEFAULT_DELIMITER = ",;";
	
	public PropertyHelper(String componentId) {
		this.componentId = componentId;
	}
	
	public String getComponentId() {
		return componentId;
	}
	
	public String getProperty(String propertyName) {
		
		return PropertyService.getProperty(componentId, propertyName);
		
	}
	
	public String getProperty(String propertyName,String defaultValue) {
		
		return PropertyService.getProperty(componentId, propertyName,defaultValue);
		
	}
	
	public Boolean getPropertyBoolean(String propertyName,boolean defaultValue) {
		
		return PropertyService.getPropertyBoolean(componentId, propertyName,defaultValue);
		
	}
	
	public Integer getPropertyInt(String propertyName) {
		
		return PropertyService.getPropertyInt(componentId, propertyName);
		
	}
	
	public Integer getPropertyInt(String propertyName,int defaultValue) {
		
		return PropertyService.getPropertyInt(componentId, propertyName,defaultValue);
		
	}
	
	public List<ComponentProperty> getProperties(){
		
		return PropertyService.getComponentProperty(componentId);
		
	}
	
	public void refreshProperties() {
		
		PropertyService.refreshComponentProperties(componentId);
		
	}
	
	public String[] getPropertyArray(String propertyName) {
		
		String property = getProperty(propertyName);
		
		if(StringUtils.isEmpty(property)) return new String[0];
		
		return StringUtils.split(property, DEFAULT_DELIMITER);
		
	}
}
