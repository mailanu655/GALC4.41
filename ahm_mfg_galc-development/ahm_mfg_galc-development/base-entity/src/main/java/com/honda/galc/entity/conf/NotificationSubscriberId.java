package com.honda.galc.entity.conf;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;

@Embeddable
public class NotificationSubscriberId implements Serializable {
    @Column(name="NOTIFICATION_CLASS")
    private String notificationClass;

	@Column(name="CLIENT_IP")
	private String clientIp;

	@Column(name="CLIENT_PORT")
	private int clientPort;

	@Column(name="PROVIDER")
	private String provider;

	private static final long serialVersionUID = 1L;

	public NotificationSubscriberId() {}
	
	public NotificationSubscriberId(String notificationClass,String clientIp, int clientPort, String provider) {
		this.notificationClass = StringUtils.trim(notificationClass);
		this.clientIp = StringUtils.trim(clientIp);
		this.clientPort = clientPort;
		this.provider = StringUtils.trim(provider);
	}

	public String getNotificationClass() {
		return StringUtils.trim(this.notificationClass);
	}

	public void setNotificationClass(String notificationClass) {
		this.notificationClass = StringUtils.trim(notificationClass);
	}

	public String getClientIp() {
		return StringUtils.trim(this.clientIp);
	}

	public void setClientIp(String clientIp) {
		this.clientIp = StringUtils.trim(clientIp);
	}

	public int getClientPort() {
		return this.clientPort;
	}

	public void setClientPort(int clientPort) {
		this.clientPort = clientPort;
	}

	public String getProvider() {
		return StringUtils.trim(provider);
	}

	public void setProvider(String provider) {
		this.provider = StringUtils.trim(provider);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((clientIp == null) ? 0 : clientIp.hashCode());
		result = prime * result + clientPort;
		result = prime
				* result
				+ ((notificationClass == null) ? 0 : notificationClass
						.hashCode());
		result = prime * result
				+ ((provider == null) ? 0 : provider.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NotificationSubscriberId other = (NotificationSubscriberId) obj;
		if (clientIp == null) {
			if (other.clientIp != null)
				return false;
		} else if (!clientIp.equals(other.clientIp))
			return false;
		if (clientPort != other.clientPort)
			return false;
		if (notificationClass == null) {
			if (other.notificationClass != null)
				return false;
		} else if (!notificationClass.equals(other.notificationClass))
			return false;
		if (provider == null) {
			if (other.provider != null)
				return false;
		} else if (!provider.equals(other.provider))
			return false;
		return true;
	}

	public String toString() {
		StringBuilder builder = new StringBuilder("NotificationSubscriberId(");
        builder.append(this.notificationClass + "-");
        builder.append(this.clientIp + ":" + this.clientPort);
        builder.append(",");
        builder.append(this.provider);
        builder.append(")");
        return builder.toString();
	}
}
