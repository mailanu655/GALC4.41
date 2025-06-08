package com.honda.galc.entity.conf;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.honda.galc.entity.AuditEntry;

/** * *
* @version 0.2
* @author Gangadhararao Gadde
* @since Aug 09, 2012
*/
@Entity
@Table(name="GAL600TBX")
public class AdminUser extends AuditEntry {
	@Id
	@Column(name="USER_ID")
	private String userId;

	@Column(name="PASSWORD")
	private String password;

	@Column(name="USER_DESC")
	private String userDesc;

	@Column(name="DISPLAY_NAME")
	private String displayName;

	private static final long serialVersionUID = 1L;

	public AdminUser() {
		super();
	}

	public String getUserId() {
		return this.userId;
	}

	public String getId() {
		return this.getUserId();
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUserDesc() {
		return this.userDesc;
	}

	public void setUserDesc(String userDesc) {
		this.userDesc = userDesc;
	}

	public String getDisplayName() {
		return this.displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	@Override
	public String toString() {
		return toString(getUserId());
	}
}
