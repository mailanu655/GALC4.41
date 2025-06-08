package com.honda.galc.entity.product;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;

@Embeddable
public class CrankshaftHistoryId implements Serializable {
	@Column(name="CRANKSHAFT_ID")
	private String crankshaftId;

	@Column(name="PROCESS_POINT_ID")
	private String processPointId;

	@Column(name="ACTUAL_TIMESTAMP")
	private Timestamp actualTimestamp;

	private static final long serialVersionUID = 1L;

	public CrankshaftHistoryId() {
		super();
	}
	
	public CrankshaftHistoryId(String crankshaftId, String processPointId) {
		this.crankshaftId = crankshaftId;
		this.processPointId = processPointId;
		setActualTimestamp(new Timestamp(System.currentTimeMillis()));
	}

	public String getCrankshaftId() {
		return StringUtils.trim(this.crankshaftId);
	}

	public void setCrankshaftId(String crankshaftId) {
		this.crankshaftId = crankshaftId;
	}

	public String getProcessPointId() {
		return StringUtils.trim(this.processPointId);
	}

	public void setProcessPointId(String processPointId) {
		this.processPointId = processPointId;
	}

	public Timestamp getActualTimestamp() {
		return this.actualTimestamp;
	}

	public void setActualTimestamp(Timestamp actualTimestamp) {
		this.actualTimestamp = actualTimestamp;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if ( ! (o instanceof CrankshaftHistoryId)) {
			return false;
		}
		CrankshaftHistoryId other = (CrankshaftHistoryId) o;
		return this.crankshaftId.equals(other.crankshaftId)
			&& this.processPointId.equals(other.processPointId)
			&& this.actualTimestamp.equals(other.actualTimestamp);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.crankshaftId.hashCode();
		hash = hash * prime + this.processPointId.hashCode();
		hash = hash * prime + this.actualTimestamp.hashCode();
		return hash;
	}

}
