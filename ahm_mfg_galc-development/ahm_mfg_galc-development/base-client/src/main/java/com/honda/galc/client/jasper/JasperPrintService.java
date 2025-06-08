package com.honda.galc.client.jasper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.MQApplicationContext;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.InstalledPartDao;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DataContainerTag;
import com.honda.galc.data.PrintRequest;
import com.honda.galc.entity.enumtype.InstalledPartStatus;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.InstalledPartId;
import com.honda.galc.entity.product.Template;
import com.honda.galc.property.PrinterQueueSenderPropertyBean;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.printing.JasperPrintUtil;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.ToStringUtil;
import com.ibm.mq.MQC;
import com.ibm.mq.MQEnvironment;
import com.ibm.mq.MQException;
import com.ibm.mq.MQMessage;
import com.ibm.mq.MQPutMessageOptions;
import com.ibm.mq.MQQueue;
import com.ibm.mq.MQQueueManager;

public class JasperPrintService implements MessageListener {

	public JasperPrintService() {
		getLogger().info("JasperPrintService Listener started");
	}

	public void onMessage(Message message) {
		String messageId = getMessageId(message);
		getLogger().info(messageId + " Listener received message");
		if (message instanceof TextMessage) {
			TextMessage textMessage = (TextMessage) message;
			try {
				final String msg = textMessage == null ? "" : textMessage.getText();
				final Message mqMessage = message;
				getLogger().debug(messageId + " Received text " + msg);
				handleMessage(msg, mqMessage);
			} catch (JMSException jex) {
				logError(jex, "JMS exception occured while processing message", messageId);
				sendToBackOutQueue(message);
			}catch (Exception ex) {
				logError(ex, "Exception occured while processing message", messageId);
				sendToBackOutQueue(message);
			}
		} else {
			sendToBackOutQueue(message);
			getLogger().error("Message " + messageId + " is not of type TextMessage");
		}
		getLogger().info(messageId + " Exiting onMessage()");
	}

	public void handleMessage(String msg, Message message) {
		String messageId = getMessageId(message);
		try {
			DataContainer dc = deserializeMessage(msg, message);
			JasperPrintUtil jasperUtil = new JasperPrintUtil(this.getClass().getSimpleName(), messageId);
			
			String templateName = dc.getString(DataContainerTag.TEMPLATE_NAME);
			PrinterQueueSenderPropertyBean propertyBean = PropertyService.getPropertyBean(PrinterQueueSenderPropertyBean.class, templateName);
			boolean reloadIfUpdated = propertyBean.isReloadTemplateIfUpdated();
			
			Template template = jasperUtil.getTemplate(templateName, reloadIfUpdated);
			JasperReport jasperReport = jasperUtil.getJasperReport(template);
			JasperPrint jasperPrint = jasperUtil.fillReport(dc, jasperReport, reloadIfUpdated);

			if(jasperPrint == null){
				getLogger().warn(messageId + " JasperPrint object is null");
				sendToBackOutQueue(message);
			}

			jasperUtil.print(jasperPrint, dc);
			getLogger().info(messageId + " " + dc.toString());
		} catch(Exception ex) {
			logError(ex, "Unable to print", messageId);
			sendToBackOutQueue(message);
		}
	}

	private DataContainer deserializeMessage(String msg, Message message) {
		DataContainer dc = null;
		try {
			dc = (DataContainer) ToStringUtil.deserialize(msg);
		} catch (Exception ex) {
			logError(ex, "Exception occured when attempting to handle message", getMessageId(message));
		}
		return dc;
	}

	private void sendToBackOutQueue(Message message) {
		getLogger().warn(getMessageId(message) + " PrintJob Failed "+ message);
		getLogger().warn("BackoutQueue Channel: " + MQApplicationContext.getInstance().getChannel());
		getLogger().warn(String.valueOf("Port: " + MQApplicationContext.getInstance().getPort()));
		getLogger().warn("QueueManager: " + MQApplicationContext.getInstance().getQueueManager());
		getLogger().warn("HostName: " + MQApplicationContext.getInstance().getHostName());
		getLogger().warn("terminalname: " + ApplicationContext.getInstance().getHostName());
		MQQueueManager queueManager = null;
		MQQueue queue = null;
		try {
			String backoutQueueName = PropertyService.getProperty(ApplicationContext.getInstance().getHostName(), "BackOutQueue");
			getLogger().debug("BackoutQueueName: " + backoutQueueName );
			if(!StringUtils.isEmpty(backoutQueueName)){
				queueManager = connectToMQSeries();
				queue = getQueue(queueManager, backoutQueueName);
				queue.put(createMqMessage(message.toString()), new MQPutMessageOptions());
			}
		} catch (Exception ex) {
			logError(ex, "Could not add message to backout queue", getMessageId(message));
		} finally{
			finalize(queueManager, queue);
		}
	}

	protected void finalize(MQQueueManager queueManager, MQQueue queue) {
		try {
			if (queue != null)
				queue.close();
			if (queueManager != null && queueManager.isConnected())
				queueManager.disconnect();
		} catch (Exception ex) {
		}
		queue = null;
		queueManager = null;
	}
	
	private MQMessage createMqMessage(String message) throws IOException {
		MQMessage mqMessage = new MQMessage();
		mqMessage.clearMessage();
		mqMessage.format = MQC.MQFMT_STRING;
		mqMessage.writeString(message);
		return mqMessage;
	}

	public MQQueueManager connectToMQSeries() throws MQException {
		// connect to MQBackout Queue
		MQEnvironment.hostname = MQApplicationContext.getInstance().getHostName();
		MQEnvironment.port = MQApplicationContext.getInstance().getPort();
		MQEnvironment.channel = MQApplicationContext.getInstance().getChannel();
		return new MQQueueManager(MQApplicationContext.getInstance().getQueueManager());
	}

	public MQQueue getQueue(MQQueueManager queueManager, String queueName) throws MQException {
		return queueManager.accessQueue(queueName, MQC.MQOO_OUTPUT, null, null,	null);
	}

	public boolean sendFileToPrinter(String postScriptData, String printerName, DataContainer dc, String messageId) {
		long startTime = System.currentTimeMillis();
		boolean check = false;
		List<String> status = new ArrayList<String>();
		PrintRequest printRequest = new PrintRequest();
		
		if(postScriptData == null){
			getLogger().warn(messageId + " Post script file is null");
			return check;
		}
		if(printerName == null){
			getLogger().warn(messageId + " PrinterName is null");
			return check;
		}
		
		getLogger().info(messageId + " Ready to print");
		status = printRequest.print(postScriptData.getBytes(), printerName.trim());
		getLogger().info(messageId + " " + StringUtils.trimToEmpty(dc.toString()));
		getLogger().info(messageId + " Print job executed");
		
		getLogger().info((System.currentTimeMillis() - startTime), messageId + " Sent to printer");

		if(status!= null && status.size()>0){
			if (status.contains("printDataTransferCompleted")) {
				getLogger().info(messageId + " Printed Successfully");
				check = true;
			}
		}
		return check;
	}

	public void updateInstalledPart(String productId, String partName, String messageId) {
		try {
			if (productId != null && partName != null) {
				InstalledPartId installedPartId = new InstalledPartId();
				installedPartId.setProductId(productId.trim());
				installedPartId.setPartName(partName.trim());
				InstalledPart installedPart = getInstalledPartDao().findByKey(installedPartId);
				if (installedPart != null) {
					installedPart.setInstalledPartStatus(InstalledPartStatus.PENDING);
					getInstalledPartDao().save(installedPart);
				}
			}
		} catch (Exception ex) {
			logError(ex, "Exception happened while updating InstalledPart", messageId);
		}
	}

	protected InstalledPartDao getInstalledPartDao() {
		InstalledPartDao installedPartDao = null;
		if (installedPartDao == null)
			installedPartDao = ServiceFactory.getDao(InstalledPartDao.class);

		return installedPartDao;
	}

	private Logger getLogger(){
		return Logger.getLogger(this.getClass().getSimpleName());
	}

	protected void logError(Exception ex, String logMessage, String messageId) {
		ex.printStackTrace();
		getLogger().error(ex, messageId + " " + logMessage + " - " + StringUtils.trimToEmpty(ex.getMessage()));
	}
	
	protected String getMessageId(Message message) {
		try {
			if (message.getJMSMessageID().length() > 3) {
				return StringUtils.trimToEmpty(message.getJMSMessageID().substring(3)); //remove "ID:"
			}
			return message.getJMSMessageID();
		} catch(Exception ex) {
			getLogger().error(ex, "Unable to retrieve message Id");
			return "";
		}
	}
}
