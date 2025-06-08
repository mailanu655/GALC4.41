package com.honda.galc.notification;

import static com.honda.galc.common.logging.Logger.getLogger;

import java.util.ArrayList;
import java.util.List;

import com.honda.galc.dao.gts.GtsClientListDao;
import com.honda.galc.entity.gts.GtsClientList;
import com.honda.galc.net.NotificationRequest;
import com.honda.galc.net.SocketRequestInvoker;
import com.honda.galc.service.ServiceFactory;
/**
 * 
 * <h3>NotificationProducer Class description</h3>
 * <p> NotificationProducer description </p>
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
 * Jun 10, 2010
 *
 *
 */
public class GtsNotificationProducer implements IProducer{
    String areaName;
 
    public GtsNotificationProducer(String areaName) {
        this.areaName = areaName;
    }
    
	public String getName() {
		return null;
	}

	public void subscribe(ISubscriber subscriber) {}

	public void unsubscribe() {}

	public boolean contains(String clientHostName, int clientPort) {
		return true;
	}

	public List<ISubscriber> getSubscribers() {
		List<ISubscriber> subscribers = new ArrayList<ISubscriber>();
		for (GtsClientList gtsClient: ServiceFactory.getDao(GtsClientListDao.class).findAll(areaName)) {
			subscribers.add(gtsClient);
		}
    	return subscribers;
	}

	public void publish(NotificationRequest request, ISubscriber subscriber) {
        
    	getLogger(areaName).debug("sending notification " + request + " to suscriber "
    			+ subscriber.getClientHostName() + ":" + subscriber.getClientPort());
		try {
			new SocketRequestInvoker(subscriber.getClientHostName(),subscriber.getClientPort()).notify(request);
			getLogger(areaName).debug("sent notification " + request + " to client : " + subscriber );
		} catch(RuntimeException e ) {
			getLogger(areaName).error(e,"failed to send notification " + request + ". reason: " + e.getMessage());
		}
	}
}
