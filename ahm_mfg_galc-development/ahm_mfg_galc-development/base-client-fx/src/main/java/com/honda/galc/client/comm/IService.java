package com.honda.galc.client.comm;

import com.honda.galc.client.comm.IResponse;
import com.honda.galc.client.comm.IRequest;
import com.honda.galc.client.comm.INotificationListener;
import com.honda.galc.client.comm.IResource;

public interface IService {

	public IResponse send(IRequest request);

	public void post(IRequest request);

	public void registerNotification(Class<? extends INotification> notificationClass, 
			INotificationListener<? extends INotification> listener);

	public IResource getResource(IResourceId resourceId);

}
