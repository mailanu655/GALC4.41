/**
 * 
 */
package com.honda.galc.client.device.property;

import com.honda.galc.property.PropertyBean;
import com.honda.galc.property.PropertyBeanAttribute;

/**
 * @author Ryan Koors
 * Jan 23, 2012	
 */
@PropertyBean(componentId="Default_Mq_Printer_Device",prefix="device.mqprinter")
public interface MqprinterDevicePropertyBean extends PrinterDevicePropertyBean {
		
	@PropertyBeanAttribute(propertyKey="name")
	String getPrinterName();
	
	@PropertyBeanAttribute(propertyKey="queuemanager")
	String getQueueManager();

	@PropertyBeanAttribute(propertyKey="channel")
	String getChannel();

	
}
