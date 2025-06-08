package com.honda.galc.entity.gts;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;
import com.honda.galc.notification.ISubscriber;

/**
 * 
 * 
 * <h3>GtsClientList Class description</h3>
 * <p> GtsClientList description </p>
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
 * Nov 14, 2014
 *
 *
 */
@Entity
@Table(name="GTS_CLIENT_LIST_TBX")
public class GtsClientList extends AuditEntry implements ISubscriber {
	@EmbeddedId
	private GtsClientListId id;

	@Column(name="CLIENT_NAME")
	private String clientName;

	private static final long serialVersionUID = 1L;

	public GtsClientList() {
		super();
	}

	public GtsClientListId getId() {
		return this.id;
	}

	public void setId(GtsClientListId id) {
		this.id = id;
	}

	public String getClientName() {
		return StringUtils.trim(this.clientName);
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}
	
	public String toString() {
		return toString(getId().getTrackingArea(),getId().getClientIp(),getId().getClientPort(),getClientName());
	}

	public boolean isHost(String clientHostName, int clientPort) {
        return this.getClientHostName().equals(clientHostName) && this.getClientPort() == clientPort;
	}

	public String getClientHostName() {
		return getId().getClientIp();
	}

	public int getClientPort() {
		return getId().getClientPort();
	}
}
