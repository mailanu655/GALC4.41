package com.honda.galc.net;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import com.honda.galc.net.Request;

/**
 * 
 * <h3>ClientDaoInvocationHandler</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> ClientDaoInvocationHandler description </p>
 * 
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Update by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 *
 * </TABLE>
 *   
 * @author Jeffray Huang
 * Oct 5, 2009
 *
 */
public class HttpServiceInvocationHandler implements InvocationHandler{
	
	private String targetClass;
	private String url;
	private String sessionCookieValue;
	
	public HttpServiceInvocationHandler(String url,String className) {
	    this.url = url;
		this.targetClass = className;
	}
	
	public HttpServiceInvocationHandler(String url,String className,String sessionCookieValue) {
	    this.url = url;
		this.targetClass = className;
		this.sessionCookieValue = sessionCookieValue;
	}
	
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		
		Object obj = new HttpRequestInvoker(url,sessionCookieValue).invoke(new Request(targetClass,method.getName(),args));
		if(obj instanceof Throwable) throw (Throwable)obj;
		return obj;
	}

}
