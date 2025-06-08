/**
 * 
 */
package com.honda.galc.web.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Subu Kathiresan
 * @Date April 03, 2012
 *
 */
public interface IWebService {
	
	public void setRequest(HttpServletRequest request);
	
	public HttpServletResponse getResponse();
}