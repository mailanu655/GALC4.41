/**
 * 
 */
package com.honda.galc.client.device.property;

import com.honda.galc.property.DevicePropertyBean;
import com.honda.galc.property.PropertyBean;
import com.honda.galc.property.PropertyBeanAttribute;

/**
 * @author Subu Kathiresan
 * Sep 26, 2011
 */
@PropertyBean(componentId="Default_Printer_Device", prefix = "device.printer")
public interface PrinterDevicePropertyBean extends DevicePropertyBean {
	
	@PropertyBeanAttribute(propertyKey="destinationPrinter", defaultValue = "")
	String getDestinationPrinter();
	
	@PropertyBeanAttribute(propertyKey="maxPrintsPerCycle", defaultValue = "1")
	int getMaxPrintsPerCycle();
	
	@PropertyBeanAttribute(propertyKey="prePrintQty", defaultValue = "0")
	int getPrePrintQty();
	
	@PropertyBeanAttribute(propertyKey="type", defaultValue = "Printer")
	String getType();
	
	@PropertyBeanAttribute(propertyKey="availPrinters", defaultValue = "")
	String getAvailPrinters();
	
	@PropertyBeanAttribute(propertyKey="templateName", defaultValue = "")
	String getTemplateName();
}
