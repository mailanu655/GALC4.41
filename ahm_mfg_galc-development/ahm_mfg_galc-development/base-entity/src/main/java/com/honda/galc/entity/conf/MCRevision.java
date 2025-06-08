package com.honda.galc.entity.conf;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;

/**
 * @author Subu Kathiresan
 * Feb 10, 2014
 */
@Entity
@Table(name="MC_REV_TBX")
public class MCRevision extends AuditEntry implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="REV_ID", nullable=false)
	private long id;

	@Column(name="ASSOCIATE_NO", length=11)
	private String associateNo;

	@Column(name="REV_DESC", length=255)
	private String description;

	@Column(name="REV_STATUS", length=32)
	private String status;

	@Column(name="REV_TYPE", length=32)
	private String type;

    public MCRevision() {}

	public Long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getAssociateNo() {
		return StringUtils.trim(this.associateNo);
	}

	public void setAssociateNo(String associateNo) {
		this.associateNo = associateNo;
	}

	public String getDescription() {
		return StringUtils.trim(this.description);
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getStatus() {
		return StringUtils.trim(this.status);
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getType() {
		return StringUtils.trim(this.type);
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((associateNo == null) ? 0 : associateNo.hashCode());
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		MCRevision other = (MCRevision) obj;
		if (associateNo == null) {
			if (other.associateNo != null)
				return false;
		} else if (!associateNo.equals(other.associateNo))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (id != other.id)
			return false;
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}
	
	@Override
	public String toString(){
		return toString(getId(), getAssociateNo(), getDescription(), getStatus(), getType());
	}
}