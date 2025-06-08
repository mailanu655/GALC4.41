package com.honda.galc.web.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Subu Kathiresan
 * @date Mar 28, 2013 
 * 
 */
public abstract class BaseWebService implements IWebService {

	private HttpServletRequest _request;
	private HttpServletResponse _response;
	
	public HttpServletRequest getRequest() {
		return _request;
	}
	
	public void setRequest(HttpServletRequest request) {
		_request = request;
	}
		
	public HttpServletResponse getResponse() {
		return _response;
	}
	
	public void setResponse(HttpServletResponse response) {
		_response = response;
	}
	
	public String getClientAddress() {
        return getRequest().getRemoteHost() + "/" + getRequest().getRemoteAddr();
	}
}
