package com.honda.galc.checkers;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.net.HttpURLConnection;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.dao.conf.DeviceDao;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DataContainerJSONUtil;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.device.dataformat.InputData;
import com.honda.galc.entity.conf.Device;
import com.honda.galc.net.HttpClient;

public abstract class RestServiceChecker<T extends InputData> extends AbstractBaseChecker<T> {
	protected Device device;

	/**
	 * Sets the Device for this RestServiceChecker.
	 * @param clientId - Client ID used to get the Device.
	 * @return true iff the Device is successfully set.
	 */
	protected boolean setDevice(String clientId) {
		this.device = null;
		if (StringUtils.isBlank(clientId)) return false;
		try {
			DeviceDao deviceDao = getDao(DeviceDao.class);
			this.device = deviceDao.findByKey(clientId);
		} catch (Throwable t) {
			t.printStackTrace();
			return false;
		}
		return (this.device != null);
	}

	/**
	 * Returns a version of the given DataContainer which has been
	 * populated with values from the corresponding device data format.
	 */
	protected DataContainer makePopulatedDataContainer(DataContainer data) {
		if(this.device == null) return null;
		this.device.populate(data);
		/* Merge the given DataContainer with the device's DataContainer,
		 * prioritizing the device's DataContainer to ensure STATIC tag values are used.
		 * This merge is necessary to ensure that fields which are in the given DataContainer,
		 * but not in the Device data format, are included in the returned DataContainer. */
		return mergeDataContainers(this.device.toDataContainer(), data);
	}

	/**
	 * Returns a version of the given DataContainer which has been
	 * populated with values from the corresponding reply device data format.
	 */
	protected DataContainer makePopulatedReplyDataContainer(DataContainer data) {
		if(this.device == null) return null;
		this.device.populateReply(data);
		/* Merge the given DataContainer with the device's DataContainer,
		 * prioritizing the device's DataContainer to ensure STATIC tag values are used.
		 * This merge is necessary to ensure that fields which are in the given DataContainer,
		 * but not in the reply Device data format, are included in the returned DataContainer. */
		return mergeDataContainers(this.device.toReplyDataContainer(false), data);
	}

	/**
	 * Returns a DataContainer which contains all key-value pairs from highPriorityData
	 * and contains key-value pairs from lowPriorityData where the key did not exist
	 * in highPriorityData.
	 */
	protected DataContainer mergeDataContainers(DataContainer highPriorityData, DataContainer lowPriorityData) {
		DataContainer data = new DefaultDataContainer();
		// add all key-value pairs from highPriorityData
		if (highPriorityData != null) data.putAll(highPriorityData);
		// add all non-present key-value pairs from lowPriorityData
		if (lowPriorityData != null) {
			for (Object key : lowPriorityData.keySet()) {
				if (!data.containsKey(key)) data.put(key, lowPriorityData.get(key));
			}
		}
		return data;
	}

	/**
	 * Sends the given DataContainer to the given REST service using the GET method.
	 * @param data - DataContainer to be sent
	 * @param url - URL of the REST service
	 * @return - DataContainer representing the response
	 */
	protected DataContainer getRestRequest(DataContainer data, String url) {
		return getRestRequest(DataContainerJSONUtil.convertToRawJSON(data), url);
	}

	/**
	 * Sends the given JSON string to the given REST service using the GET method.
	 * @param json - JSON string to be sent
	 * @param url - URL of the REST service
	 * @return - DataContainer representing the response
	 */
	protected DataContainer getRestRequest(String json, String url) {
		try {
			String getUrl = url.concat(json);
			String jsonResponse = HttpClient.get(getUrl, HttpURLConnection.HTTP_OK);
			if (StringUtils.isBlank(jsonResponse)) return null;
			return makePopulatedReplyDataContainer(DataContainerJSONUtil.convertFromJSON(null, jsonResponse));
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * Sends the given DataContainer to the given REST service using the POST method.
	 * @param data - DataContainer to be sent
	 * @param url - URL of the REST service
	 * @return - DataContainer representing the response
	 */
	protected DataContainer postRestRequest(DataContainer data, String url) {
		return postRestRequest(DataContainerJSONUtil.convertToRawJSON(data), url);
	}

	/**
	 * Sends the given JSON string to the given REST service using the POST method.
	 * @param json - JSON string to be sent
	 * @param url - URL of the REST service
	 * @return - DataContainer representing the response
	 */
	protected DataContainer postRestRequest(String json, String url) {
		try {
			String jsonResponse = HttpClient.post(url, json, HttpURLConnection.HTTP_CREATED);
			if (StringUtils.isBlank(jsonResponse)) return null;
			return makePopulatedReplyDataContainer(DataContainerJSONUtil.convertFromJSON(null, jsonResponse));
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
}
