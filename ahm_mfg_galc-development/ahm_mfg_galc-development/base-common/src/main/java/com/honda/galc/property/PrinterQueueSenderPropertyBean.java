package com.honda.galc.property;

@PropertyBean(componentId ="PRINTER_QUEUE_SENDER")
public interface PrinterQueueSenderPropertyBean extends IProperty{

	@PropertyBeanAttribute(defaultValue="false")
	public boolean isDebug();
	
	@PropertyBeanAttribute(defaultValue="/tmp")
	public String getTempDir() ;
	
	@PropertyBeanAttribute(defaultValue="true")
	public boolean isCleanUp();
	
	@PropertyBeanAttribute(defaultValue="qprt -r -P")
	public String getPrintCommand();
	
	@PropertyBeanAttribute(defaultValue="")
	public String getJasperPrintCommand();

	@PropertyBeanAttribute()
	public String getTemplateDir();
	
	@PropertyBeanAttribute(defaultValue="false")
	public boolean isReloadTemplateIfUpdated();
}
