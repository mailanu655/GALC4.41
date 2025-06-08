package com.honda.galc.rest.common;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import com.honda.galc.common.logging.Logger;

/**
 * @author Subu Kathiresan
 * @date Mar 17, 2015
 */
public abstract class AbstractContentHandler implements IContentHandler {

	protected String clientAddress = "";
	protected Logger logger = null;
	
	abstract public ArrayList<Object> getParametersFromRequestBody(HttpServletRequest httpRequest, String requestSignature);

	abstract public String getResponse(String requestSignature, Object obj);

	public String getClientAddress() {
		return clientAddress;
	}

	public void setClientAddress(String clientAddress) {
		this.clientAddress = clientAddress;
	}

	public Logger getLogger() {
		return logger;
	}

	public void setLogger(Logger logger) {
		this.logger = logger;
	}
}
