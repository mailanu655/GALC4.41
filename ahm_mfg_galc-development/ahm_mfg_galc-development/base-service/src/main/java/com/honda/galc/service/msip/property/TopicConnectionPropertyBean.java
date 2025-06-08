package com.honda.galc.service.msip.property;

import com.honda.galc.service.msip.property.BaseMsipPropertyBean;
import com.honda.galc.property.PropertyBean;
import com.honda.galc.property.PropertyBeanAttribute;

/**
 * @author Subu Kathiresan
 * @date Apr 26, 2017
 */
@PropertyBean(componentId="DEFAULT_TOPIC_CONNECTION")
public interface TopicConnectionPropertyBean extends BaseMsipPropertyBean {

	@PropertyBeanAttribute()
	String getDestinationName();
	
	@PropertyBeanAttribute()
	String getUserName();
	
	@PropertyBeanAttribute()
	String getPassword();
	
	@PropertyBeanAttribute()
	String getConnectionString();
	
	@PropertyBeanAttribute(defaultValue="org.apache.activemq.jndi.ActiveMQInitialContextFactory")
	String getInitialContextFactory();
}
