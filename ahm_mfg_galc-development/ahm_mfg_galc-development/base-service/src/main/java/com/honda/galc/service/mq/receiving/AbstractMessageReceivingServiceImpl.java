package com.honda.galc.service.mq.receiving;

import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.QueueConnectionFactory;
import javax.naming.NamingException;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.jndi.JndiTemplate;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.conf.ComponentPropertyDao;

/**
 * 
 * 
 * <h3>AbstractMessageReceivingServiceImpl Class description</h3>
 * <p> AbstractMessageReceivingServiceImpl description </p>
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
 * @author hcm_adm_008925<br>
 * Mar 16, 2018
 *
 *
 */
public abstract class AbstractMessageReceivingServiceImpl implements MessageListener,ApplicationContextAware {
	
	private String serviceId;
	
	private static String jndiPrefix = "java:comp/env/";
	private static final String MESSAGE_TYPE = "MESSAGE_TYPE";
	
	@Autowired
	ComponentPropertyDao componentPropertyDao;
	
	public AbstractMessageReceivingServiceImpl(String serviceId) {
		this.serviceId = serviceId;
	}
	
	
	 @Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException{
	 
		 AutowireCapableBeanFactory beanFactory = applicationContext.getAutowireCapableBeanFactory();

		 initBeans(beanFactory);
	 
	}

	public void initBeans(AutowireCapableBeanFactory beanFactory) {
		
		String messageType = getMessageType();
		
		if(StringUtils.isEmpty(messageType)) return;
		
		QueueConnectionFactory qcf = lookupConnectionFactory();
		
		if(qcf == null) return;
		
		Queue queue = lookupQueue();
		
		if(queue == null) return;
		
		DefaultMessageListenerContainer listenerContainer = new DefaultMessageListenerContainer();
		listenerContainer.setMessageListener(this);
		listenerContainer.setConnectionFactory(qcf);
		listenerContainer.setDestination(queue);
		listenerContainer.setSessionTransacted(true);
	    listenerContainer.setConcurrentConsumers(1);
	    listenerContainer.setMessageSelector(MESSAGE_TYPE + " = '" + messageType + "'" );
		
		System.out.println(serviceId + " - Message Type is " + messageType);
		
		try {
			beanFactory.autowireBean(listenerContainer);
			DefaultMessageListenerContainer container = (DefaultMessageListenerContainer)beanFactory.initializeBean(listenerContainer, "MessageListenerContainer_" + serviceId);
			container.start();
			System.out.println(serviceId + " - message queue initialization is successful");
		}catch(Exception ex) {
			ex.printStackTrace();
			System.out.println(serviceId + " - exception when starting message listener " + ex.getMessage());
		}
	}
	
	
	protected QueueConnectionFactory lookupConnectionFactory() {
		String jndicf = getJndiConnectionFactory();
		if(StringUtils.isEmpty(jndicf)) return null;
		
		QueueConnectionFactory qcf = null;
		
		JndiTemplate jndiTemplate = new JndiTemplate();
		try {
			qcf= (QueueConnectionFactory) jndiTemplate.lookup(jndiPrefix + jndicf);
		}catch (NamingException e) {
			e.printStackTrace();
			System.out.println(serviceId + " - unable to get jndi MQ connection factory " + jndicf + " due to : " + e.getMessage());
		}
		
		return qcf;
		
	}
	
	protected Queue lookupQueue() {
		String jndiQueue = getJndiQueue();
		if(StringUtils.isEmpty(jndiQueue)) return null;
		
		Queue queue = null;
		
		JndiTemplate jndiTemplate = new JndiTemplate();
		try {
			queue= (Queue) jndiTemplate.lookup(jndiPrefix + jndiQueue);
		}catch (NamingException e) {
			e.printStackTrace();
			System.out.println(serviceId + " - unable to get jndi MQ Queue " + jndiQueue + " due to : " + e.getMessage());
		}
		
		return queue;
		
	}
	
	protected String getJndiConnectionFactory() {
		return getProperty("JNDI_CONNECTION_FACTORY","");
	}
	
	protected String getJndiQueue() {
		return getProperty("JNDI_QUEUE","");
	}
	
	protected String getMessageType() {
		return getProperty("MESSAGE_TYPE","");
	}
	
    public Logger getLogger() {
    	return Logger.getLogger(serviceId);
    }
    
    protected String getProperty(String propertyName,String defaultValue) {
    	String value = componentPropertyDao.findValueForCompIdAndKey(serviceId, propertyName);
    	return value == null ? defaultValue : value;
    }
    
    protected Boolean getPropertyBoolean(String propertyName,Boolean defaultValue) {
    	String value = componentPropertyDao.findValueForCompIdAndKey(serviceId, propertyName);
    	if(value == null) return defaultValue;
    	value = value.trim();
    	
    	return value.equalsIgnoreCase("Y") || value.equalsIgnoreCase("TRUE") 
 		       || value.equalsIgnoreCase("YES") || value.equalsIgnoreCase("1");
    }
    
}
