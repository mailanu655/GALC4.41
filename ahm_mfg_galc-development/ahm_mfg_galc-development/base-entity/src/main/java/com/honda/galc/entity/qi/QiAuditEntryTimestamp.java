package com.honda.galc.entity.qi;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import com.honda.galc.entity.CreateUserAuditEntry;
import com.honda.galc.util.CommonUtil;

/**
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * QIAuditEntry is generic entry supporting create and update user
 * <p/>
 * <h4>Special Notes</h4>
 * <h4>Change History</h4>
 * <TABLE BORDER="1" CELLPADDING="3" CELLSPACING="0" WIDTH="100%">
 * <TR BGCOLOR="#EEEEFF" CLASS="TableSubHeadingColor">
 * <TH>Updated by</TH>
 * <TH>Update date</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * </TABLE>
 */
/** * *
* @author L&T Infotech
* @since April 25 ,2016
*/


@MappedSuperclass()
public abstract class QiAuditEntryTimestamp extends CreateUserAuditEntry {

	private static final long serialVersionUID = 1L;
	
    @Column(name = "APP_CREATE_TIMESTAMP")
    private Date appCreateTimestamp;

    @Column(name = "APP_UPDATE_TIMESTAMP")
    private Date appUpdateTimestamp;

	public Date getAppCreateTimestamp() {
		return appCreateTimestamp;
	}

	public void setAppCreateTimestamp(Date appCreateTimestamp) {
		this.appCreateTimestamp = appCreateTimestamp;
	}

	public Date getAppUpdateTimestamp() {
		return appUpdateTimestamp;
	}

	public void setAppUpdateTimestamp(Date appUpdateTimestamp) {
		this.appUpdateTimestamp = appUpdateTimestamp;
	}

	public String getCreateDate() {
		if(getAppCreateTimestamp() == null) return "";
		return CommonUtil.formatDate(getAppCreateTimestamp());
    }

    public String getUpdateDate() {
    	if(getAppUpdateTimestamp() == null) return "";
		return CommonUtil.formatDate(getAppUpdateTimestamp());
    }
}
