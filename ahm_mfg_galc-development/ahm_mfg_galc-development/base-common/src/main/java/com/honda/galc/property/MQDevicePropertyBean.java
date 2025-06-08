package com.honda.galc.property;

public interface MQDevicePropertyBean extends DefaultMQDevicePropertyBean{
	@PropertyBeanAttribute(propertyKey="queuename")
	String getQueueName();
	
	@PropertyBeanAttribute(propertyKey="messageconvertor", defaultValue="JsonMqMessageConvertor")
	String getMessageConvertor();
	
	/**
	 * Message ID
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue="")
	String getMessageId();
	
	/**
	 * data element delimiter 
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = ",")
	public String getDelimiter();
	
	@PropertyBeanAttribute(defaultValue = "")
	public String getMessageType();
	
	
}
