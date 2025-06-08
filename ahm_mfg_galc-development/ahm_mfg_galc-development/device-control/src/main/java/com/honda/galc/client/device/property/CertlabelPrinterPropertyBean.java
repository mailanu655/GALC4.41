package com.honda.galc.client.device.property;

import com.honda.galc.property.DevicePropertyBean;
import com.honda.galc.property.PropertyBean;
import com.honda.galc.property.PropertyBeanAttribute;

@PropertyBean(componentId="Default_Certlabel_Device", prefix="device.certlabel")
public interface CertlabelPrinterPropertyBean extends DevicePropertyBean{
	
		@PropertyBeanAttribute(propertyKey="printBuffer")
		String getPrintBuffer();
		
		@PropertyBeanAttribute(propertyKey="printProcessPoint")
		String getPrintProcessPoint();
		
		@PropertyBeanAttribute(propertyKey="backup")
		String getBackup();
		
		@PropertyBeanAttribute(propertyKey="primary")
		String getPrimary();
		
		@PropertyBeanAttribute(propertyKey="maxPrintsPerCycle")
		String getMaxPrintsPerCycle();
		

	}
