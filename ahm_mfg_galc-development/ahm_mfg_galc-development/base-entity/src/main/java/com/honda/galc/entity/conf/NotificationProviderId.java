package com.honda.galc.entity.conf;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;

@Embeddable
public class NotificationProviderId implements Serializable {
	
    @Column(name="NOTIFICATION_CLASS")
    private String notificationClass;

	@Column(name="HOST_IP")
	private String hostIp;

	@Column(name="HOST_PORT")
	private int hostPort;

	private static final long serialVersionUID = 1L;

	public NotificationProviderId() {
		super();
	}

	public String getNotificationClass() {
		return StringUtils.trim(this.notificationClass);
	}

	public void setNotificationClass(String notificationClass) {
		this.notificationClass = StringUtils.trim(notificationClass);
	}

	public String getHostIp() {
		return StringUtils.trim(this.hostIp);
	}

	public void setHostIp(String hostIp) {
		this.hostIp = StringUtils.trim(hostIp);
	}

	public int getHostPort() {
		return this.hostPort;
	}

	public void setHostPort(int hostPort) {
		this.hostPort = hostPort;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if ( ! (o instanceof NotificationProviderId)) {
			return false;
		}
		NotificationProviderId other = (NotificationProviderId) o;
		return this.getNotificationClass().equals(other.getNotificationClass())
			&& this.getHostIp().equals(other.getHostIp())
			&& (this.hostPort == other.hostPort);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.notificationClass.hashCode();
		hash = hash * prime + this.hostIp.hashCode();
		hash = hash * prime + ((int) this.hostPort);
		return hash;
	}

	@Override
	public String toString() {
		return "NotificationProviderId [notificationClass=" + notificationClass + ", hostIp=" + hostIp + ", hostPort=" + hostPort + "]";
	}

}
