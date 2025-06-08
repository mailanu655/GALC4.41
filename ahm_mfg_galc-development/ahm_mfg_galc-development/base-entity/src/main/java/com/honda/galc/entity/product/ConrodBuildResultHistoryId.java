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
public class ConrodBuildResultHistoryId implements Serializable {
	@Column(name="CONROD_ID")
	private String conrodId;

	@Column(name="PART_NAME")
	private String partName;
	
	@Column(name="ACTUAL_TIMESTAMP")
	private Date actualTimestamp;

	private static final long serialVersionUID = 1L;

	public ConrodBuildResultHistoryId() {
		super();
	}

	public ConrodBuildResultHistoryId(String conrodId, String partName, Date actualTimestamp) {
		super();
		this.conrodId = conrodId;
		this.partName = partName;
		this.actualTimestamp = actualTimestamp;
	}

	public String getConrodId() {
		return conrodId;
	}

	public void setConrodId(String conrodId) {
		this.conrodId = conrodId;
	}

	public String getPartName() {
		return StringUtils.trim(this.partName);
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
		result = prime * result + ((conrodId == null) ? 0 : conrodId.hashCode());
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
		ConrodBuildResultHistoryId other = (ConrodBuildResultHistoryId) obj;
		if (actualTimestamp == null) {
			if (other.actualTimestamp != null)
				return false;
		} else if (!actualTimestamp.equals(other.actualTimestamp))
			return false;
		if (conrodId == null) {
			if (other.conrodId != null)
				return false;
		} else if (!conrodId.equals(other.conrodId))
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
		return "ConrodBuildResultHistoryId [conrodId=" + conrodId + ", partName=" + partName + ", actualTimestamp="
				+ actualTimestamp + "]";
	}

}
