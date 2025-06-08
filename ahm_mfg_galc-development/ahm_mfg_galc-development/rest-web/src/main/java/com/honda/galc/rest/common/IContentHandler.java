package com.honda.galc.rest.common;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import com.honda.galc.common.logging.Logger;

/**
 * @author Subu Kathiresan
 * @date Mar 17, 2015
 */
public interface IContentHandler {

	public ArrayList<Object> getParametersFromRequestBody(HttpServletRequest httpRequest, String requestSignature);
	
	public String getResponse(String requestSignature, Object obj);
	
	public String getClientAddress();
	
	public void setClientAddress(String clientAddress);
	
	public Logger getLogger();
	
	public void setLogger(Logger logger);
	
}
