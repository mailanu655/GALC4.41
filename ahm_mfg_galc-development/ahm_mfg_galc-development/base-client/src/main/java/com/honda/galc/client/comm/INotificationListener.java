package com.honda.galc.client.comm;

public interface INotificationListener<T extends INotification> {

	public void processNotification(T notification);

}
