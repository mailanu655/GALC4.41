/**
 * 
 */
package com.honda.galc.property;


/**
 * 
 * <h3>MqprinterDevicePropertyBean Class description</h3>
 * <p> MqprinterDevicePropertyBean description </p>
 * 
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Update by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 *
 * </TABLE>
 *   
 * @author Jeffray Huang<br>
 * Aug 26, 2013
 *
 *
 */
@PropertyBean(componentId="DEFAULT_MQ_DEVICE")
public interface DefaultMQDevicePropertyBean extends IProperty {
		
	@PropertyBeanAttribute(propertyKey="name")
	String getPrinterName();
	
	@PropertyBeanAttribute(propertyKey="queuemanager")
	String getQueueManagerName();

	@PropertyBeanAttribute(propertyKey="channel")
	String getChannelName();
	
	@PropertyBeanAttribute(propertyKey="hostname")
	String getHostName();
	
	@PropertyBeanAttribute(propertyKey="port")
	int getPort();
	
	/**
	 * mq connection user name - default=""  no user/pass required
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue="")
	String getUserId();
	
	/**
	 * mq connection password - default=""  no user/pass required
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue="")
	String getPassword();
	
}

