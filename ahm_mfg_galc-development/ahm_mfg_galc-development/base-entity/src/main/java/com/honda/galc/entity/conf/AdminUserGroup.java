package com.honda.galc.entity.conf;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.honda.galc.entity.AuditEntry;

/** * *
* @version 0.2
* @author Gangadhararao Gadde
* @since Aug 09, 2012
*/
@Entity
@Table(name="GAL602TBX")
public class AdminUserGroup extends AuditEntry {
	@EmbeddedId
	private AdminUserGroupId id;

	private static final long serialVersionUID = 1L;

//	@OneToOne(targetEntity = AdminGroup.class,fetch = FetchType.EAGER)
//    @JoinColumn(name="USER_ID",referencedColumnName="USER_ID")
//    private AdminUser adminUser;
//
	@OneToOne(targetEntity = AdminGroup.class,fetch = FetchType.EAGER)
    @JoinColumn(name="GROUP_ID",referencedColumnName="GROUP_ID",updatable=false,insertable=false)
    private AdminGroup adminGroup;

	public AdminUserGroup() {
		super();
	}

	public AdminUserGroup(String userId, String groupId) {
		this.id = new AdminUserGroupId(userId,groupId);
	}

	public AdminUserGroupId getId() {
		return this.id;
	}

	public void setId(AdminUserGroupId id) {
		this.id = id;
	}

	public String getUserId() {
		return id.getUserId();
	}

	public String getGroupId() {
		return id.getGroupId();
	}

	public String getGroupDisplayText(){
		return getGroupId() + "[" + adminGroup.getGroupDesc() + "]";
	}
//	public AdminUser getAdminUser() {
//		return adminUser;
//	}

	public AdminGroup getAdminGroup() {
		return adminGroup;
	}

	@Override
	public String toString() {
		return toString(getUserId(),getGroupId());
	}
}
