package com.honda.galc.dao.conf;


import java.util.List;

import com.honda.galc.entity.conf.Notification;
import com.honda.galc.service.IDaoService;


public interface NotificationDao extends IDaoService<Notification, String> {

	public List<Notification> findAllOrderByNotificationClass();

}
