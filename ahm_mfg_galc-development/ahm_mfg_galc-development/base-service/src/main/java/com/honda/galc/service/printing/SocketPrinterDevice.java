package com.honda.galc.service.printing;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DataContainerTag;
import com.honda.galc.data.DataContainerUtil;
import com.honda.galc.entity.conf.Device;
import com.honda.galc.net.TextSocketSender;

/**
 * 
 * <h3>SocketPrinterDevice Class description</h3>
 * <p>
 * SocketPrinterDevice description
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
 *         Aug 27, 2013
 * 
 * 
 */
public class SocketPrinterDevice implements IPrinterDevice {

	private Logger logger;

	public SocketPrinterDevice(Logger logger) {
		this.logger = logger;
	}

	public void print(byte[] printData, Device device, DataContainer dc) {
		try {
			logger.info("printData: "+ new String(printData));
			String printQuantity = DataContainerUtil.getString(dc,
					DataContainerTag.PRINT_QUANTITY, "1");
			if (StringUtils.isEmpty(printQuantity)) {
				logger.info("print quantity is null");
			} else {
				if (Integer.parseInt(printQuantity) == 0) {
					logger
							.info("print quantity is zero.so no need to print this.");
				} else {
					for (int i = 0; i < Integer.parseInt(printQuantity); i++) {

						TextSocketSender socket = new TextSocketSender(device
								.getEifIpAddress(), device.getEifPort());
						socket.send(new String(printData));
						logger.info("sent print data to socket printer device "
								+ device);
					}
				}
			}
		} catch (Exception ex) {
			DataContainerUtil.error(logger, dc, ex, "Cound not send print data to socket : " + device);
		}
	}
}
