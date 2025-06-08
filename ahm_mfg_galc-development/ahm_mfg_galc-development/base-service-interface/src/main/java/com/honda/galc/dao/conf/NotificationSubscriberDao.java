package com.honda.galc.dao.conf;


import java.util.List;

import com.honda.galc.entity.conf.NotificationSubscriber;
import com.honda.galc.entity.conf.NotificationSubscriberId;
import com.honda.galc.enumtype.SubscriptionType;
import com.honda.galc.notification.service.ISubscriptionService;
import com.honda.galc.service.IDaoService;


public interface NotificationSubscriberDao extends IDaoService<NotificationSubscriber, NotificationSubscriberId>, ISubscriptionService {
    public List<NotificationSubscriber> findAllByNotificationClass(String notificationClass);
    public List<NotificationSubscriber> findAll(String notificationClass, String clientIp, int clientPort);
    public List<NotificationSubscriber> findAllBySubscriptionType(String notificationClass, SubscriptionType type);
    public int updateNotificationClass(String notificationClass, String oldNotificationClass);
}
