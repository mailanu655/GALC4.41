package com.honda.galc.service.mq;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.data.ApplicationContextProvider;
import com.honda.galc.data.DataContainer;
import com.honda.galc.entity.conf.Device;
import com.honda.galc.property.MQDevicePropertyBean;
import com.honda.galc.service.property.PropertyService;
import com.ibm.mq.MQPutMessageOptions;
import com.ibm.mq.MQQueue;
import com.ibm.mq.MQQueueManager;
/**
 * 
 * <h3>MqStringMessageDevice Class description</h3>
 * <p>
 * MqStringMessageDevice description
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
 * @author Haohua Xie<br>
 *         Feb 25, 2014
 * 
 * 
 */
public class MqStringMessageDevice extends MqDevice {

	public MqStringMessageDevice(Logger logger) {
		super(logger);
	}

	protected String prepareSendData(DataContainer dc,
			MQDevicePropertyBean propertyBean) throws Exception {
		IMqMessageConvertor conv = (IMqMessageConvertor) ApplicationContextProvider
				.getBean(propertyBean.getMessageConvertor());
		return conv.convert(dc);
	}

	@Override
	public void sendToMqQueue(Device device, DataContainer dc) {
		MQQueueManager queueManager = null;
		MQQueue queue = null;
		MQDevicePropertyBean propertyBean = PropertyService.getPropertyBean(
				MQDevicePropertyBean.class, device.getClientId());
		
		try {
			queueManager = connectToMQSeries(device);
			queue = getQueue(queueManager, propertyBean.getQueueName());
			String sendData = prepareSendData(dc, propertyBean);
			queue.put(createMqMessage(sendData), new MQPutMessageOptions());
			logger.info("sent data to MQ device " + device + ": " + dc);
		} catch (Exception ex) {
			logger.error("Could not send data to MQ device " + device);
		} finally {
			finalize(queueManager, queue);
		}
	}
}
