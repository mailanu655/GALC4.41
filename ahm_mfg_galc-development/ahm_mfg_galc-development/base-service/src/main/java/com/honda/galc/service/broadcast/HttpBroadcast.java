package com.honda.galc.service.broadcast;

import java.net.HttpURLConnection;
import java.util.Map;

import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DataContainerTag;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.entity.conf.BroadcastDestination;
import com.honda.galc.entity.conf.Device;
import com.honda.galc.net.HttpClient;

public class HttpBroadcast extends AbstractDeviceBroadcast {
	String url = null;
	String json = null;
	boolean asString = false;

	public HttpBroadcast(BroadcastDestination destination, String processPointId, DataContainer dc) {
		super(destination, processPointId, dc);
	}

	@Override
	public void send(Device device, DataContainer dc) {
		try {
			createHttpClient(device);
			if (url != null) {
				String finalUrl = constructUrlWithParams(device, dc);
				json = HttpClient.get(finalUrl, HttpURLConnection.HTTP_OK);
				logger.info("Sent data to URL: " + finalUrl);
			} else {
				logger.error("Unable to connect to server");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error(ex, "Failed to send data " + dc.toString() + " to device " + device);
		}
	}

	@Override
	public DataContainer syncSend(Device device, DataContainer dc) {
		DataContainer returnDC = null;
		try {
			returnDC = new DefaultDataContainer();
			createHttpClient(device);
			if (url != null) {
				String finalUrl = constructUrlWithParams(device, dc);
				json = HttpClient.get(finalUrl, HttpURLConnection.HTTP_OK);
				logger.info("Sent data to URL: " + finalUrl);
			} else {
				logger.error("Unable to connect to server");
			}
			if (returnDC == null) {
				logger.error("Failed to receive data");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error(ex, "Failed to send data " + dc.toString() + " to device " + device);
		}
		return returnDC;
	}

	private void createHttpClient(Device device) {
		url = device.getEifIpAddress().toString();
		if (url == null || url.length() == 0) {
			logger.error("Empty host name, CID [" + url + "]");
		}
	}

	private String constructUrlWithParams(Device device, DataContainer dc) {
		StringBuilder finalUrl = new StringBuilder(url);
        dc.remove(DataContainerTag.CLIENT_ID);
        
		finalUrl.append("?");
		boolean isFirstParam = true;

		for (Map.Entry<Object, Object> entry : dc.entrySet()) {
			String key = entry.getKey().toString();
			String value = entry.getValue().toString();

			if (value == null || value.isEmpty()) {
				logger.warn("No value found in DataContainer for key: " + key);
				continue;
			}

			if (!isFirstParam) {
				finalUrl.append("&");
			}

			finalUrl.append(key).append("=").append(value);
			isFirstParam = false;
		}

		return finalUrl.toString();
	}
}
