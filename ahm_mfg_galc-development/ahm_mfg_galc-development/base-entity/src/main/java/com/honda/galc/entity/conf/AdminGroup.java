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
@Table(name="GAL601TBX")
public class AdminGroup extends AuditEntry {
	@Id
	@Column(name="GROUP_ID")
	private String groupId;

	@Column(name="GROUP_DESC")
	private String groupDesc;

	@Column(name="DISPLAY_NAME")
	private String displayName;

	private static final long serialVersionUID = 1L;

	public AdminGroup() {
		super();
	}

	public String getGroupId() {
		return this.groupId;
	}

	public String getId() {
		return getGroupId();
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getGroupDesc() {
		return this.groupDesc;
	}

	public void setGroupDesc(String groupDesc) {
		this.groupDesc = groupDesc;
	}

	public String getDisplayName() {
		return this.displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getDisplayText() {
		return getGroupId() + "[" + this.getGroupDesc() + "]";
	}
	@Override
	public String toString() {
		return toString(getGroupId(),getGroupDesc());
	}


}
