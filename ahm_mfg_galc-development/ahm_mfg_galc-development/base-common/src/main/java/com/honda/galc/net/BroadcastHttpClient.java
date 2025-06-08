package com.honda.galc.net;

import java.net.HttpURLConnection;

public class BroadcastHttpClient extends HttpServiceClient {
	protected int responseCode = HttpURLConnection.HTTP_OK;	
	public BroadcastHttpClient(String url) {
		super(url);
	}
	
	public BroadcastHttpClient(String url, int httpConnTimeout, int httpReadTimeout) {
		super(url, httpConnTimeout, httpReadTimeout);
	}

	@Override
	public int getResponseCode() {
		return responseCode;
	}
	
	public String send(String url, String jsonString){
		try {
			String jsonStr = HttpClient.post(url, jsonString, getResponseCode(), getHttpConnTimeout(), getHttpReadTimeout());
			return jsonStr;
		} catch (Exception e) {
			log.error("Exception to invoke Rest Service." + e.toString());
		}
		return null;
	}	
}
