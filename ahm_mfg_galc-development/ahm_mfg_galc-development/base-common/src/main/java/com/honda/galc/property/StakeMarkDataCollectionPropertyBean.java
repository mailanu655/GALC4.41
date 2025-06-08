package com.honda.galc.property;

import com.honda.galc.property.IProperty;
import com.honda.galc.property.PropertyBean;
import com.honda.galc.property.PropertyBeanAttribute;

@PropertyBean
public interface StakeMarkDataCollectionPropertyBean extends IProperty{
	
	@PropertyBeanAttribute(defaultValue = "TINTED_GLASS")
	public String getTintedGlass();
	
	@PropertyBeanAttribute(defaultValue = "CLEAR_GLASS")
	public String getClearGlass();
	
	@PropertyBeanAttribute(defaultValue = "STAKED")
	public String getStakedMark();
	
	@PropertyBeanAttribute(defaultValue = "NOT_STAKED")
	public String getNotStakedMark();
	
	@PropertyBeanAttribute(defaultValue = "ROBOT SCAN REAR TINTED GLASS")
	public String getTintedRearGlassPartName();
	
	@PropertyBeanAttribute(defaultValue = "ROBOT SCAN REAR CLEAR GLASS")
	public String getClearRearGlassPartName();
	
	@PropertyBeanAttribute(defaultValue = "STAKED MARK")
	public String getStakedPartName();
	
	@PropertyBeanAttribute(defaultValue = "NOT STAKED MARK")
	public String getNotStakedPartName();
	
	@PropertyBeanAttribute(defaultValue = "GLASS")
	public String getGlassPartName();
	
	@PropertyBeanAttribute(defaultValue = "")
	public String getGlassInstallProcessPointId();
	
	@PropertyBeanAttribute(defaultValue = "")
	public String getGlassInstallClientId();
}


