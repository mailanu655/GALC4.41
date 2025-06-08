package com.honda.galc.service.printing;

import static com.honda.galc.common.logging.Logger.getLogger;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DataContainerTag;
import com.honda.galc.data.DataContainerUtil;
import com.honda.galc.entity.conf.Device;
import com.honda.galc.util.ToStringUtil;
import com.ibm.mq.MQMessage;
import com.ibm.mq.MQPutMessageOptions;
import com.ibm.mq.MQQueue;
import com.ibm.mq.MQQueueManager;

/**
 * @author Subu Kathiresan
 * @date Sep 11, 2016
 */
public class MqJasperPrintDevice extends MQPrintDevice {

	public MqJasperPrintDevice(Logger logger) {
		super(logger);
	}

	public void print(byte[] printData, Device device, DataContainer dc) {
		dc.put("PRINTER_NAME", device.getClientId());
		sendToMqQueue(device, dc);
	}

	private void sendToMqQueue(Device device, DataContainer dc) {
		MQQueueManager queueManager = null;
		MQQueue queue = null;

		try {
			queueManager = connectToMQSeries(device);
			getLogger().debug("successfully connected to MQ");
			queue = getQueue(queueManager, device.getClientId().toUpperCase());

			MQMessage msg = createMqMessage(ToStringUtil.serialize(dc));
			String printQuantity = DataContainerUtil.getString(dc, DataContainerTag.PRINT_QUANTITY, "1");

			if (Integer.parseInt(printQuantity) == 0) {
				getLogger().info("Print quantity is zero. Nothing to print.");
			} else {
				for (int i = 1; i <= Integer.parseInt(printQuantity); i++) {
					queue.put(msg, new MQPutMessageOptions());
					getLogger().info("sent print request " + i + " to MQ");
				}
			}
		} catch (Exception ex) {
			DataContainerUtil.error(getLogger(), dc,"Could not send print request to MQ");
		} finally {
			finalize(queueManager, queue);
		}
	}
}
