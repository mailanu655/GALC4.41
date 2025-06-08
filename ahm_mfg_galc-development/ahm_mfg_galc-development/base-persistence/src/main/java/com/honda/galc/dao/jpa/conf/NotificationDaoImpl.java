package com.honda.galc.dao.jpa.conf;


import java.util.List;

import com.honda.galc.dao.conf.NotificationDao;
import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.entity.conf.Notification;

/**
 * 
 * <h3>NotificationDaoImpl Class description</h3>
 * <p> NotificationDaoImpl description </p>
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
public class NotificationDaoImpl extends BaseDaoImpl<Notification, String> implements NotificationDao {

	@Override
	public List<Notification> findAllOrderByNotificationClass() {
		return findAll(null, new String[] { "notificationClass" });
	}

}
