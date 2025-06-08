package com.honda.galc.service.msip;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.service.msip.dto.outbound.IMsipOutboundDto;
import com.honda.galc.service.msip.property.TopicConnectionPropertyBean;
import com.honda.galc.service.msip.property.outbound.MsipOutboundPropertyBean;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.ToStringUtil;

/**
 * @author Subu Kathiresan
 * @date Apr 26, 2017
 */
public class PublishToTopicTask implements Runnable {

	private static final String SITE_NAME = "SITE_NAME";
	private static final String PLANT_NAME = "PLANT_NAME";
	private static final String PROCESS_POINT_ID = "PROCESS_POINT_ID";
	
	private IMsipOutboundDto dto = null;
	
	private TopicConnectionPropertyBean propertyBean = PropertyService.getPropertyBean(TopicConnectionPropertyBean.class);
	
	public PublishToTopicTask(IMsipOutboundDto dto, MsipOutboundPropertyBean propertyBean) {
		this.setDto(dto);
		if(propertyBean!=null){
			this.setPropertyBean(propertyBean);
		}
	}

	public IMsipOutboundDto getDto() {
		return dto;
	}

	public void setDto(IMsipOutboundDto dto) {
		this.dto = dto;
	}

	public TopicConnectionPropertyBean getPropertyBean() {
		return propertyBean;
	}

	public void setPropertyBean(TopicConnectionPropertyBean propertyBean) {
		this.propertyBean = propertyBean;
	}

	public void run() {
		Connection connection = null;
		Session session = null;
		MessageProducer producer;
		String destName = null;

		try {
			destName = propertyBean.getDestinationName();
            // Create a ConnectionFactory
            ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(propertyBean.getConnectionString());
            getLogger().info("Connected to: " +  propertyBean.getConnectionString());

            // Create a Connection
            connection = connectionFactory.createConnection(propertyBean.getUserName(), propertyBean.getPassword());
            connection.start();

            // Create a Session
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            // Create the destination (Topic or Queue)
            Destination destination = session.createTopic(destName);
            getLogger().info("Sending message to topic: " +  destName);

			producer = session.createProducer(destination);
			TextMessage message = prepareMessage(session);
			getLogger().info("Publishing to topic: " + message.getText());
			producer.send(message);

		} catch (Exception e) {
			getLogger().error("Unexpected Error Occured: " + e.getMessage());
			e.printStackTrace();
		}
		finally {
			if (session != null) {
				try {
					session.close();
				} 
				catch (JMSException ignored) {}
			}
			if (connection != null) {
				try {
					connection.close();
				} 
				catch (JMSException ignored) {}
			}
		}
	}

	private TextMessage prepareMessage(Session session) throws JMSException {
		
		TextMessage message = session.createTextMessage();
		// set headers
		message.setStringProperty(SITE_NAME, getDto().getSiteName());
		message.setStringProperty(PLANT_NAME, getDto().getPlantName());
		message.setStringProperty(PROCESS_POINT_ID, getDto().getProcessPointId());
				
		getLogger().info("Site:"+getDto().getSiteName()+" Plant:"+getDto().getPlantName()+"PPID:"+getDto().getProcessPointId());
		// set JSON message
		message.setText(ToStringUtil.generateJsonToString(getDto()));
		return message;
	}
	
	private Logger getLogger() {
		return Logger.getLogger("PublishToTopic");
	}
}