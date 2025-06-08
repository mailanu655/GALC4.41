package com.honda.galc.client.jasper;

import java.io.IOException;
import java.util.HashMap;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.device.property.JasperDevicePropertyBean;
import com.honda.galc.dao.conf.DeviceDao;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DataContainerTag;
import com.honda.galc.device.events.IPrintDeviceListener;
import com.honda.galc.device.printer.AbstractPrintDevice;
import com.honda.galc.entity.conf.Device;
import com.honda.galc.property.DevicePropertyBean;
import com.honda.galc.service.PrintingService;
import com.honda.galc.service.ServiceFactory;
import com.ibm.mq.MQC;
import com.ibm.mq.MQEnvironment;
import com.ibm.mq.MQException;
import com.ibm.mq.MQMessage;
import com.ibm.mq.MQPutMessageOptions;
import com.ibm.mq.MQQueue;
import com.ibm.mq.MQQueueManager;

import static com.honda.galc.service.ServiceFactory.getService;

public class JasperDevice extends AbstractPrintDevice {
	private String _channelName = "";
	private String _queueManagerName = "";
	public static final char STX = '\u0002';
	public static final char ETX = '\u0003';
	private String printerName = "";
	private String formId = "";
	private String templateName = "";
	
	@Override
	public void setDeviceProperty(DevicePropertyBean propertyBean) {
		JasperDevicePropertyBean property = (JasperDevicePropertyBean) propertyBean;
		super.setDeviceProperty(propertyBean);
		setName(property.getPrinterName());
		setChannelName(property.getChannel());
		setQueueManagerName(property.getQueueManager());
		setConnected(true);
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
		if(dc.get(DataContainerTag.PRINTER_NAME)==null){
			getLogger().info("PrinterName not found in configuration");
			return null;
		}
		printerName = dc.get(DataContainerTag.PRINTER_NAME).toString();
		formId = dc.get(DataContainerTag.FORM_ID).toString();
		templateName = dc.get(DataContainerTag.TEMPLATE_NAME).toString();
		byte[] printData = null;
		try{
			printData = getService(PrintingService.class).generateJasperReport(dc);
		} catch(Exception ex){
			ex.printStackTrace();
		}
		if(printData== null)
			return null;
		return new String(printData);
	}

	public MQQueue getQueue(MQQueueManager queueManager, String queueName)
			throws MQException {
		return queueManager.accessQueue(queueName, MQC.MQOO_OUTPUT, null, null,
				null);
	}

	private MQMessage createMqMessage(String message) throws IOException {
		MQMessage mqMessage = new MQMessage();
		mqMessage.clearMessage();
		mqMessage.format = MQC.MQFMT_STRING;
		mqMessage.writeString(message);
		return mqMessage;
	}

	public String getETX() {
		return Character.toString(ETX);
	}

	public String getSTX() {
		return Character.toString(STX);
	}

	public boolean print(String printData, int printQuantity, String productId) {
		MQQueueManager queueManager = null;
		MQQueue queue = null;

		try {
			if(StringUtils.isEmpty(printerName)){
				getLogger().info("Printer name is not configured");
				return false;
			}
			Device device = getDevice(getDestinationPrinter());

			if (device == null) {
				getLogger().info(
						"Device " + getDestinationPrinter() + " not found in Device table");
				return false;
			}
			queueManager = connectToMQSeries(device);
			queue = getQueue(queueManager, device.getClientId().toUpperCase());

			String sendData = prepareSendData(getDestinationPrinter(), printerName,
					productId, formId, printData.getBytes(), templateName);

			MQMessage msg = createMqMessage(sendData);
			if (printQuantity == 0) {
				getLogger().info(
						"print quantity is zero.so no need to print this");
				return true;
			} else {
				for (int i = 1; i <= printQuantity; i++) {

					queue.put(msg, new MQPutMessageOptions());
					getLogger().info("sent print " + i + " of " + printQuantity + " to MQ device " + device.getClientId());
				} 
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			getLogger().info("Could not print data: " + ex.getMessage());
			return false;

		} finally {
			finalize(queueManager, queue);
		}
		return true;
	}

	public MQQueueManager connectToMQSeries(Device tray) throws MQException {
		// connect to MQSeries
		MQEnvironment.hostname = tray.getEifIpAddress();
		MQEnvironment.port = tray.getEifPort();
		MQEnvironment.channel = getChannelName();
		return new MQQueueManager(getQueueManagerName());
	}

	private String prepareSendData(String printQueue, String printerName,
			String productId, String partName, byte[] printData,
			String templateId) {
		String sendData = "";
		sendData = "PRINT_QUEUE" + getSTX() + printQueue + getETX()
				+ "PRINTER_NAME" + getSTX() + printerName + getETX()
				+ "PRODUCT_ID" + getSTX() + productId + getETX() + "PART_NAME"
				+ getSTX() + partName + getETX() + "STR_PS_FILE" + getSTX()
				+ new String(printData) + getETX() + "TEMPLATE_ID" + getSTX()
				+ templateId + getETX();
		getLogger().info("Printing msg for mq" + sendData);
		return sendData;
	}

	private Device getDevice(String trayData) {
		Device device = null;
		try {
			device = ServiceFactory.getDao(DeviceDao.class).findByKey(trayData);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return device;
	}

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

}
