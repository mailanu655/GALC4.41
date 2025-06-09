package com.honda.ahm.lc.service;

import com.honda.ahm.lc.common.logging.Logger;
import com.honda.ahm.lc.common.logging.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.jms.JmsException;
import org.springframework.jms.core.JmsTemplate;

@Service(value = "queueManagerService")
public class QueueManagerService implements IQueueManagerService {
	
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private JmsTemplate jmsTemplate;
	
	
	protected Logger getLogger() {
		return logger;
	}

	public String send(String destinationQueueName, String message) {
		try {
			
			jmsTemplate.convertAndSend(destinationQueueName, message);
			logger.info("Successfully sent message to queue - "+ destinationQueueName);
			return "OK";
		} catch (JmsException ex) {
			getLogger().error(ex.getMessage());
			return "";
		}catch (Exception ex) {
			getLogger().error(ex.getMessage());
			return "";
		}
	}

	public String recv(String sourceQueueName) {
		
		try {
			jmsTemplate.setReceiveTimeout(5);
			logger.info("Reading message from queue - "+ sourceQueueName);
			return jmsTemplate.receiveAndConvert(sourceQueueName).toString();
		} catch (JmsException ex) {
			getLogger().error(ex.getMessage());
			return "";
		}catch (Exception ex) {
			getLogger().error(ex.getMessage());
			return "";
		}
	}
}
