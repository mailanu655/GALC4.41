package com.honda.galc.entity.product;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;

/** * * 
* @author Xiaomei Ma
* @since Feb 27, 2020
*/
@Embeddable
public class HeadBuildResultHistoryId implements Serializable {
	@Column(name="HEAD_ID")
	private String headId;

	@Column(name="PART_NAME")
	private String partName;
	
	@Column(name="ACTUAL_TIMESTAMP")
	private Date actualTimestamp;

	private static final long serialVersionUID = 1L;

	public HeadBuildResultHistoryId() {
		super();
	}

	public HeadBuildResultHistoryId(String headId, String partName, Date actualTimestamp) {
		super();
		this.headId = headId;
		this.partName = partName;
		this.actualTimestamp = actualTimestamp;
	}

	public String getPartName() {
		return StringUtils.trim(this.partName);
	}

	public String getHeadId() {
		return headId;
	}

	public void setHeadId(String headId) {
		this.headId = headId;
	}

	public void setPartName(String partName) {
		this.partName = partName;
	}

	public Date getActualTimestamp() {
		return actualTimestamp;
	}

	public void setActualTimestamp(Date actualTimestamp) {
		this.actualTimestamp = actualTimestamp;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((actualTimestamp == null) ? 0 : actualTimestamp.hashCode());
		result = prime * result + ((headId == null) ? 0 : headId.hashCode());
		result = prime * result + ((partName == null) ? 0 : partName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HeadBuildResultHistoryId other = (HeadBuildResultHistoryId) obj;
		if (actualTimestamp == null) {
			if (other.actualTimestamp != null)
				return false;
		} else if (!actualTimestamp.equals(other.actualTimestamp))
			return false;
		if (headId == null) {
			if (other.headId != null)
				return false;
		} else if (!headId.equals(other.headId))
			return false;
		if (partName == null) {
			if (other.partName != null)
				return false;
		} else if (!partName.equals(other.partName))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "HeadBuildResultHistoryId [headId=" + headId + ", partName=" + partName + ", actualTimestamp="
				+ actualTimestamp + "]";
	}
}
