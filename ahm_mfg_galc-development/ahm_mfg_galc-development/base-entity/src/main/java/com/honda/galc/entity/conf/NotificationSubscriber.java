package com.honda.galc.entity.conf;


import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;
import com.honda.galc.enumtype.SubscriptionType;
import com.honda.galc.notification.INotificationSubscriber;

@Entity
@Table(name="NOTIFICATION_SUBSCRIBER_TBX")
public class NotificationSubscriber extends AuditEntry implements INotificationSubscriber {
	
	public static final String ANY_PROVIDER = "ANY";
	
	@EmbeddedId
	private NotificationSubscriberId id;

	@Column(name="CLIENT_NAME")
	private String clientName;
	
	@Column(name="NOTIFICATION_HANDLER_CLASS")
	private String notificationHandlerClass;
	
	@Column(name="SUBSCRIPTION_TYPE")
	@Enumerated(EnumType.STRING)
	private SubscriptionType subscriptionType;

	private static final long serialVersionUID = 1L;

	public NotificationSubscriber() {
		super();
	}
	
	public NotificationSubscriber(String notificationClass, String clientId, int clientPort) {
	    this(notificationClass, clientId, clientPort, "");
	}
	
	public NotificationSubscriber(String notificationClass, String clientId, int clientPort, String provider) {
	    this(notificationClass, clientId, clientPort, provider, null);
	}

	public NotificationSubscriber(String category, String clientId, int clientPort, String provider, String clientName) {
        this.id = new NotificationSubscriberId(category, clientId, clientPort, provider);
        this.clientName = StringUtils.trim(clientName);
    }
	public NotificationSubscriberId getId() {
		return this.id;
	}

	public void setId(NotificationSubscriberId id) {
		this.id = id;
	}

	public String getClientName() {
		return StringUtils.trim(clientName);
	}

	public void setClientName(String clientName) {
		this.clientName = StringUtils.trim(clientName);
	}

    public String getClientHostName() {
        return StringUtils.trim(id.getClientIp());
    }

    public int getClientPort() {
        return id.getClientPort();
    }
    
	public String getNotificationHandlerClass() {
		return StringUtils.trim(notificationHandlerClass);
	}

	public void setNotificationHandlerClass(String notificationHandlerClass) {
		this.notificationHandlerClass = StringUtils.trim(notificationHandlerClass);
	}

	public SubscriptionType getSubscriptionType() {
		return subscriptionType;
	}

	public void setSubscriptionType(SubscriptionType subscriptionType) {
		this.subscriptionType = subscriptionType;
	}

    public boolean isHost(String clientHostName, int clientPort) {
        return this.getClientHostName().equals(clientHostName) && this.getClientPort() == clientPort;
    }
    
    public void heartbeat(){}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((clientName == null) ? 0 : clientName.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime
				* result
				+ ((notificationHandlerClass == null) ? 0
						: notificationHandlerClass.hashCode());
		result = prime
				* result
				+ ((subscriptionType == null) ? 0 : subscriptionType.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		NotificationSubscriber other = (NotificationSubscriber) obj;
		if (clientName == null) {
			if (other.clientName != null)
				return false;
		} else if (!clientName.equals(other.clientName))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (notificationHandlerClass == null) {
			if (other.notificationHandlerClass != null)
				return false;
		} else if (!notificationHandlerClass
				.equals(other.notificationHandlerClass))
			return false;
		if (subscriptionType != other.subscriptionType)
			return false;
		return true;
	}

	public String toString() {
        StringBuilder builder = new StringBuilder("Subscriber(");
        if(clientName!= null) builder.append(clientName + "-");
        builder.append(id.getClientIp() + ":" +id.getClientPort());
        builder.append(",");
        builder.append(id.getNotificationClass());
        builder.append(",");
        builder.append(getSubscriptionType().toString());
        builder.append(",");
        builder.append(getNotificationHandlerClass());
        builder.append(")");
        return builder.toString();
    }
}
