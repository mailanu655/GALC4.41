package com.honda.galc.dao.conf;


import java.util.List;

import com.honda.galc.entity.conf.NotificationProvider;
import com.honda.galc.entity.conf.NotificationProviderId;
import com.honda.galc.service.IDaoService;


public interface NotificationProviderDao extends IDaoService<NotificationProvider, NotificationProviderId> {
	public List<NotificationProvider> findAllByNotificationClass(String notificationClass);
	public int updateNotificationClass(String notificationClass, String oldNotificationClass);
}
