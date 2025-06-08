package com.honda.galc.device.printer;

import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import com.honda.galc.data.DataContainer;
import com.honda.galc.device.events.IPrintDeviceListener;
import com.honda.galc.entity.conf.Device;
import com.honda.galc.net.TCPSocketFactory;
import com.honda.galc.property.DevicePropertyBean;
import com.honda.galc.service.printing.PrintAttributeConvertor;


/**
 * @author Subu Kathiresan
 * @date Sep 26, 2011
 */
public class PrinterSocketDevice extends AbstractPrintDevice {

	public Integer _connTimeout = 2000; // default timeout is 2 secs

	public PrinterSocketDevice() {
	}

	/**
	 * get printer device using destinationPrinter
	 * 
	 * @return
	 */
	private Device getDevice() {
		Device device = null;
		Iterator<Entry<String, Device>> it = getAvailablePrinters().entrySet()
				.iterator();
		while (it.hasNext()) {
			Entry<String, Device> pairs = it.next();
			if (getDestinationPrinter().trim().equals(
					pairs.getKey().toString().trim())) {
				device = pairs.getValue();
				break;
			}
		}
		return device;
	}

	/**
	 * finalize the socket and the output stream
	 * 
	 * @param socket
	 */
	private void finalize(Socket socket) {
		if (socket != null) {
			try {
				socket.getOutputStream().flush();
			} catch (Exception ex) {
			}

			try {
				socket.getOutputStream().close();
			} catch (Exception ex) {
			}

			try {
				socket.close();
			} catch (Exception ex) {
			}
		}

		socket = null;
	}

	@Override
	public void setDeviceProperty(DevicePropertyBean propertyBean) {
		super.setDeviceProperty(propertyBean);
		setConnected(true);
	}

	public Integer getConnectionTimeout() {
		return _connTimeout;
	}

	public void setConnectionTimeout(Integer connTimeout) {
		_connTimeout = connTimeout;
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
	 * send print data over TCP/IP socket. Return true if successful, false
	 * otherwise
	 */
	public boolean print(String printData, int printQuantity, String productId) {
		Socket socket = null;
		try {
			if (getAvailablePrinters().size() <= 0) {
				getLogger().warn("No Printer devices available to print");
				return false;
			}

			Device device = getDevice();
			if (device == null) {
				getLogger().warn(
						"Device " + getDestinationPrinter().trim()
								+ " not found");
				return false;
			}

			setHostName(device.getEifIpAddress());
			setPort(device.getEifPort());
			socket = TCPSocketFactory.getSocket(getHostName(), getPort(),
					getConnectionTimeout());

			getLogger().debug(
					"Socket Connection to printer " + getHostName()
							+ " created successfully. Printing..\n "
							+ printData);
			if (printQuantity == 0) {
				getLogger().info(
						"No need to print due to Print Quantity is zero");
				return true;
			}
			for (int i = 1; i <= printQuantity; i++) {

				socket.getOutputStream().write(printData.getBytes());
				getLogger().info(
						"sent print " + i + " of " + printQuantity
								+ " to Socket device " + device.getClientId());
			}

		} catch (Exception ex) {
			getLogger().warn(
					"Could not print data to " + getHostName() + ":"
							+ getPort());
			return false;
		} finally {
			finalize(socket);
		}
		return true;

	}

}
