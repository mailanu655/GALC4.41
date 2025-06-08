package com.honda.galc.entity.product;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;

@Entity
@Table(name="SEQUENCE_TBX")
public class Sequence extends AuditEntry {
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="SEQUENCE_ID_NAME")
	private String sequenceIdName;

	@Column(name="START_SEQ")
	private Integer startSeq;

	@Column(name="END_SEQ")
	private Integer endSeq;
	
	@Column(name="CURRENT_SEQ")
	private Integer currentSeq;
	
	@Column(name="INCREMENT_VALUE")
	private Integer incrementValue;
	
	@Column(name="DESCRIPTION")
	private String description;
	

	public String getId() {
		return sequenceIdName;
	}
	
	public String getSequenceIdName() {
		return StringUtils.trim(sequenceIdName);
	}

	public Integer getStartSeq() {
		return startSeq;
	}

	public Integer getEndSeq() {
		return endSeq;
	}

	public Integer getCurrentSeq() {
		return currentSeq;
	}

	public Integer getIncrementValue() {
		return incrementValue;
	}

	public String getDescription() {
		return StringUtils.trim(description);
	}

	public void setSequenceIdName(String sequenceIdName) {
		this.sequenceIdName = sequenceIdName;
	}

	public void setStartSeq(Integer startSeq) {
		this.startSeq = startSeq;
	}

	public void setEndSeq(Integer endSeq) {
		this.endSeq = endSeq;
	}

	public void setCurrentSeq(Integer currentSeq) {
		this.currentSeq = currentSeq;
	}

	public void setIncrementValue(Integer incrementValue) {
		this.incrementValue = incrementValue;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((sequenceIdName == null) ? 0 : sequenceIdName.hashCode());
		
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
		Sequence other = (Sequence) obj;
		
		if (sequenceIdName == null) {
			if (other.sequenceIdName != null)
				return false;
		} else if (!getSequenceIdName().equals(other.getSequenceIdName()))
			return false;
		
		return true;
	}

	@Override
	public String toString() {
		return "Sequence [sequenceIdName=" + sequenceIdName + ", startSeq=" + startSeq + ", endSeq=" + endSeq
				+ ", currentSeq=" + currentSeq + ", incrementValue=" + incrementValue + ", description=" + description
				+ "]";
	}
	
	
	
}
