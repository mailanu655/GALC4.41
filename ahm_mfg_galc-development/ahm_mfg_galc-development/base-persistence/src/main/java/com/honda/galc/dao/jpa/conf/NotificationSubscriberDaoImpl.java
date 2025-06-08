package com.honda.galc.dao.jpa.conf;


import java.util.List;

import org.springframework.transaction.annotation.Transactional;


import com.honda.galc.dao.conf.NotificationSubscriberDao;
import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.entity.conf.NotificationSubscriber;
import com.honda.galc.entity.conf.NotificationSubscriberId;
import com.honda.galc.enumtype.SubscriptionType;
import com.honda.galc.service.Parameters;

/**
 * 
 * <h3>NotificationSubscriberDaoImpl Class description</h3>
 * <p> NotificationSubscriberDaoImpl description </p>
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
 * @author Jeffray Huang<br>
 * Mar 29, 2010
 *
 */
public class NotificationSubscriberDaoImpl extends BaseDaoImpl<NotificationSubscriber, NotificationSubscriberId> implements NotificationSubscriberDao {

	private static final String UPDATE_NOTIFICATION_CLASS = "update NotificationSubscriber e set e.id.notificationClass = :notificationClass where e.id.notificationClass = :oldNotificationClass";

    public List<NotificationSubscriber> findAllByNotificationClass(String notificationClass) {
        return findAll(Parameters.with("id.notificationClass", notificationClass), new String[] { "clientName", "id.provider", "id.clientIp", "id.clientPort" });
    }

    public List<NotificationSubscriber> findAll(String notificationClass, String clientIp, int clientPort) {
		Parameters parameters = Parameters.with("id.notificationClass", notificationClass);
		parameters.put("id.clientIp", clientIp);
		parameters.put("id.clientPort", clientPort);
        return findAll(parameters);
    }
    
	public List<NotificationSubscriber> findAllBySubscriptionType(String notificationClass, SubscriptionType subscriptiontype) {
		Parameters parameters = Parameters.with("id.notificationClass", notificationClass);
		parameters.put("subscriptionType", subscriptiontype);
        return findAll(parameters);
	}
	
    @Transactional
    public boolean subscribe(String producerName, String clientHostName, int clientPort,String provider, String clientName) {
        save(new NotificationSubscriber(producerName,clientHostName,clientPort, provider, clientName));
        return true;
    }

    @Transactional
    public boolean unsubscribe(String producerName, String clientHostName, int clientPort) {
        remove(new NotificationSubscriber(producerName,clientHostName,clientPort));
        return true;
    }

    public boolean unsubscribe(String clientHostName, int clientPort) {
        // TODO Auto-generated method stub
        return false;
    }

	public boolean heartbeat(String producerName, String clientHostName, int clientPort) {
		return true;
	}

	@Override
	@Transactional
	public int updateNotificationClass(String notificationClass, String oldNotificationClass) {
		return executeUpdate(UPDATE_NOTIFICATION_CLASS, Parameters.with("notificationClass", notificationClass).put("oldNotificationClass", oldNotificationClass));
	}
 }
