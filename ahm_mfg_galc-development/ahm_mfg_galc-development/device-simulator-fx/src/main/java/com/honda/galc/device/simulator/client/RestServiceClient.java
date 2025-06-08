package com.honda.galc.device.simulator.client;

import java.net.HttpURLConnection;

import com.honda.galc.net.HttpServiceClient;

public class RestServiceClient extends HttpServiceClient{
	protected int responseCode = HttpURLConnection.HTTP_CREATED;
	public RestServiceClient(String url) {
		super(url);
	}

	@Override
	public int getResponseCode() {
		return responseCode;
	}
	
}
