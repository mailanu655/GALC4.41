package com.honda.galc.entity.product;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;

@Embeddable
public class ConrodHistoryId implements Serializable {
	@Column(name="CONROD_ID")
	private String conrodId;

	@Column(name="PROCESS_POINT_ID")
	private String processPointId;

	@Column(name="ACTUAL_TIMESTAMP")
	private Timestamp actualTimestamp;

	private static final long serialVersionUID = 1L;

	public ConrodHistoryId() {
		super();
	}
	
	public ConrodHistoryId(String conrodId, String processPointId) {
		this.conrodId = conrodId;
		this.processPointId = processPointId;
		setActualTimestamp(new Timestamp(System.currentTimeMillis()));
	}

	public String getConrodId() {
		return StringUtils.trim(this.conrodId);
	}

	public void setConrodId(String conrodId) {
		this.conrodId = conrodId;
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
		if ( ! (o instanceof ConrodHistoryId)) {
			return false;
		}
		ConrodHistoryId other = (ConrodHistoryId) o;
		return this.conrodId.equals(other.conrodId)
			&& this.processPointId.equals(other.processPointId)
			&& this.actualTimestamp.equals(other.actualTimestamp);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.conrodId.hashCode();
		hash = hash * prime + this.processPointId.hashCode();
		hash = hash * prime + this.actualTimestamp.hashCode();
		return hash;
	}

}
