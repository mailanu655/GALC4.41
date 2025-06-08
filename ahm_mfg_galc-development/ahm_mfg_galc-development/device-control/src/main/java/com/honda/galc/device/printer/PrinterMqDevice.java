package com.honda.galc.device.printer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.device.property.MqprinterDevicePropertyBean;
import com.honda.galc.dao.conf.DeviceDao;
import com.honda.galc.data.DataContainer;
import com.honda.galc.device.events.IPrintDeviceListener;
import com.honda.galc.entity.conf.Device;
import com.honda.galc.property.DevicePropertyBean;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.printing.PrintAttributeConvertor;
import com.ibm.mq.MQC;
import com.ibm.mq.MQEnvironment;
import com.ibm.mq.MQException;
import com.ibm.mq.MQMessage;
import com.ibm.mq.MQPutMessageOptions;
import com.ibm.mq.MQQueue;
import com.ibm.mq.MQQueueManager;

/**
 * @author Ryan Koors Jan 23, 2012
 */
public class PrinterMqDevice extends AbstractPrintDevice {

	public static final char STX = '\u0002';
	public static final char ETX = '\u0003';

	private String _channelName = "";
	private String _queueManagerName = "";
	
	/**
	 * prepares text to print
	 * 
	 * @param textToPrint
	 * @param trayData
	 * @param device
	 * @return
	 */
	private String prepareSendData(String textToPrint, String trayData,
			Device device) {
		StringBuilder sendData = new StringBuilder(textToPrint);
		sendData.replace(textToPrint.lastIndexOf(trayData), textToPrint
				.lastIndexOf(trayData) + 1, device.getClientId().toUpperCase());
		return sendData.toString();
	}

	/**
	 * creates a new MQ message
	 * 
	 * @param message
	 * @return
	 * @throws IOException
	 */
	private MQMessage createMqMessage(String message) throws IOException {
		MQMessage mqMessage = new MQMessage();
		mqMessage.clearMessage();
		mqMessage.format = MQC.MQFMT_STRING;
		mqMessage.writeString(message);
		return mqMessage;
	}

	/**
	 * validate tray data
	 * 
	 * @param trayData
	 * @return
	 */
	private boolean validateTrayData(String trayData) {
		if (StringUtils.isBlank(trayData)) {
			getLogger().warn("Tray not found");
			return false;
		}

		if (trayData.contains("@")) {
			getLogger().warn(
					"PrintAttributeFormat not found: " + trayData.trim());
			return false;
		}
		return true;
	}

	/**
	 * get printer device using destinationPrinter and trayData
	 * 
	 * @param trayData
	 * @return
	 */
	private Device getDevice(String trayData) {
		Device device = null;
		Iterator<Entry<String, Device>> it = getAvailablePrinters().entrySet()
				.iterator();
		while (it.hasNext()) {
			Entry<String, Device> pairs = it.next();
			if ((getDestinationPrinter().trim()).equals(pairs.getKey()
					.toString().trim())) {
				device = ServiceFactory.getDao(DeviceDao.class).findByKey(
						getDestinationPrinter() + trayData.trim());
				break;
			}
		}
		return device;
	}

	/**
	 * Connect to a MQ Manager
	 * 
	 * @throws MQException
	 *             if any MQ exception raises while connecting to the MQ Server
	 */
	public MQQueueManager connectToMQSeries(Device tray) throws MQException {
		// connect to MQSeries
		MQEnvironment.hostname = tray.getEifIpAddress();
		MQEnvironment.port = tray.getEifPort();
		MQEnvironment.channel = getChannelName();
		return new MQQueueManager(getQueueManagerName());
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

	/**
	 * finalize queue and the queue manager
	 * 
	 * @param queueManager
	 * @param queue
	 */
	private void finalize(MQQueueManager queueManager, MQQueue queue) {
		try {
			if (queue != null) {
				queue.close();
			}
		} catch (Exception ex) {
		}

		try {
			if (queueManager != null) {
				if (queueManager.isConnected())
					queueManager.disconnect();
			}
		} catch (Exception ex) {
		}

		queue = null;
		queueManager = null;
	}

	/**
	 * set printer device properties
	 */
	@Override
	public void setDeviceProperty(DevicePropertyBean propertyBean) {
		MqprinterDevicePropertyBean property = (MqprinterDevicePropertyBean) propertyBean;
		super.setDeviceProperty(propertyBean);
		setName(property.getPrinterName());
		setChannelName(property.getChannel());
		setQueueManagerName(property.getQueueManager());
		setConnected(true);
	}

	public String getETX() {
		return Character.toString(ETX);
	}

	public String getSTX() {
		return Character.toString(STX);
	}

	public String getChannelName() {
		return _channelName;
	}

	private void setChannelName(String channelName) {
		_channelName = channelName;
	}

	public String getQueueManagerName() {
		return _queueManagerName;
	}

	private void setQueueManagerName(String queueManagerName) {
		_queueManagerName = queueManagerName;
	}

	public HashMap<String, IPrintDeviceListener> getListeners() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean registerListener(String applicationId,
			IPrintDeviceListener listener) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean requestControl(String applicationId,
			IPrintDeviceListener listener) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean unregisterListener(String applicationId,
			IPrintDeviceListener listener) {
		// TODO Auto-generated method stub
		return false;
	}

	public String getprintData(DataContainer dc) {
		PrintAttributeConvertor printAttributeConvertor = new PrintAttributeConvertor(getLogger());
		return printAttributeConvertor.getPrintData(dc);
	}

	/**
	 * send print data to printer via MQSeries implements abstract method
	 * AbstractPrinterDevice.print()
	 */
	public boolean print(String printData, int printQuantity, String productId) {
		MQQueueManager queueManager = null;
		MQQueue queue = null;

		try {
			String trayData = StringUtils.substring(printData, printData
					.lastIndexOf(getSTX()) + 1);

			if (!validateTrayData(trayData))
				return false;

			if (getAvailablePrinters().size() <= 0) {
				getLogger().warn("No Printer devices available to print");
				return false;
			}

			Device device = getDevice(trayData);
			if (device == null) {
				getLogger().warn(
						"Device " + getDestinationPrinter() + trayData.trim()
								+ " not found");
				return false;
			}

			queueManager = connectToMQSeries(device);
			queue = getQueue(queueManager, device.getClientId().toUpperCase());
			String sendData = prepareSendData(printData, trayData, device);
			MQMessage msg = createMqMessage(sendData);
			
			if(printQuantity == 0){
				getLogger().info("No need to print due to Print Quantity is zero");
				return true;
			}
			for (int i = 1; i <= printQuantity; i++) {
				queue.put(msg, new MQPutMessageOptions());
				getLogger().info("sent print " + i + " of " + printQuantity + " to MQ device " + device.getClientId());
			} 
			
		} catch (Exception ex) {
			getLogger().warn("Could not print data: " + ex.getMessage());
			return false;
		} finally {
			finalize(queueManager, queue);
		}
		return true;
	
	}

	
}

	
