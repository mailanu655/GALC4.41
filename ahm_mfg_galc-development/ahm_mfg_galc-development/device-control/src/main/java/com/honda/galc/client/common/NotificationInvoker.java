package com.honda.galc.client.common;

import java.io.InputStream;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DataContainerXMLUtil;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.device.DevicePoint;
import com.honda.galc.net.SocketClient;


/**
 * 
 * <h3>NotificationInvoker</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> NotificationInvoker description </p>
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
 * @author Paul Chou
 * Nov 5, 2010
 *
 */

public class NotificationInvoker {
	Logger logger;
	public NotificationInvoker(Logger logger) {
		this.logger = logger;
	}

	public void send(String host, int port, DataContainer dc){
		try {
			SocketClient socketClient = new SocketClient(host, port, 1000);
			try {
				DataContainerXMLUtil.convertToXML(dc, socketClient.getOutputStream());
				
				socketClient.close();
				logger.info("succeeded to send " + dc);
			} catch (Exception e) {
				logger.error(e, "send exception.");
				if (socketClient != null)
					socketClient.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e, "send exception.");
		}
	}
	
	public DataContainer syncSend(String host, int port, DataContainer dc){
		try {
			SocketClient socketClient = new SocketClient(host, port, 1000);
			try {
				DataContainerXMLUtil.convertToXML(dc, socketClient.getOutputStream());
				socketClient.getOutputStream().flush();
				
				InputStream inputStream = socketClient.getInputStream();
				DataContainer returnDc = DataContainerXMLUtil.readDeviceDataFromXML(inputStream);
				socketClient.close();
				
				return returnDc;
			} catch (Exception e) {
				logger.error(e, "syncSend exception.");
				e.printStackTrace();
				if (socketClient != null)
					socketClient.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e, "syncSend exception.");
		}
		return null;
	}

	public DataContainer syncSend(String address, int port, DevicePoint plcData) {
		DataContainer dc = new DefaultDataContainer();
		dc.put(plcData.getName(), plcData.getValue());
		return syncSend(address, port, dc);
	}

}
