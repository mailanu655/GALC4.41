package com.honda.galc.notification;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.data.ApplicationContextProvider;
import com.honda.galc.net.NotificationRequest;

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
public class NotificationInvocationHandler implements InvocationHandler{
	private String targetClass;
	private IProducer producer;
	
	public NotificationInvocationHandler(String className, IProducer producer) {
		this.targetClass = className;
		this.producer = producer;
	}
	
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		if(producer != null) {
			NotificationServiceExecutor executor = getExecutor();
			if(executor == null) return null; 
			// asyncronous execute , return right away
			executor.invoke(producer, new NotificationRequest(targetClass, method.getName(), args));
		}
		return null;
	}
	
	private NotificationServiceExecutor getExecutor() {
		return (NotificationServiceExecutor)ApplicationContextProvider.getBean("NotificationServiceExecutor");
	}
}
