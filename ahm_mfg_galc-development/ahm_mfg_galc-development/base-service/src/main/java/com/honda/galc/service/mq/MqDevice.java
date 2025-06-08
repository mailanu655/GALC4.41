package com.honda.galc.service.mq;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.data.ApplicationContextProvider;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DataContainerTag;
import com.honda.galc.data.DataContainerUtil;
import com.honda.galc.entity.conf.Device;
import com.honda.galc.property.MQDevicePropertyBean;
import com.honda.galc.service.property.PropertyService;
import com.ibm.mq.MQC;
import com.ibm.mq.MQEnvironment;
import com.ibm.mq.MQException;
import com.ibm.mq.MQMessage;
import com.ibm.mq.MQPutMessageOptions;
import com.ibm.mq.MQQueue;
import com.ibm.mq.MQQueueManager;

public class MqDevice implements IMqDevice  {

	protected Logger logger;
	String qMngrStr = "";
	
	public MqDevice(Logger logger){
		this.logger=logger;
	}
	
	public void sendToMqQueue(Device device, DataContainer dc) {
		MQQueueManager queueManager = null;
		MQQueue queue = null;
		MQDevicePropertyBean propertyBean = PropertyService.getPropertyBean(MQDevicePropertyBean.class,device.getClientId());
		dc.put(DataContainerTag.DELIMITER, propertyBean.getDelimiter());
		try {
			queueManager = connectToMQSeries(device);
			queue = getQueue(queueManager, propertyBean.getQueueName());
			IMqMessageConvertor msgConvertor = (IMqMessageConvertor) ApplicationContextProvider.getBean(propertyBean.getMessageConvertor());
			String sendData = prepareSendData(dc, msgConvertor);
			MQMessage mqMessage = this.createMQMessage(propertyBean.getMessageId(),sendData);
			if(!StringUtils.isEmpty(propertyBean.getMessageType())) {
					mqMessage.setStringProperty("MESSAGE_TYPE", propertyBean.getMessageType());
					logger.info("Setting MQ MESSAGE_TYPE " , propertyBean.getMessageType() );
			}
			
			queue.put(mqMessage, new MQPutMessageOptions());
			logger.info("sent data to MQ device " + device + ": "+sendData);
		} catch (Exception ex) {
			DataContainerUtil.error(logger, dc,"Could not send data to MQ device " + device +" due to: "+ex);
		} finally {
			finalize(queueManager, queue);
		}
	}
	
	public void sendToMqQueue(Device device, String data) {
		MQQueueManager queueManager = null;
		MQQueue queue = null;
		MQDevicePropertyBean propertyBean = PropertyService.getPropertyBean(MQDevicePropertyBean.class,device.getClientId());
		try {
			queueManager = connectToMQSeries(device);
			queue = getQueue(queueManager, propertyBean.getQueueName());
			MQMessage mqMessage = this.createMQMessage(propertyBean.getMessageId(),data);
			if(!StringUtils.isEmpty(propertyBean.getMessageType())) {
					mqMessage.setStringProperty("MESSAGE_TYPE", propertyBean.getMessageType());
					logger.info("Setting MQ MESSAGE_TYPE " , propertyBean.getMessageType() );
			}
			
			queue.put(mqMessage, new MQPutMessageOptions());
			logger.info("sent data to MQ device " + device + ": "+ data);
		} catch (Exception ex) {
			logger.error(ex, "Could not send data to MQ device " + device +" data: "+ data);
		} finally {
			finalize(queueManager, queue);
		}
	}

	/**
	 * Connect to a MQ Manager
	 * 
	 * @throws MQException
	 *             if any MQ exception raises while connecting to the MQ Server
	 */
	public MQQueueManager connectToMQSeries(Device device) throws MQException {
		MQDevicePropertyBean propertyBean = PropertyService.getPropertyBean(MQDevicePropertyBean.class,device.getClientId());
		//connect to MQSeries
		MQEnvironment.hostname = propertyBean.getHostName();
		MQEnvironment.port = propertyBean.getPort();
		MQEnvironment.channel = propertyBean.getChannelName();
		if(!StringUtils.isEmpty(propertyBean.getUserId())) {
			MQEnvironment.userID = propertyBean.getUserId();
			MQEnvironment.password = propertyBean.getPassword();
		}
         //set transport properties.
        MQEnvironment.properties.put(MQC.TRANSPORT_PROPERTY, MQC.TRANSPORT_MQSERIES_CLIENT);
        qMngrStr=propertyBean.getQueueManagerName();
        return new MQQueueManager(qMngrStr);
	}

	/**
	 * returns message queue
	 * 
	 * @param queueManager
	 * @param queueName
	 * @return
	 * @throws MQException
	 */
	public MQQueue getQueue(MQQueueManager queueManager, String queueName)
			throws MQException {
		int openOptions = MQC.MQOO_OUTPUT;
		return queueManager.accessQueue(queueName, openOptions);
	}

	protected MQMessage createMQMessage(String messageId,String message) throws IOException {
		MQMessage mqMessage = createMqMessage(message);
		if(!StringUtils.isEmpty(messageId)) {
			mqMessage.messageId = messageId.getBytes();
		}
		return mqMessage;
	}
	
	protected MQMessage createMqMessage(String message) throws IOException {
		MQMessage mqMessage = new MQMessage();
		mqMessage.clearMessage();
		mqMessage.format = MQC.MQFMT_STRING;
		mqMessage.writeString(message);
		return mqMessage;
	}
	
	protected String prepareSendData(DataContainer dc, IMqMessageConvertor conv) {
		String sendData = "";
		try{
			sendData = conv.convert(dc);
		}catch(Exception ex){
			logger.error("Unable to convert to String due to " + ex);
		}
		return sendData;
	}
	
	/**
	 * finalize queue and the queue manager
	 * 
	 * @param queueManager
	 * @param queue
	 */
	protected void finalize(MQQueueManager queueManager, MQQueue queue) {
		try {
			if (queue != null) queue.close();
			if (queueManager != null && queueManager.isConnected())
					queueManager.disconnect();
		} catch (Exception ex) {
		}
		queue = null;
		queueManager = null;
	}
}
