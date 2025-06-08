package com.honda.galc.entity;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import org.apache.commons.lang.StringUtils;

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
public abstract class CreateUserAuditEntry extends UserAuditEntry {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Column(name = "CREATE_USER")
	private String createUser;

	/**
	 * @return the createUser
	 */
	public String getCreateUser() {
		return StringUtils.trimToEmpty(this.createUser);
	}

	/**
	 * @param createUser
	 *            the createUser to set
	 */
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public String getCreateDate() {
		if(getCreateTimestamp() == null) return "";
		return CommonUtil.formatDate(getCreateTimestamp());
    }

    public String getUpdateDate() {
    	if(getUpdateTimestamp() == null) return "";
		return CommonUtil.formatDate(getUpdateTimestamp());
    }
}
