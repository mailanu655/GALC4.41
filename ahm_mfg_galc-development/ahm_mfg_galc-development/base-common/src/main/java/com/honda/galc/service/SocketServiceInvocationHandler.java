package com.honda.galc.service;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import com.honda.galc.net.Request;
import com.honda.galc.net.TcpEndpoint;
import com.honda.galc.net.SocketRequestInvoker;


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
public class SocketServiceInvocationHandler implements InvocationHandler{
	private String targetClass;
	private SocketRequestInvoker socketClient;
	
	public SocketServiceInvocationHandler(String clientIp, int port,String className) {
	    
		this.targetClass = className;
		this.socketClient = new SocketRequestInvoker(clientIp,port);
	}
	
	public SocketServiceInvocationHandler(TcpEndpoint endPoint,String className) {
	    
	    this.targetClass = className;
	    this.socketClient = new SocketRequestInvoker(endPoint);
	}
	    
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
	    
	    Object obj = null;
	    obj = socketClient.invoke(new Request(targetClass,method.getName(),args));
	    if(obj instanceof Throwable) throw (Throwable)obj;
		return obj;
	}

}
