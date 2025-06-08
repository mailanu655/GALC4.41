package com.honda.galc.entity.conf;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;

@Embeddable
public class BroadcastDestinationId implements Serializable {
	@Column(name="PROCESS_POINT_ID")
	private String processPointId;

	@Column(name="SEQUENCE_NUMBER")
	private int sequenceNumber;

	private static final long serialVersionUID = 1L;

	public BroadcastDestinationId() {
		super();
	}

	public BroadcastDestinationId(String processPointId,int sequenceNumber) {
		super();
		this.processPointId = processPointId;
		this.sequenceNumber = sequenceNumber;
	}
	
	public String getProcessPointId() {
		return StringUtils.trim(this.processPointId);
	}

	public void setProcessPointId(String processPointId) {
		this.processPointId = processPointId;
	}

	public int getSequenceNumber() {
		return this.sequenceNumber;
	}

	public void setSequenceNumber(int sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if ( ! (o instanceof BroadcastDestinationId)) {
			return false;
		}
		BroadcastDestinationId other = (BroadcastDestinationId) o;
		return this.getProcessPointId().equals(other.getProcessPointId())
			&& (this.sequenceNumber == other.sequenceNumber);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.processPointId.hashCode();
		hash = hash * prime + this.sequenceNumber;
		return hash;
	}

}
