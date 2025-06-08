package com.honda.galc.notification;

import com.honda.galc.net.NotificationRequest;
import com.honda.galc.notification.IProducer;

public interface NotificationServiceExecutor {
	public void invoke(IProducer producer, NotificationRequest nRequest);
}
