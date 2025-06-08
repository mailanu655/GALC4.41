package com.honda.galc.service.printing;

import static com.honda.galc.common.logging.Logger.getLogger;

import java.io.IOException;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DataContainerTag;
import com.honda.galc.data.DataContainerUtil;
import com.honda.galc.entity.conf.Device;
import com.honda.galc.property.MQDevicePropertyBean;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.StringUtil;
import com.ibm.mq.MQC;
import com.ibm.mq.MQEnvironment;
import com.ibm.mq.MQException;
import com.ibm.mq.MQMessage;
import com.ibm.mq.MQPutMessageOptions;
import com.ibm.mq.MQQueue;
import com.ibm.mq.MQQueueManager;

/**
 * 
 * <h3>MQPrintDevice Class description</h3>
 * <p>
 * MQPrintDevice description
 * </p>
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
 *         Aug 26, 2013
 * 
 * 
 */
public class MQPrintDevice implements IPrinterDevice {

	public static final char STX = '\u0002';
	public static final char ETX = '\u0003';

	private Logger logger;

	public MQPrintDevice(Logger logger) {
		this.logger = logger;
	}

	public void print(byte[] printData, Device device, DataContainer dc) {
		sendToMqQueue(printData, device, dc);
	}

	protected void sendToMqQueue(byte[] printData, Device device, DataContainer dc) {
		MQQueueManager queueManager = null;
		MQQueue queue = null;

		try {
			queueManager = connectToMQSeries(device);
			logger.debug("successfully connected to mq");
			queue = getQueue(queueManager, device.getClientId().toUpperCase());

			String sendData = prepareSendData(printData, device, dc);
			MQMessage msg = createMqMessage(sendData);
			String printQuantity = DataContainerUtil.getString(dc,
					DataContainerTag.PRINT_QUANTITY, "1");

			if (Integer.parseInt(printQuantity) == 0) {
				getLogger().info("Print quantity is zero. Nothing to print. ");
			} else {

				for (int i = 1; i <= Integer.parseInt(printQuantity); i++) {
					queue.put(msg, new MQPutMessageOptions());
					logger.info("sent print data to MQ device to" + i
							+ "time" + device);
				}

			}
		} catch (Exception ex) {
			DataContainerUtil.error(logger, dc,"Could not send print data to MQ device ");			
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

		MQDevicePropertyBean propertyBean = PropertyService.getPropertyBean(
				MQDevicePropertyBean.class, device.getClientId());
		// connect to MQSeries
		MQEnvironment.hostname = propertyBean.getHostName();
		MQEnvironment.port = propertyBean.getPort();
		MQEnvironment.channel = propertyBean.getChannelName();
		return new MQQueueManager(propertyBean.getQueueManagerName());
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
		return queueManager.accessQueue(queueName, MQC.MQOO_OUTPUT, null, null,
				null);
	}

	protected MQMessage createMqMessage(String message) throws IOException {
		MQMessage mqMessage = new MQMessage();
		mqMessage.clearMessage();
		mqMessage.format = MQC.MQFMT_STRING;
		mqMessage.writeString(message);
		return mqMessage;
	}

	protected String prepareSendData(byte[] printData, Device device,
			DataContainer dc) {
		String strPrData = StringUtil.convertToString(printData);
		String sendData = "";
		sendData = "PRINT_QUEUE" + STX
				+ dc.getString(DataContainerTag.QUEUE_NAME) + ETX
				+ "PRINTER_NAME" + STX + device.getClientId() + ETX
				+ "PRODUCT_ID" + STX
				+ dc.getString(DataContainerTag.PRODUCT_ID) + ETX + "PART_NAME"
				+ STX + dc.getString(DataContainerTag.FORM_ID) + ETX
				+ "STR_PS_FILE" + STX + strPrData + ETX + "TEMPLATE_ID" + STX
				+ dc.getString(DataContainerTag.TEMPLATE_NAME) + ETX;

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
			if (queue != null)
				queue.close();
			if (queueManager != null && queueManager.isConnected())
				queueManager.disconnect();
		} catch (Exception ex) {
		}
		queue = null;
		queueManager = null;
	}

}
