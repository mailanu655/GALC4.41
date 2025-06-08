package com.honda.galc.entity.conf;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;

/** * * 
* @version 0.2 
* @author Gangadhararao Gadde 
* @since Aug 09, 2012
*/
@Entity
@Table(name="NOTIFICATION_PROVIDER_TBX")
public class NotificationProvider extends AuditEntry {
	@EmbeddedId
	private NotificationProviderId id;

	@Column(name="HOST_NAME")
	private String hostName;
	
	private String description;

	private static final long serialVersionUID = 1L;
	
	@SuppressWarnings("unused")
	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "NOTIFICATION_CLASS", referencedColumnName = "NOTIFICATION_CLASS",
                    unique = true, nullable = false, insertable = false, updatable = false)
    private Notification notification;
	
	@Transient
	private boolean isConnected = false;
	
	public boolean isConnected() {
        return isConnected;
    }

    public void setConnected(boolean isConnected) {
        this.isConnected = isConnected;
    }

    public NotificationProvider() {
		super();
	}

	public NotificationProviderId getId() {
		return this.id;
	}

	public void setId(NotificationProviderId id) {
		this.id = id;
	}

	public String getHostName() {
        return StringUtils.trim(hostName);
    }

    public void setHostName(String hostName) {
        this.hostName = StringUtils.trim(hostName);
    }

    public String getDescription() {
		return StringUtils.trim(description);
	}

	public void setDescription(String description) {
		this.description = StringUtils.trim(description);
	}

	@Override
	public String toString() {
		return toString(id.getNotificationClass(),id.getHostIp(),id.getHostPort());
	}


}
