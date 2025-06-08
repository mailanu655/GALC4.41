package com.honda.galc.net;


public class HttpEndpoint extends AbstractEndpoint{
	private String urlString;
	
	public HttpEndpoint() {
		this.urlString = HttpServiceProvider.url;
	}
	public String name() {
		return urlString;
	}

	public boolean connect() {
		HttpRequestInvoker requestInvoker= new HttpRequestInvoker(urlString);
		try{
			requestInvoker.initConnection();
			requestInvoker.invoke(null);
		}catch(Exception e) {
			return false;
		}
		return true;
	}
}
