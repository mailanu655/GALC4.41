package com.honda.galc.entity.product;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.honda.galc.entity.AbstractEntity;

@Entity
@Table(name="TRAILER_STATUS")
public class ShippingTrailerStatus extends AbstractEntity {
	@Id
	@Column(name="TRAILER_ID")
	private int trailerId;

	private int status;

	@Column(name="CREATE_TIMESTAMP")
	private Timestamp createTimestamp;

	private static final long serialVersionUID = 1L;

	public ShippingTrailerStatus() {
		super();
	}

	public int getTrailerId() {
		return this.trailerId;
	}

	public void setTrailerId(int trailerId) {
		this.trailerId = trailerId;
	}
	
	public Integer getId() {
		return getTrailerId();
	}

	public int getStatus() {
		return this.status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Timestamp getCreateTimestamp() {
		return this.createTimestamp;
	}

	public void setCreateTimestamp(Timestamp createTimestamp) {
		this.createTimestamp = createTimestamp;
	}

}
