package com.honda.galc.dao.jpa.conf;


import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.conf.NotificationProviderDao;
import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.entity.conf.NotificationProvider;
import com.honda.galc.entity.conf.NotificationProviderId;
import com.honda.galc.service.Parameters;

/**
 * 
 * <h3>NotificationProviderDaoImpl Class description</h3>
 * <p> NotificationProviderDaoImpl description </p>
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
public class NotificationProviderDaoImpl extends BaseDaoImpl<NotificationProvider, NotificationProviderId> implements NotificationProviderDao {

	private static final String UPDATE_NOTIFICATION_CLASS = "update NotificationProvider e set e.id.notificationClass = :notificationClass where e.id.notificationClass = :oldNotificationClass";

	@Override
	public List<NotificationProvider> findAllByNotificationClass(String notificationClass) {
		return findAll(Parameters.with("id.notificationClass", notificationClass), new String[] { "hostName", "id.hostIp", "id.hostPort" });
	}

	@Override
	@Transactional
	public int updateNotificationClass(String notificationClass, String oldNotificationClass) {
		return executeUpdate(UPDATE_NOTIFICATION_CLASS, Parameters.with("notificationClass", notificationClass).put("oldNotificationClass", oldNotificationClass));
	}

}
