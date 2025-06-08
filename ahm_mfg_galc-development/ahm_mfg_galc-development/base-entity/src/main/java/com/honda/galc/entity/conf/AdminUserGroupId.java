package com.honda.galc.entity.conf;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class AdminUserGroupId implements Serializable {
	@Column(name="USER_ID")
	private String userId;

	@Column(name="GROUP_ID")
	private String groupId;

	private static final long serialVersionUID = 1L;

	public AdminUserGroupId() {
		super();
	}
	
	public AdminUserGroupId(String userId, String groupId) {
		this.userId = userId;
		this.groupId= groupId;
	}

	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getGroupId() {
		return this.groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if ( ! (o instanceof AdminUserGroupId)) {
			return false;
		}
		AdminUserGroupId other = (AdminUserGroupId) o;
		return this.userId.equals(other.userId)
			&& this.groupId.equals(other.groupId);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.userId.hashCode();
		hash = hash * prime + this.groupId.hashCode();
		return hash;
	}

}
