package com.honda.galc.property;

public interface DeliveryOrderAdjustmentPropertyBean extends IProperty {
	
	@PropertyBeanAttribute(defaultValue = "13")
	public String getSubsCode();
	
	@PropertyBeanAttribute(defaultValue = "113")
	public String getShipper();
	
	@PropertyBeanAttribute(defaultValue = "0")
	public String getInvoiceSequenceNumber();
	
	@PropertyBeanAttribute(defaultValue = "")
	public String getSalesModelOptionCode();
	
	@PropertyBeanAttribute(defaultValue = "USD")
	public String getCurrency();
	
	@PropertyBeanAttribute(defaultValue = "USD")
	public String getCurrencySettlement();
	
	@PropertyBeanAttribute(defaultValue = "2")
	public String getProductGroupCode();
	
	@PropertyBeanAttribute(defaultValue = "1")
	public String getDeliveryFormCode();
}
