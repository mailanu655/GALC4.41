package com.honda.galc.notification;

import static com.honda.galc.common.logging.Logger.getLogger;

import java.util.ArrayList;
import java.util.List;

import com.honda.galc.net.NotificationRequest;
import com.honda.galc.net.SocketRequestInvoker;
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
public class NotificationProducer implements IProducer {
	String name;
	List<ISubscriber> subscribers = new ArrayList<ISubscriber>();

	public NotificationProducer(String name) {
		this.name = name;
	}

	public  <T extends ISubscriber> NotificationProducer(String name,List<T> subscribers) {
		this(name);
		this.subscribe(subscribers);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<ISubscriber> getSubscribers() {
		return subscribers;
	}

	public void setSubscribers(List<ISubscriber> subscribers) {
		this.subscribers = subscribers;
	}

	public <T extends ISubscriber> void subscribe(List<T> subscribers) {
		this.subscribers.addAll(subscribers);
	}

	public void subscribe(ISubscriber subscriber) {
		if(contains(subscriber)) return;	
		this.subscribers.add(subscriber);
	}

	private boolean contains(ISubscriber subscriber) {

		String ip = subscriber.getClientHostName();
		int port = subscriber.getClientPort();

		for(ISubscriber item : subscribers) {
			if(item.isHost(ip, port)) return true;
		}
		return false;
	}

	public void unsubscribe() {
		this.subscribers.clear();
	}

	public void unsubscribe(ISubscriber subscriber) {
		subscribers.remove(subscriber);
		getLogger().warn(subscriber.toString(), "is removed from subscription list");
	}

	public boolean unsubscribe(String clientHostName,int clientPort) {
		for(ISubscriber subscriber : subscribers) {
			if(subscriber.isHost(clientHostName, clientPort)) {
				subscribers.remove(subscriber);
				return true;
			}
		}
		return false;
	}

	public boolean contains(String clientHostName, int clientPort) {
		for(ISubscriber item : subscribers) {
			if(item.isHost(clientHostName, clientPort)) return true;
		}
		return false;
	}

	public void publish(NotificationRequest request, ISubscriber subscriber) {
		try {
			new SocketRequestInvoker(subscriber.getClientHostName(),subscriber.getClientPort()).notify(request);
			getLogger().debug("sent notification " + request + " to suscriber : " + subscriber.toString());
		} catch(RuntimeException e ) {
			getLogger().error(e,"failed to send notification " + request + ". reason: " + e.getMessage());
		}
	}
}
