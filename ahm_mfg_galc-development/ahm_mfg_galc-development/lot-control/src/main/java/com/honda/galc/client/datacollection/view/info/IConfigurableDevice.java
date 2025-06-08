package com.honda.galc.client.datacollection.view.info;

import java.util.List;

import com.honda.galc.entity.conf.ComponentProperty;

public interface IConfigurableDevice {
	public List<ComponentProperty> getChangedProperties(); 
}
