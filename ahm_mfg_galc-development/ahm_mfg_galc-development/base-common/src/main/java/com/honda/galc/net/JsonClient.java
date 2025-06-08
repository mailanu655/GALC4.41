package com.honda.galc.net;


import java.io.IOException;
import com.honda.galc.common.exception.ServiceInvocationException;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DataContainerJSONUtil;
import com.honda.galc.data.DefaultDataContainer;

/**
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * A HTTP client for sending a JSON/HTML request. Used by Broadcast components.
 * <P>
 */
public class JsonClient {
	private boolean asString = false;
	private String url = null;
	private int httpConnTimeout;
	private int httpReadTimeout;

	public JsonClient(String url){
		this.url = url;
	}
	
	public JsonClient(String url, int httpConnTimeout, int httpReadTimeout){
		this.url = url;
		this.httpConnTimeout = httpConnTimeout;
		this.httpReadTimeout = httpReadTimeout;
	}

	public DataContainer syncSend(DataContainer dc, boolean asStrings)
	throws IOException
	{
		asString = asStrings;
		String jsonString = null;
		long start = System.currentTimeMillis();
		DataContainer result = new DefaultDataContainer();
		try {
			jsonString=DataContainerJSONUtil.convertToJSON(dc, asString);
			BroadcastHttpClient dwClient=new BroadcastHttpClient(url, httpConnTimeout, httpReadTimeout);
			String receiveString=dwClient.send(url, jsonString);
			result=receiveString == null ? null : DataContainerJSONUtil.convertFromJSON(result, receiveString.toString());
		} catch(Exception e) {
			throw new ServiceInvocationException("Failed to receive reply from "
					+ this.url + " due to " + e.toString(), e);
		}

		long end = System.currentTimeMillis();
		Logger.getLogger().debug((end-start), "Invoke " + dc + " completed");
		return result;
	}

	public void send(DataContainer dc, boolean asStrings) throws IOException {
		asString = asStrings;
		String jsonString=null;
		long start = System.currentTimeMillis();
		try {
			jsonString=DataContainerJSONUtil.convertToJSON(dc, asString);
			BroadcastHttpClient dwClient=new BroadcastHttpClient(url, httpConnTimeout, httpReadTimeout);
			dwClient.send(url, jsonString);
		} catch(Exception e) {
			throw new ServiceInvocationException("Failed to receive reply from "
					+ this.url + " due to " + e.toString(), e);
		}
		long end = System.currentTimeMillis();
		Logger.getLogger().debug((end-start), "Invoke " + dc + " completed");
	}

	public int getHttpConnTimeout() {
		return httpConnTimeout;
	}

	public void setHttpConnTimeout(int httpConnTimeout) {
		this.httpConnTimeout = httpConnTimeout;
	}

	public int getHttpReadTimeout() {
		return httpReadTimeout;
	}

	public void setHttpReadTimeout(int httpReadTimeout) {
		this.httpReadTimeout = httpReadTimeout;
	}
	
}

