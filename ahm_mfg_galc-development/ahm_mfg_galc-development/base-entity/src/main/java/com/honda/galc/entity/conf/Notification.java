package com.honda.galc.entity.conf;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
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
@Table(name="NOTIFICATION_TBX")
public class Notification extends AuditEntry  {
	@Id
	@Column(name="NOTIFICATION_CLASS")
	private String notificationClass;

	private String description;

	@Column(name="CLIENT_ONLY")
	private short clientOnly;

	private static final long serialVersionUID = 1L;
	
	@OneToMany(targetEntity = NotificationProvider.class, mappedBy = "notification", cascade = {}, fetch = FetchType.EAGER)
    private List<NotificationProvider> notificationProviders = new ArrayList<NotificationProvider>();

   @Transient
    private boolean isConnected = false;

	public Notification() {
		super();
	}

	public String getNotificationClass() {
		return StringUtils.trim(this.notificationClass);
	}
	
	public String getId() {
		return getNotificationClass();
	}

	public void setNotificationClass(String notificationClass) {
		this.notificationClass = StringUtils.trim(notificationClass);
	}

	public String getDescription() {
		return StringUtils.trim(this.description);
	}

	public void setDescription(String description) {
		this.description = StringUtils.trim(description);
	}

	public short getClientOnly() {
		return this.clientOnly;
	}
	
	public boolean isClientOnly(){
	    return clientOnly > 0;
	}
	
	public boolean isConnected() {
        return isConnected;
    }

    public void setConnected(boolean isConnected) {
        this.isConnected = isConnected;
    }

    public void setClientOnly(short clientOnly) {
		this.clientOnly = clientOnly;
	}
    
    public void setClientOnly(boolean clientOnlyFlag) {
    	this.clientOnly = clientOnlyFlag ? (short) 1 : 0;
    }

    public List<NotificationProvider> getNotificationProviders() {
        return notificationProviders;
    }

    public void setNotificationProviders(List<NotificationProvider> notificationProviders) {
        this.notificationProviders = notificationProviders;
    }
    
	@Override
	public String toString() {
		return toString(getNotificationClass(),getClientOnly());
	}

}
