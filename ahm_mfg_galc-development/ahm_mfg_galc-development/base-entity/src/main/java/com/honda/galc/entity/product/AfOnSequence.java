package com.honda.galc.entity.product;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;


/*
 * 
 * @author Gangadhararao Gadde 
 * @since May 19, 2014
 */
@Entity
@Table(name="GAL103TBX")
public class AfOnSequence extends AuditEntry {
	
	@Id
	@Column(name="PROCESS_POINT_ID")
	private String  processPointId;

	@Column(name="CURRENT_SEQUENCE_NUMBER")
	private Integer currentSeqNum;
	
	private static final long serialVersionUID = 1L;
	
	public AfOnSequence(String processPointId, Integer currentSeqNum) {
		super();
		this.processPointId = processPointId;
		this.currentSeqNum = currentSeqNum;
	}

	public AfOnSequence() {
		super();
	}

	public String getProcessPointId() {
		return StringUtils.trimToEmpty(processPointId);
	}

	public void setProcessPointId(String processPointId) {
		this.processPointId = processPointId;
	}

	public Integer getCurrentSeqNum() {
		return currentSeqNum;
	}

	public void setCurrentSeqNum(Integer currentSeqNum) {
		this.currentSeqNum = currentSeqNum;
	}

	public String getId() {
		return this.processPointId;
	}

	public void setId(String processPointId) {
		this.processPointId = processPointId;
	}

	@Override
	public String toString() {
		return toString(getProcessPointId(),getCurrentSeqNum());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((currentSeqNum == null) ? 0 : currentSeqNum.hashCode());
		result = prime * result
				+ ((processPointId == null) ? 0 : processPointId.hashCode());
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
		AfOnSequence other = (AfOnSequence) obj;
		if (currentSeqNum == null) {
			if (other.currentSeqNum != null)
				return false;
		} else if (!currentSeqNum.equals(other.currentSeqNum))
			return false;
		if (processPointId == null) {
			if (other.processPointId != null)
				return false;
		} else if (!processPointId.equals(other.processPointId))
			return false;
		return true;
	}

}
